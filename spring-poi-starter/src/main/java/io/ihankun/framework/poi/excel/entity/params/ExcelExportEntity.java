package io.ihankun.framework.poi.excel.entity.params;

import lombok.Data;

import java.util.List;

/**
 * excel 导出工具类,对cell类型做映射
 *
 * @author hankun
 */
@Data
public class ExcelExportEntity extends ExcelBaseEntity implements Comparable<ExcelExportEntity> {


    public ExcelExportEntity() {

    }

    public ExcelExportEntity(String name) {
        super.name = name;
    }

    public ExcelExportEntity(String name, Object key) {
        super.name = name;
        this.key = key;
    }

    public ExcelExportEntity(String name, Object key, int width) {
        super.name = name;
        this.width = width;
        this.key = key;
    }

    /**
     * 如果是MAP导出,这个是map的key
     */
    private Object                  key;

    private double                  width           = 10;

    private double                  height          = 10;

    /**
     * 图片的类型,1是文件,2是数据库
     */
    private int                     exportImageType = 0;

    /**
     * 排序顺序
     */
    private int                     orderNum        = 0;

    /**
     * 是否支持换行
     */
    private boolean                 isWrap;

    /**
     * 是否需要合并
     */
    private boolean                 needMerge;
    /**
     * 单元格纵向合并
     */
    private boolean                 mergeVertical;
    /**
     * 合并依赖`
     */
    private int[]                   mergeRely;
    /**
     * 后缀
     */
    private String                  suffix;
    /**
     * 统计
     */
    private boolean                isStatistics;

    private String                  numFormat;
    /**
     *  是否隐藏列
     */
    private boolean                isColumnHidden;
    /**
     * 枚举导出属性字段
     */
    private String                  enumExportField;
    /**
     * 脱敏规则
     */
    private String                  desensitizationRule;

    private List<ExcelExportEntity> list;

    @Override
    public int compareTo(ExcelExportEntity prev) {
        return this.getOrderNum() - prev.getOrderNum();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExcelExportEntity other = (ExcelExportEntity) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }
}
