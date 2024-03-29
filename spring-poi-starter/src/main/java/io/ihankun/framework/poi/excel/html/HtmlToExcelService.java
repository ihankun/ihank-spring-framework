package io.ihankun.framework.poi.excel.html;

import io.ihankun.framework.poi.excel.export.styler.ExcelExportStylerDefaultImpl;
import io.ihankun.framework.poi.excel.html.css.CssParseService;
import io.ihankun.framework.poi.excel.html.css.ICssConvertToExcel;
import io.ihankun.framework.poi.excel.html.css.impl.*;
import io.ihankun.framework.poi.excel.html.entity.ExcelCssConstant;
import io.ihankun.framework.poi.excel.html.entity.HtmlCssConstant;
import io.ihankun.framework.poi.excel.html.entity.style.CellStyleEntity;
import io.ihankun.framework.poi.util.PoiMergeCellUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.RuntimeErrorException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 读取Table数据生成Excel
 *
 * @author hankun
 */
public class HtmlToExcelService {
    private final Logger                          LOGGER         = LoggerFactory
        .getLogger(HtmlToExcelService.class);

    //样式
    private static final List<ICssConvertToExcel> STYLE_APPLIERS = new LinkedList<ICssConvertToExcel>();
    static {
        STYLE_APPLIERS.add(new AlignCssConvertImpl());
        STYLE_APPLIERS.add(new BackgroundCssConvertImpl());
        STYLE_APPLIERS.add(new BorderCssConverImpl());
        STYLE_APPLIERS.add(new TextCssConvertImpl());
    }

    //Cell 高宽
    private static final List<ICssConvertToExcel> SHEET_APPLIERS = new LinkedList<ICssConvertToExcel>();
    static {
        SHEET_APPLIERS.add(new WidthCssConverImpl());
        SHEET_APPLIERS.add(new HeightCssConverImpl());
    }
    private Sheet                  sheet;
    private Map<String, Object>    cellsOccupied = new HashMap<String, Object>();
    private Map<String, CellStyle> cellStyles    = new HashMap<String, CellStyle>();
    private CellStyle              defaultCellStyle;
    private int                    maxRow        = 0;
    private CssParseService cssParse      = new CssParseService();

    // --
    // private methods

    private void processTable(Element table) {
        int rowIndex = 0;
        if (maxRow > 0) {
            // blank row
            maxRow += 2;
            rowIndex = maxRow;
        }
        LOGGER.debug("Interate Table Rows.");
        String freezeCol = null;
        int freezeColIndex = -1;
        for (Element row : table.select("tr")) {
            LOGGER.debug("Parse Table Row [{}]. Row Index [{}].", row, rowIndex);
            String freezeRow = row.attr(ExcelCssConstant.FREEZE_ROW);
            if ("true".equals(freezeRow)) {
                sheet.createFreezePane(0, rowIndex + 1, 0, rowIndex + 1);
            }
            int colIndex = 0;
            LOGGER.debug("Interate Cols.");
            for (Element td : row.select("td, th")) {
                freezeCol = td.attr(ExcelCssConstant.FREEZE_COL);
                if ("true".equals(freezeCol)) {
                    if (colIndex > freezeColIndex) {
                        freezeColIndex = colIndex;
                    }
                }

                // skip occupied cell
                while (cellsOccupied.get(rowIndex + "_" + colIndex) != null) {
                    LOGGER.debug("Cell [{}][{}] Has Been Occupied, Skip.", rowIndex, colIndex);
                    ++colIndex;
                }
                LOGGER.debug("Parse Col [{}], Col Index [{}].", td, colIndex);
                int rowSpan = 0;
                String strRowSpan = td.attr("rowspan");
                if (StringUtils.isNotBlank(strRowSpan) && StringUtils.isNumeric(strRowSpan)) {
                    LOGGER.debug("Found Row Span [{}].", strRowSpan);
                    rowSpan = Integer.parseInt(strRowSpan);
                }
                int colSpan = 0;
                String strColSpan = td.attr("colspan");
                if (StringUtils.isNotBlank(strColSpan) && StringUtils.isNumeric(strColSpan)) {
                    LOGGER.debug("Found Col Span [{}].", strColSpan);
                    colSpan = Integer.parseInt(strColSpan);
                }
                // col span & row span
                if (colSpan > 1 && rowSpan > 1) {
                    spanRowAndCol(td, rowIndex, colIndex, rowSpan, colSpan);
                    colIndex += colSpan;
                }
                // col span only
                else if (colSpan > 1) {
                    spanCol(td, rowIndex, colIndex, colSpan);
                    colIndex += colSpan;
                }
                // row span only
                else if (rowSpan > 1) {
                    spanRow(td, rowIndex, colIndex, rowSpan);
                    ++colIndex;
                }
                // no span
                else {
                    Cell cell = createCell(td, getOrCreateRow(rowIndex), colIndex);
                    cell.setCellValue(getCellValue(cell, td.text()));
                    ++colIndex;
                }
            }
            ++rowIndex;
        }
        if (freezeColIndex != -1) {
            sheet.createFreezePane(freezeColIndex + 1, 0, freezeColIndex + 1, 0);
        }
    }

