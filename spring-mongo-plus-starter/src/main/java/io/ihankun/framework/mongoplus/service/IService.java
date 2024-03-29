package io.ihankun.framework.mongoplus.service;

import io.ihankun.framework.mongoplus.conditions.aggregate.AggregateChainWrapper;
import io.ihankun.framework.mongoplus.conditions.aggregate.LambdaAggregateChainWrapper;
import io.ihankun.framework.mongoplus.conditions.query.LambdaQueryChainWrapper;
import io.ihankun.framework.mongoplus.conditions.query.QueryChainWrapper;
import io.ihankun.framework.mongoplus.conditions.update.LambdaUpdateChainWrapper;
import io.ihankun.framework.mongoplus.conditions.update.UpdateChainWrapper;
import io.ihankun.framework.mongoplus.execute.SqlExecute;
import io.ihankun.framework.mongoplus.model.PageParam;
import io.ihankun.framework.mongoplus.model.PageResult;
import io.ihankun.framework.mongoplus.support.SFunction;
import com.mongodb.client.ClientSession;
import com.mongodb.client.model.CreateIndexOptions;
import com.mongodb.client.model.DropIndexOptions;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * @author hankun
 * @since 2023-02-09 13:25
 **/
public interface IService<T> {

    /**
     * 添加
     * @param entity 添加的对象
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:27
    */
    Boolean save(T entity);

    /**
     * 添加
     * @param entity 添加的对象
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:27
     */
    Boolean save(ClientSession clientSession,T entity);


    /**
     * 添加多个
     * @param entityList 对象集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:56
    */
    Boolean saveBatch(Collection<T> entityList);

    /**
     * 添加多个
     * @param entityList 对象集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:56
     */
    Boolean saveBatch(ClientSession clientSession,Collection<T> entityList);

    /**
     * 添加或修改
     * @param entity 对象
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:57
    */
    Boolean saveOrUpdate(T entity);

    /**
     * 添加或修改
     * @param entity 对象
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:57
    */
    Boolean saveOrUpdate(ClientSession clientSession,T entity);

    /**
     * 批量添加或修改
     * @param entityList 对象集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:57
    */
    Boolean saveOrUpdateBatch(Collection<T> entityList);

    /**
     * 批量添加或修改
     * @param entityList 对象集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:57
    */
    Boolean saveOrUpdateBatch(ClientSession clientSession,Collection<T> entityList);

    /**
     * 修改
     * @param entity 修改的对象，需要包含id
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:28
    */
    Boolean updateById(T entity);

    /**
     * 修改
     * @param entity 修改的对象，需要包含id
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:28
    */
    Boolean updateById(ClientSession clientSession,T entity);

    Boolean updateBatchByIds(Collection<T> entityList);

    Boolean updateBatchByIds(ClientSession clientSession,Collection<T> entityList);

    /**
     * 通过列进行修改
     * @param entity 修改的实体
     * @param column 根据什么列修改
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:46
    */
    Boolean updateByColumn(T entity, SFunction<T, Object> column);

    /**
     * 通过列进行修改
     * @param entity 修改的实体
     * @param column 根据什么列修改
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:46
    */
    Boolean updateByColumn(ClientSession clientSession,T entity, SFunction<T, Object> column);

    Boolean updateByColumn(T entity, String column);

    Boolean updateByColumn(ClientSession clientSession,T entity, String column);

    /**
     * 根据条件删除
     * @param updateChainWrapper 条件
     * @return java.lang.Boolean
     * @author hankun
     * @date 2023/10/20 0:51
    */
    Boolean remove(UpdateChainWrapper<T,?> updateChainWrapper);

    /**
     * 根据条件删除
     * @param updateChainWrapper 条件
     * @param clientSession 事务接口
     * @return java.lang.Boolean
     * @author hankun
     * @date 2023/10/20 0:51
     */
    Boolean remove(ClientSession clientSession,UpdateChainWrapper<T,?> updateChainWrapper);

    /**
     * 根据条件修改
     * @param updateChainWrapper 条件
     * @return java.lang.Boolean
     * @author hankun
     * @date 2023/10/20 0:51
     */
    Boolean update(UpdateChainWrapper<T,?> updateChainWrapper);

