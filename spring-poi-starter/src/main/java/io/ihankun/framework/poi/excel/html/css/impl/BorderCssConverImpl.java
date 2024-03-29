package io.ihankun.framework.poi.excel.html.css.impl;

import io.ihankun.framework.poi.excel.html.css.ICssConvertToExcel;
import io.ihankun.framework.poi.excel.html.css.ICssConvertToHtml;
import io.ihankun.framework.poi.excel.html.entity.style.CellStyleBorderEntity;
import io.ihankun.framework.poi.excel.html.entity.style.CellStyleEntity;
import io.ihankun.framework.poi.util.PoiCssUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.ihankun.framework.poi.excel.html.entity.HtmlCssConstant.*;

/**
 * 边框转换实现类
 *
 * @author hankun
 */
public class BorderCssConverImpl implements ICssConvertToExcel, ICssConvertToHtml {

    private static Logger log = LoggerFactory.getLogger(BorderCssConverImpl.class);

    @Override
    public String convertToHtml(Cell cell, CellStyle cellStyle, CellStyleEntity style) {
        return null;
    }

    @Override
    public void convertToExcel(Cell cell, CellStyle cellStyle, CellStyleEntity style) {
        if (style == null || style.getBorder() == null) {
            return;
        }
        CellStyleBorderEntity border = style.getBorder();
        for (String pos : new String[] { TOP, RIGHT, BOTTOM, LEFT }) {
            String posName = StringUtils.capitalize(pos.toLowerCase());
            // color
            String colorAttr = null;
            try {
                colorAttr = (String) MethodUtils.invokeMethod(border,
                    "getBorder" + posName + "Color");
            } catch (Exception e) {
                log.error("Set Border Style Error Caused.", e);
            }
            if (StringUtils.isNotEmpty(colorAttr)) {
                if (cell instanceof HSSFCell) {
                    HSSFColor poiColor = PoiCssUtils
                        .parseColor((HSSFWorkbook) cell.getSheet().getWorkbook(), colorAttr);
                    if (poiColor != null) {
                        try {
                            MethodUtils.invokeMethod(cellStyle, "set" + posName + "BorderColor",
                                poiColor.getIndex());
                        } catch (Exception e) {
                            log.error("Set Border Color Error Caused.", e);
                        }
                    }
                }
                if (cell instanceof XSSFCell) {
                    XSSFColor poiColor = PoiCssUtils.parseColor(colorAttr);
                    if (poiColor != null) {
                        try {
                            MethodUtils.invokeMethod(cellStyle, "set" + posName + "BorderColor",
                                poiColor);
                        } catch (Exception e) {
                            log.error("Set Border Color Error Caused.", e);
                        }
                    }
                }
            }
            // width
            int width = 0;
            try {
                String widthStr = (String) MethodUtils.invokeMethod(border,
                    "getBorder" + posName + "Width");
                if (PoiCssUtils.isNum(widthStr)) {
                    width = Integer.parseInt(widthStr);
                }
            } catch (Exception e) {
                log.error("Set Border Style Error Caused.", e);
            }
            String styleValue = null;
            try {
                styleValue = (String) MethodUtils.invokeMethod(border,
                    "getBorder" + posName + "Style");
            } catch (Exception e) {
                log.error("Set Border Style Error Caused.", e);
            }
            BorderStyle shortValue = BorderStyle.NONE;
            // empty or solid
            if (StringUtils.isBlank(styleValue) || "solid".equals(styleValue)) {
                if (width > 2) {
                    shortValue = BorderStyle.THICK;
                } else if (width > 1) {
                    shortValue = BorderStyle.MEDIUM;
                } else {
                    shortValue = BorderStyle.THIN;
                }
            } else if (ArrayUtils.contains(new String[] { NONE, HIDDEN }, styleValue)) {
                shortValue = BorderStyle.NONE;
            } else if (DOUBLE.equals(styleValue)) {
                shortValue = BorderStyle.DOUBLE;
            } else if (DOTTED.equals(styleValue)) {
                shortValue = BorderStyle.DOTTED;
            } else if (DASHED.equals(styleValue)) {
                if (width > 1) {
                    shortValue = BorderStyle.MEDIUM_DASHED;
                } else {
                    shortValue = BorderStyle.DASHED;
                }
            }
            // border style
            if (shortValue != BorderStyle.NONE) {
                try {
                    MethodUtils.invokeMethod(cellStyle, "setBorder" + posName, shortValue);
                } catch (Exception e) {
                    log.error("Set Border Style Error Caused.", e);
                }
            }
        }
    }

}