    public Workbook createSheet(String html, Workbook workbook) {
        Elements els = Jsoup.parseBodyFragment(html).select("table");
        Map<String, Sheet> sheets = new HashMap<String, Sheet>();
        Map<String, Integer> maxRowMap = new HashMap<String, Integer>();
        for (Element table : els) {
            String sheetName = table.attr(ExcelCssConstant.SHEET_NAME);

            if (StringUtils.isBlank(sheetName)) {
                LOGGER.error("table必须存在name属性!");
                throw new RuntimeErrorException(null, "table必须存在name属性");
            }

            if (sheets.containsKey(sheetName)) {
                maxRow = maxRowMap.get(sheetName);
                //cellStyles = csStyleMap.get(sheetName);
                //cellsOccupied = cellsOccupiedMap.get(sheetName);
                sheet = sheets.get(sheetName);
            } else {
                maxRow = 0;
                cellStyles.clear();
                cellsOccupied.clear();
                sheet = workbook.createSheet(sheetName);
            }
            //生成一个默认样式
            defaultCellStyle = new ExcelExportStylerDefaultImpl(workbook).stringNoneStyle(workbook,
                true);
            processTable(table);
            maxRowMap.put(sheetName, maxRow);
            sheets.put(sheetName, sheet);

        }
        return workbook;
    }

    private void spanRow(Element td, int rowIndex, int colIndex, int rowSpan) {
        LOGGER.debug("Span Row , From Row [{}], Span [{}].", rowIndex, rowSpan);
        mergeRegion(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex);
        for (int i = 0; i < rowSpan; ++i) {
            Row row = getOrCreateRow(rowIndex + i);
            createCell(td, row, colIndex);
            cellsOccupied.put((rowIndex + i) + "_" + colIndex, true);
        }
        Cell cell = getOrCreateRow(rowIndex).getCell(colIndex);
        cell.setCellValue(getCellValue(cell, td.text()));
    }

    private void spanCol(Element td, int rowIndex, int colIndex, int colSpan) {
        LOGGER.debug("Span Col, From Col [{}], Span [{}].", colIndex, colSpan);
        mergeRegion(rowIndex, rowIndex, colIndex, colIndex + colSpan - 1);
        Row row = getOrCreateRow(rowIndex);
        for (int i = 0; i < colSpan; ++i) {
            createCell(td, row, colIndex + i);
        }
        row.getCell(colIndex).setCellValue(getCellValue(row.getCell(colIndex), td.text()));
    }

    private RichTextString getCellValue(Cell cell, String text) {
        RichTextString richText;
        if (cell instanceof HSSFCell) {
            richText = new HSSFRichTextString(text);
        } else {
            richText = new XSSFRichTextString(text);
        }
        return richText;
    }

    private void spanRowAndCol(Element td, int rowIndex, int colIndex, int rowSpan, int colSpan) {
        LOGGER.debug("Span Row And Col, From Row [{}], Span [{}].", rowIndex, rowSpan);
        LOGGER.debug("From Col [{}], Span [{}].", colIndex, colSpan);
        mergeRegion(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex + colSpan - 1);
        for (int i = 0; i < rowSpan; ++i) {
            Row row = getOrCreateRow(rowIndex + i);
            for (int j = 0; j < colSpan; ++j) {
                createCell(td, row, colIndex + j);
                cellsOccupied.put((rowIndex + i) + "_" + (colIndex + j), true);
            }
        }
        getOrCreateRow(rowIndex).getCell(colIndex).setCellValue(
                getCellValue(getOrCreateRow(rowIndex).getCell(colIndex),td.text()));
    }

    private Cell createCell(Element td, Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            LOGGER.debug("Create Cell [{}][{}].", row.getRowNum(), colIndex);
            cell = row.createCell(colIndex);
        }
        return applyStyle(td, cell);
    }

    private Cell applyStyle(Element td, Cell cell) {
        String style = td.attr(HtmlCssConstant.STYLE);
        CellStyle cellStyle = defaultCellStyle;
        if (StringUtils.isNotBlank(style)) {
            CellStyleEntity styleEntity = cssParse.parseStyle(style.trim());
            cellStyle = cellStyles.get(styleEntity.toString());
            if (cellStyle == null) {
                LOGGER.debug("No Cell Style Found In Cache, Parse New Style.");
                cellStyle = cell.getRow().getSheet().getWorkbook().createCellStyle();
                cellStyle.cloneStyleFrom(defaultCellStyle);
                for (ICssConvertToExcel cssConvert : STYLE_APPLIERS) {
                    cssConvert.convertToExcel(cell, cellStyle, styleEntity);
                }
                cellStyles.put(styleEntity.toString(), cellStyle);
            }
            for (ICssConvertToExcel cssConvert : SHEET_APPLIERS) {
                cssConvert.convertToExcel(cell, cellStyle, styleEntity);
            }
            if (cellStyles.size() >= 4000) {
                LOGGER.info(
                    "Custom Cell Style Exceeds 4000, Could Not Create New Style, Use Default Style.");
                cellStyle = defaultCellStyle;
            }
        } else {
            LOGGER.debug("Style is null ,Use Default Cell Style.");
            cellStyle = defaultCellStyle;
        }

        cell.setCellStyle(cellStyle);
        return cell;
    }

    private Row getOrCreateRow(int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            LOGGER.debug("Create New Row [{}].", rowIndex);
            row = sheet.createRow(rowIndex);
            if (rowIndex > maxRow) {
                maxRow = rowIndex;
            }
        }
        return row;
    }

    private void mergeRegion(int firstRow, int lastRow, int firstCol, int lastCol) {
        LOGGER.debug("Merge Region, From Row [{}], To [{}].", firstRow, lastRow);
        LOGGER.debug("From Col [{}], To [{}].", firstCol, lastCol);
        PoiMergeCellUtil.addMergedRegion(sheet,firstRow, lastRow, firstCol, lastCol);
    }
}