    /**
     * 根据条件修改
     * @param updateChainWrapper 条件
     * @param clientSession 事务接口
     * @return java.lang.Boolean
     * @author hankun
     * @date 2023/10/20 0:51
     */
    Boolean update(ClientSession clientSession,UpdateChainWrapper<T,?> updateChainWrapper);

    /**
     * 根据id删除
     * @param id 数据id
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:47
    */
    Boolean removeById(Serializable id);

    /**
     * 根据id删除
     * @param id 数据id
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:47
    */
    Boolean removeById(ClientSession clientSession,Serializable id);

    /**
     * 根据字段删除
     * @param column 字段名
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 14:01
    */
    Boolean removeByColumn(SFunction<T, Object> column, Object value);

    /**
     * 根据字段删除
     * @param column 字段名
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 14:01
    */
    Boolean removeByColumn(ClientSession clientSession,SFunction<T, Object> column, Object value);

    /**
     * 根据字段删除
     * @param column 字段
     * @param value 值
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 14:05
    */
    Boolean removeByColumn(String column,Object value);

    /**
     * 根据字段删除
     * @param column 字段
     * @param value 值
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 14:05
    */
    Boolean removeByColumn(ClientSession clientSession,String column,Object value);

    /**
     * 根据id批量删除
     * @param idList id集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:59
    */
    Boolean removeBatchByIds(Collection<Serializable> idList);

    /**
     * 根据id批量删除
     * @param idList id集合
     * @return java.lang.Boolean
     * @author hankun
     * @since 2023/2/9 13:59
    */
    Boolean removeBatchByIds(ClientSession clientSession,Collection<Serializable> idList);

    /**
     * 查询所有
     * @return java.util.List<T>
     * @author hankun
     * @since 2023/2/10 9:48
    */
    List<T> list();

    /**
     * 查询所有
     * @return java.util.List<T>
     * @author hankun
     * @since 2023/2/10 9:48
    */
    List<T> list(ClientSession clientSession);

    List<T> aggregateList(AggregateChainWrapper<T,?> queryChainWrapper);

    List<T> aggregateList(ClientSession clientSession,AggregateChainWrapper<T,?> queryChainWrapper);

    T one(QueryChainWrapper<T,?> queryChainWrapper);

    T one(ClientSession clientSession,QueryChainWrapper<T,?> queryChainWrapper);

    /**
     * 获取单个，返回T类型的对象
     * <p style="color:red">注：如果查询到大于一条数据，会取第一条返回</p>
     * @return Map< String, Object>
     * @author hankun
     * @date 2023/7/20 23:20
     */
    T limitOne(QueryChainWrapper<T,?> queryChainWrapper);

    /**
     * 获取单个，返回T类型的对象
     * <p style="color:red">注：如果查询到大于一条数据，会取第一条返回</p>
     * @return Map< String, Object>
     * @author hankun
     * @date 2023/7/20 23:20
     */
    T limitOne(ClientSession clientSession,QueryChainWrapper<T,?> queryChainWrapper);

    List<T> list(QueryChainWrapper<T ,?> queryChainWrapper);

    List<T> list(ClientSession clientSession,QueryChainWrapper<T ,?> queryChainWrapper);

    List<T> list(AggregateChainWrapper<T,?> queryChainWrapper);

    List<T> list(ClientSession clientSession,AggregateChainWrapper<T,?> queryChainWrapper);

    long count();

    long count(ClientSession clientSession);

    long count(QueryChainWrapper<T,?> queryChainWrapper);

    long count(ClientSession clientSession,QueryChainWrapper<T,?> queryChainWrapper);

    /**
     * 分页查询
     * @param pageParam 分页参数对象
     * @return io.ihankun.framework.mongoplus.sql.model.PageResult<T>
     * @author hankun
     * @date 2023/6/25/025
    */
    PageResult<T> page(PageParam pageParam);

    /**
     * 分页查询
     * @param pageParam 分页参数对象
     * @return io.ihankun.framework.mongoplus.sql.model.PageResult<T>
     * @author hankun
     * @date 2023/6/25/025
    */
    PageResult<T> page(ClientSession clientSession,PageParam pageParam);

