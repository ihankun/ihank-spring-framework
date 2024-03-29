package io.ihankun.framework.mongoplus.build;

import com.mongodb.BasicDBObject;
import io.ihankun.framework.mongoplus.conditions.BuildCondition;
import io.ihankun.framework.mongoplus.conditions.MongoPlusBasicDBObject;
import io.ihankun.framework.mongoplus.conditions.accumulator.Accumulator;
import io.ihankun.framework.mongoplus.constant.SqlOperationConstant;
import io.ihankun.framework.mongoplus.model.GroupField;
import io.ihankun.framework.mongoplus.toolkit.CollUtil;
import io.ihankun.framework.mongoplus.toolkit.StringUtils;

import java.util.List;

/**
 * @author hankun
 * @project mongo-plus
 * @description 使用建造者构建group的条件
 * @date 2023-11-13 17:41
 **/
public class GroupBuilder {

    private MongoPlusBasicDBObject basicDBObject;

    public GroupBuilder() {
        this.basicDBObject = new MongoPlusBasicDBObject();
    }

    public GroupBuilder withAccumulatorList(List<Accumulator> accumulatorList) {
        if (CollUtil.isNotEmpty(accumulatorList)) {
            basicDBObject = BuildCondition.buildGroup(accumulatorList);
        }
        return this;
    }

    public GroupBuilder withId(String id) {
        if (StringUtils.isNotBlank(id)) {
            basicDBObject.put(SqlOperationConstant._ID, "$" + id);
        }
        return this;
    }

    public GroupBuilder withIdList(List<GroupField> idList) {
        if (CollUtil.isNotEmpty(idList)) {
            for (GroupField groupField : idList) {
                basicDBObject.put(SqlOperationConstant._ID,new BasicDBObject(){{
                    put(groupField.getGroupField(),"$" + groupField.getField());
                }});
            }
        }
        return this;
    }

    public GroupBuilder withIdAccumulator(List<Accumulator> idAccumulator) {
        if (CollUtil.isNotEmpty(idAccumulator)) {
            basicDBObject.put(SqlOperationConstant._ID, BuildCondition.buildGroup(idAccumulator));
        }
        return this;
    }

    public BasicDBObject build() {
        return basicDBObject;
    }

}