    /**
     * 分页查询
     * @param pageNum 当前页
     * @param pageSize 每页显示行数
     * @return io.ihankun.framework.mongoplus.sql.model.PageResult<T>
     * @author hankun
     * @date 2023/6/25/025
    */
    PageResult<T> page(Integer pageNum,Integer pageSize);

    /**
     * 分页查询
     * @param pageNum 当前页
     * @param pageSize 每页显示行数
     * @return io.ihankun.framework.mongoplus.sql.model.PageResult<T>
     * @author hankun
     * @date 2023/6/25/025
    */
    PageResult<T> page(ClientSession clientSession,Integer pageNum,Integer pageSize);

    PageResult<T> page(QueryChainWrapper<T, ?> queryChainWrapper, Integer pageNum, Integer pageSize);

    PageResult<T> page(ClientSession clientSession,QueryChainWrapper<T, ?> queryChainWrapper, Integer pageNum, Integer pageSize);

    PageResult<T> page(QueryChainWrapper<T, ?> queryChainWrapper, PageParam pageParam);

    PageResult<T> page(ClientSession clientSession,QueryChainWrapper<T, ?> queryChainWrapper, PageParam pageParam);

    /**
     * 根据id查询单个
     * @param id id
     * @return T
     * @author hankun
     * @date 2023/6/25/025
    */
    T getById(Serializable id);

    /**
     * 根据id查询单个
     * @param id id
     * @return T
     * @author hankun
     * @date 2023/6/25/025
    */
    T getById(ClientSession clientSession,Serializable id);

    List<T> getByIds(Collection<Serializable> ids);

    List<T> getByIds(ClientSession clientSession,Collection<Serializable> ids);

    /**
     * 查询sql，传入值为json，如{eq:XXX}
     * @author hankun
     * @date 2023/10/8 22:15
    */
    List<T> sql(String sql);

    List<T> sql(ClientSession clientSession,String sql);

    /**
     * 根据某一列查询
     * @param field 字段
     * @param fieldValue 字段值
     * @return List<T>
     * @author hankun
     * @date 2023/10/19 23:28
    */
    List<T> getByColumn(SFunction<T,Object> field,Object fieldValue);

    List<T> getByColumn(ClientSession clientSession,SFunction<T,Object> field,Object fieldValue);

    /**
     * 根据某一列查询
     * @param field 字段
     * @param fieldValue 字段值
     * @return T
     * @author hankun
     * @date 2023/10/19 23:30
    */
    List<T> getByColumn(String field,Object fieldValue);

    List<T> getByColumn(ClientSession clientSession,String field,Object fieldValue);

    /**
     * 创建索引，携带事务
     * @param bson 描述索引键的对象，该对象不能为 null
     * @return java.lang.String
     * @author hankun
     * @date 2023/11/15 14:04
     */
    String createIndex(ClientSession clientSession,Bson bson);

    /**
     * 创建索引
     * @param bson 描述索引键的对象，该对象不能为 null
     * @return java.lang.String
     * @author hankun
     * @date 2023/11/15 14:04
    */
    String createIndex(Bson bson);

    /**
     * 使用给定的键和选项创建索引。
     * @param clientSession 要与此操作关联的客户端会话
     * @param bson 描述索引键的对象，该对象不能为 null
     * @param indexOptions 指数的选项
     * @return java.lang.String
     * @author hankun
     * @date 2023/11/15 15:37
    */
    String createIndex(ClientSession clientSession, Bson bson, IndexOptions indexOptions);

    /**
     * 使用给定的键和选项创建索引。
     * @param bson 描述索引键的对象，该对象不能为 null
     * @param indexOptions 指数的选项
     * @return java.lang.String
     * @author hankun
     * @date 2023/11/15 15:37
     */
    String createIndex(Bson bson, IndexOptions indexOptions);

    /**
     * 创建多个索引
     * @param indexes 索引列表
     * @return java.util.List<java.lang.String>
     * @author hankun
     * @date 2023/11/15 15:34
    */
    List<String> createIndexes(List<IndexModel> indexes);

    /**
     * 创建多个索引
     * @param indexes 索引列表
     * @param createIndexOptions 创建索引时要使用的选项
     * @return java.util.List<java.lang.String> 索引名称列表
     * @author hankun
     * @date 2023/11/15 15:34
    */
    List<String> createIndexes(List<IndexModel> indexes, CreateIndexOptions createIndexOptions);

    /**
     * 创建多个索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param indexes 索引列表
     * @return 索引名称列表
     * @since 3.6
     * @mongodb.server.release 3.6
     * @mongodb.driver.manual reference/command/createIndexes Create indexes
     */
    /**
     * 创建多个索引
     * @param clientSession 要与此操作关联的客户端会话
     * @param indexes 索引列表
     * @return java.util.List<java.lang.String> 索引名称列表
     * @author hankun
     * @date 2023/11/15 15:35
    */
    List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes);

    /**
     * 创建多个索引
     * @param clientSession 要与此操作关联的客户端会话
     * @param indexes 索引列表
     * @param createIndexOptions 创建索引时要使用的选项
     * @return java.util.List<java.lang.String> 索引名称列表
     * @author hankun
     * @date 2023/11/15 15:36
    */
    List<String> createIndexes(ClientSession clientSession, List<IndexModel> indexes, CreateIndexOptions createIndexOptions);

    /**
     * 获取此集合中的所有索引。
     *
     * @return 列表索引可迭代接口
     */
    List<Document> listIndexes();

    /**
     * 获取此集合中的所有索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @return 列表索引可迭代接口
     */
    List<Document> listIndexes(ClientSession clientSession);

    /**
     * 删除给定其名称的索引。
     *
     * @param indexName 要删除的索引的名称
     */
    void dropIndex(String indexName);

    /**
     * 删除给定其名称的索引。
     *
     * @param indexName 要删除的索引的名称
     * @param dropIndexOptions 删除索引时要使用的选项
     */
    void dropIndex(String indexName, DropIndexOptions dropIndexOptions);

    /**
     * 在给定用于创建索引的键的情况下删除索引。
     *
     * @param keys 要删除的索引的键
     */
    void dropIndex(Bson keys);

    /**
     * 在给定用于创建索引的键的情况下删除索引。
     *
     * @param keys 要删除的索引的键
     * @param dropIndexOptions 删除索引时要使用的选项
     * @since 3.6
     */
    void dropIndex(Bson keys, DropIndexOptions dropIndexOptions);

    /**
     * 删除给定其名称的索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param indexName 要删除的索引的名称
     */
    void dropIndex(ClientSession clientSession, String indexName);

    /**
     * 在给定用于创建索引的键的情况下删除索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param keys 要删除的索引的键
     */
    void dropIndex(ClientSession clientSession, Bson keys);

    /**
     * 删除给定其名称的索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param indexName 要删除的索引的名称
     * @param dropIndexOptions 删除索引时要使用的选项
     */
    void dropIndex(ClientSession clientSession, String indexName, DropIndexOptions dropIndexOptions);

    /**
     * 在给定用于创建索引的键的情况下删除索引。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param keys 要删除的索引的键
     * @param dropIndexOptions 删除索引时要使用的选项
     */
    void dropIndex(ClientSession clientSession, Bson keys, DropIndexOptions dropIndexOptions);

    /**
     * 删除此集合上的所有索引，但 _id 上的默认值除外。
     *
     */
    void dropIndexes();

    /**
     * 删除此集合上的所有索引，但 _id 上的默认值除外。
     *
     * @param clientSession 要与此操作关联的客户端会话
     */
    void dropIndexes(ClientSession clientSession);

    /**
     * 删除此集合上的所有索引，但 _id 上的默认值除外。
     *
     * @param dropIndexOptions 删除索引时要使用的选项
     * @since 3.6
     */
    void dropIndexes(DropIndexOptions dropIndexOptions);

    /**
     * 删除此集合上的所有索引，但 _id 上的默认值除外。
     *
     * @param clientSession 要与此操作关联的客户端会话
     * @param dropIndexOptions 删除索引时要使用的选项
     */
    void dropIndexes(ClientSession clientSession, DropIndexOptions dropIndexOptions);

    SqlExecute getSqlOperation();

    Class<T> getGenericityClazz();


    LambdaQueryChainWrapper<T> lambdaQuery();

    LambdaAggregateChainWrapper<T> lambdaAggregate();

    LambdaUpdateChainWrapper<T> lambdaUpdate();

}
