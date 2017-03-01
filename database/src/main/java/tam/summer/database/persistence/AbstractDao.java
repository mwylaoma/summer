package tam.summer.database.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import tam.summer.common.ArrayUtil;
import tam.summer.common.StringUtil;
import tam.summer.common.ValidateUtil;
import tam.summer.common.config.Config;
import tam.summer.common.reflect.ReflectUtil;
import tam.summer.database.DatabaseTemplate;
import tam.summer.database.cache.QueryCache;
import tam.summer.database.dialect.IDialect;
import tam.summer.database.exception.DatabaseException;
import tam.summer.database.meta.MetaData;
import tam.summer.database.meta.Page;
import tam.summer.database.meta.Sql;
import tam.summer.database.meta.schema.ColumnMeta;
import tam.summer.database.meta.schema.ColumnType;
import tam.summer.database.meta.schema.TableMeta;
import tam.summer.database.persistence.handler.PropertyHandler;
import tam.summer.database.persistence.handler.PropertyHandlerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by tanqimin on 2015/11/4.
 */
@SuppressWarnings("unchecked")
abstract class AbstractDao<TModel> {
    private Logger logger = LogManager.getLogger(getClass());
    private Class<TModel> clazz;
    private TableMeta     tableMeta;
    private boolean       enableCached;

    public AbstractDao() {
        this.clazz = (Class<TModel>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.tableMeta = MetaData.table(this.clazz);
        this.enableCached = tableMeta.getCached();
    }

    abstract protected DatabaseTemplate getDatabaseTemplate();

    /**
     * 执行存储过程需使用SimpleJdbcCall
     *
     * @return
     */
    protected SimpleJdbcCall createJdbcCall() {
        return new SimpleJdbcCall(getDatabaseTemplate());
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param params
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            String sql,
            Object... params) {
        return find(clazz, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            Sql sql) {
        return find(clazz, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回多行结果集
     *
     * @param clazz
     * @param sql
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> find(
            Class<TView> clazz,
            Sql sql,
            boolean cached) {
        List<TView> result;
        if (cached) {
            result = this.getCache(sql.getSql(), sql.getParams());
            if (result != null) {
                return result;
            }
        }
        result = getDatabaseTemplate().query(sql.getSql(), sql.getParams(), (resultSet, i) -> this.toEntity(clazz, resultSet));
        if (cached) {
            this.addCache(sql.getSql(), sql.getParams(), result);
        }
        return result;
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @param params
     * @return
     */
    public List<TModel> findBy(
            String where,
            Object... params) {
        return findBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @return
     */
    public List<TModel> findBy(
            Sql where) {
        return findBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回多行结果集
     *
     * @param where
     * @return
     */
    public List<TModel> findBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return this.find(this.clazz, sql, cached);
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param params
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            String sql,
            Object... params) {
        return findTop(clazz, top, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            Sql sql) {
        return findTop(clazz, top, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回指定行数的结果集
     *
     * @param clazz
     * @param top
     * @param sql
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> List<TView> findTop(
            Class<TView> clazz,
            int top,
            Sql sql,
            boolean cached) {
        Sql querySql = getDialect().selectTop(1, top, sql.getSql(), sql.getParams());
        return find(clazz, querySql, cached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @param params
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            String where,
            Object... params) {
        return findTopBy(top, new Sql(where, params));
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            Sql where) {
        return findTopBy(top, where, this.enableCached);
    }

    /**
     * 根据WHERE条件，执行SQL语句，返回指定行数的结果集
     *
     * @param top
     * @param where
     * @param cached
     * @return
     */
    public List<TModel> findTopBy(
            int top,
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return findTop(clazz, top, sql, cached);
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param params         SQL查询参数
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            String sql,
            Object... params) {
        return findByPage(clazz, isPageable, currentPage, recordsPerPage, new Sql(sql, params));
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql) {
        return findByPage(clazz, isPageable, currentPage, recordsPerPage, sql, this.enableCached);
    }

    /**
     * 执行SQL语句，返回分页结果集
     *
     * @param clazz          返回的数据类型
     * @param isPageable     是否分页
     * @param currentPage    当前页码
     * @param recordsPerPage 每页记录数
     * @param sql            SQL查询语句
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> Page<TView> findByPage(
            Class<TView> clazz,
            boolean isPageable,
            int currentPage,
            int recordsPerPage,
            Sql sql,
            boolean cached) {
        int curPage    = currentPage;
        int recPerPage = recordsPerPage;

        int totalRow;
        int totalPage;

        if (isPageable == false) {
            curPage = 1;
            recPerPage = Integer.MAX_VALUE;
        }

        Sql querySql = getDialect().selectTop(curPage, recPerPage, sql.getSql(), sql.getParams());

        List<TView> data = find(clazz, querySql, cached);

        if (isPageable == false) {
            totalRow = data.size();
            totalPage = 1;
        } else {
            Sql countSql = getDialect().count(sql.getSql(), sql.getParams());
            totalRow = count(countSql, cached);
            totalPage = totalRow / recordsPerPage;

            if (totalRow % recordsPerPage != 0) {
                totalPage++;
            }
        }

        if (isPageable && totalPage > 0 && curPage > totalPage) {
            logger.info(String.format("当前页数(%s)大于总页数(%s), 加载最后一页数据！", curPage, totalPage));
            curPage = totalPage;
            return findByPage(clazz, isPageable, curPage, recordsPerPage, sql, cached);
        }

        return new Page<>(data, curPage, recPerPage, totalPage, totalRow);
    }

    /**
     * 根据主键获取记录
     *
     * @param clazz 返回的数据类型
     * @param id    主键
     * @return
     */
    public TModel getById(
            Class<TModel> clazz,
            Object id) {
        return getById(clazz, id, this.enableCached);
    }

    /**
     * 根据主键获取记录
     *
     * @param clazz  返回的数据类型
     * @param id     主键
     * @param cached
     * @return
     */
    public TModel getById(
            Class<TModel> clazz,
            Object id,
            boolean cached) {
        Sql          sql     = getDialect().selectById(clazz, id);
        List<TModel> tModels = findTop(clazz, 1, sql, cached);
        return tModels.size() > 0 ? tModels.get(0) : null;
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param params  SQL查询参数
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            String sql,
            Object... params) {
        return get(clazz, new Sql(sql, params));
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            Sql sql) {
        return get(clazz, sql, this.enableCached);
    }

    /**
     * 根据SQL语句获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param clazz   返回的数据类型
     * @param sql     SQL查询语句
     * @param cached
     * @param <TView>
     * @return
     */
    protected <TView> TView get(
            Class<TView> clazz,
            Sql sql,
            boolean cached) {
        Sql         querySql = getDialect().selectTop(1, 1, sql.getSql(), sql.getParams());
        List<TView> result   = find(clazz, querySql, cached);
        return result.size() == 0 ? null : result.get(0);
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @param params
     * @return
     */
    public TModel getBy(
            String where,
            Object... params) {
        return getBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @return
     */
    public TModel getBy(
            Sql where) {
        return getBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件获取记录，如果SQL语句查询的结果集有多行，只返回第1行
     *
     * @param where
     * @param cached
     * @return
     */
    public TModel getBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().select(clazz).where(where);
        return get(clazz, sql, cached);
    }


    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys
     * @param cached
     * @return
     */
    public List<TModel> findByIds(
            Collection primaryKeys,
            boolean cached) {
        if (ValidateUtil.isBlank(primaryKeys)) {
            return Collections.EMPTY_LIST;
        }
        Sql sql = getDialect().selectByIds(clazz, primaryKeys.toArray());
        return this.find(clazz, sql, cached);
    }

    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys 主键集合
     * @return
     */
    public List<TModel> findByIds(
            Object... primaryKeys) {
        return findByIds(Arrays.asList(primaryKeys));
    }

    /**
     * 根据主键查询结果集
     *
     * @param primaryKeys
     * @return
     */
    public List<TModel> findByIds(
            Collection primaryKeys) {
        return findByIds(primaryKeys, this.enableCached);
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz  返回的数据类型
     * @param sql    SQL查询语句
     * @param params SQL查询参数
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            String sql,
            Object... params) {
        return queryForObject(clazz, new Sql(sql, params));
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz
     * @param sql
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            Sql sql) {
        return queryForObject(clazz, sql, this.enableCached);
    }

    /**
     * 根据SQL语句返回Object，用于执行COUNT等结果集只有一个值的查询
     *
     * @param clazz
     * @param sql
     * @param cached
     * @param <T>
     * @return
     */
    protected <T> T queryForObject(
            Class<T> clazz,
            Sql sql,
            boolean cached) {
        List<T> resultList = find(clazz, sql, cached);
        if (resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql    SQL查询语句
     * @param params SQL查询参数
     * @return
     */
    protected int count(
            String sql,
            Object... params) {
        return count(new Sql(sql, params));
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql
     * @return
     */
    protected int count(Sql sql) {
        return count(sql, this.enableCached);
    }

    /**
     * 根据SQL语句返回记录数
     *
     * @param sql
     * @param cached
     * @return
     */
    protected int count(
            Sql sql,
            boolean cached) {
        return queryForObject(Integer.class, sql, cached);
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @param params
     * @return
     */
    public int countBy(
            String where,
            Object... params) {
        return countBy(new Sql(where, params));
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @return
     */
    public int countBy(
            Sql where) {
        return countBy(where, this.enableCached);
    }

    /**
     * 根据WHERE条件，返回记录数
     *
     * @param where
     * @param cached
     * @return
     */
    public int countBy(
            Sql where,
            boolean cached) {
        Sql sql = getDialect().countBy(clazz, where.getSql(), where.getParams());
        return queryForObject(Integer.class, sql, cached);
    }

    /**
     * 保存
     *
     * @param clazz
     * @param tModel
     * @param <TModel>
     * @return
     */
    public <TModel> int save(
            Class<TModel> clazz,
            TModel tModel) {
        if (ValidateUtil.isBlank(tModel)) {
            return 0;
        }

        Sql sql = getDialect().insert(clazz, tModel);
        return execute(sql);
    }

    /**
     * 批量保存
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int[] save(
            Class<TModel> clazz,
            Collection<TModel> models) {
        int[] result = new int[0];

        if (ValidateUtil.isBlank(models)) return result;
        //清除缓存
        this.purgeCache();

        List<ColumnMeta> columns = MetaData.columns(clazz, ColumnType.WRITABLE);
        String           sql     = getDialect().insert(clazz, columns);
        List<Object[]>   params  = new ArrayList<>();

        Object[] param;
        TModel   model;
        try {
            for (Iterator<TModel> modelIterator = models.iterator(); modelIterator.hasNext(); ) {
                model = modelIterator.next();
                param = new Object[columns.size()];
                for (int i = 0; i < columns.size(); i++) {
                    param[i] = ReflectUtil.getGetter(clazz, columns.get(i).getFieldName()).invoke(model);
                }
                params.add(param);
            }

            result = getDatabaseTemplate().batchUpdate(sql, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e);
        } finally {
            //释放资源
            param = null;
            model = null;
            columns = null;
            sql = null;
            params = null;
        }

        return result;
    }

    /**
     * 更新
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            TModel model) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        Sql sql = getDialect().update(clazz, model);
        return execute(sql);
    }

    /**
     * 根据指定列更新
     *
     * @param clazz
     * @param model
     * @param columns
     * @param <TModel>
     * @return
     */
    public <TModel> int update(
            Class<TModel> clazz,
            TModel model,
            String columns) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        List<TModel> models = new ArrayList<>();
        models.add(model);
        return update(clazz, models, columns)[0];
    }

    /**
     * 批量更新
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int[] update(
            Class<TModel> clazz,
            Collection<TModel> models) {
        return update(clazz, models, "*");
    }

    /**
     * 根据制定列批量更新
     *
     * @param clazz
     * @param models
     * @param columns
     * @param <TModel>
     * @return
     */
    public <TModel> int[] update(
            Class<TModel> clazz,
            Collection<TModel> models,
            String columns) {
        if (ValidateUtil.isBlank(models)) {
            return new int[0];
        }

        this.purgeCache();

        String           col           = columns;
        List<ColumnMeta> updateColumns = new ArrayList<>();
        List<ColumnMeta> dbColumns     = MetaData.columns(clazz, ColumnType.WRITABLE);

        if (ValidateUtil.isBlank(col) || col.equals("*")) {
            updateColumns.addAll(dbColumns);
        } else {
            for (String columnStr : col.split(",")) {
                ColumnMeta column = MetaData.getColumnByColumnName(clazz, columnStr.trim());
                if (column == null) {
                    throw new DatabaseException(String.format("校验失败：数据库字段 %s 不存在", columnStr));
                }
                updateColumns.add(column);
            }
        }

        Sql            sql        = Sql.Update(MetaData.table(clazz).getTableName()).append("SET");
        ColumnMeta     primaryKey = MetaData.getPrimaryKey(clazz);
        List<Object[]> params     = new ArrayList<>();

        List<Object> param;
        try {
            for (TModel model : models) {
                param = new ArrayList<>();

                for (ColumnMeta updateColumn : updateColumns) {
                    if (updateColumn.getIsPrimaryKey()) {
                        continue;
                    }
                    if (sql.getCompleted() == false) {
                        sql.append(String.format("%s = ?,", updateColumn.getColumnName()));
                    }
                    param.add(ReflectUtil.getGetter(clazz, updateColumn.getFieldName()).invoke(model));
                }

                if (sql.getCompleted() == false) {
                    sql.where(String.format("%s = ?", primaryKey.getColumnName())).setCompleted(true);
                }

                param.add(ReflectUtil.getGetter(clazz, primaryKey.getFieldName()).invoke(model));//主键值
                params.add(convert(param));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }

        return getDatabaseTemplate().batchUpdate(sql.getSql().toString(), params);
    }

    /**
     * 删除
     *
     * @param clazz
     * @param model
     * @param <TModel>
     * @return
     */
    public <TModel> int delete(
            Class<TModel> clazz,
            TModel model) {
        if (ValidateUtil.isBlank(model)) {
            return 0;
        }
        Sql sql = getDialect().delete(clazz, model);
        return execute(sql);
    }

    /**
     * 批量删除
     *
     * @param clazz
     * @param models
     * @param <TModel>
     * @return
     */
    public <TModel> int delete(
            Class<TModel> clazz,
            Collection<TModel> models) {
        if (ValidateUtil.isBlank(models)) {
            return 0;
        }
        List<Object> idList     = new ArrayList<>();
        ColumnMeta   primaryKey = MetaData.getPrimaryKey(clazz);

        try {
            for (TModel model : models) {
                idList.add(ReflectUtil.getGetter(clazz, primaryKey.getFieldName()).invoke(model));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }

        return deleteByIds(clazz, convert(idList));
    }

    /**
     * 根据ID删除
     *
     * @param clazz
     * @param primaryKey
     * @param <TModel>
     * @return
     */
    public <TModel> int deleteById(
            Class<TModel> clazz,
            Object primaryKey) {
        if (ValidateUtil.isBlank(primaryKey)) {
            return 0;
        }
        Sql sql = getDialect().deleteById(clazz, primaryKey);
        return execute(sql);
    }

    /**
     * 根据ID集合删除
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByIds(
            Class<TModel> clazz,
            Object... primaryKeys) {
        if (ValidateUtil.isBlank(primaryKeys)) {
            return 0;
        }

        Sql sql = getDialect().deleteByIds(clazz, primaryKeys);
        return execute(sql);
    }

    /**
     * 根据ID集合删除
     *
     * @param clazz
     * @param primaryKeys
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByIds(
            Class<TModel> clazz,
            Collection primaryKeys) {
        return deleteByIds(clazz, convert(primaryKeys));
    }

    /**
     * 根据WHERE条件删除
     *
     * @param where
     * @param params
     * @return
     */
    protected int deleteBy(
            String where,
            Object... params) {
        Sql sql = new Sql().delete(MetaData.table(clazz).getTableName()).where(where, params);
        return execute(sql);
    }

    /**
     * 根据WHERE条件删除
     *
     * @param where
     * @return
     */
    protected int deleteBy(
            Sql where) {
        return deleteBy(where.getSql(), where.getParams());
    }

    /**
     * 根据字段删除
     *
     * @param clazz
     * @param fieldName
     * @param params
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByField(
            Class<TModel> clazz,
            String fieldName,
            Object... params) {
        if (ValidateUtil.isBlank(params)) {
            return 0;
        }
        Sql sql = new Sql().delete(MetaData.table(clazz).getTableName()).where(Sql.In(fieldName, params));
        return execute(sql);
    }

    /**
     * 根据字段删除
     *
     * @param clazz
     * @param fieldName
     * @param params
     * @param <TModel>
     * @return
     */
    protected <TModel> int deleteByField(
            Class<TModel> clazz,
            String fieldName,
            Collection params) {
        return deleteByField(clazz, fieldName, convert(params));
    }

    /**
     * 执行SQL语句，返回执行行数
     *
     * @param sql
     * @param params
     * @return
     */
    public int execute(
            String sql,
            Object... params) {
        int result = getDatabaseTemplate().update(sql, params);
        this.purgeCache();
        return result;
    }

    /**
     * 执行SQL语句，返回执行行数
     *
     * @param sql
     * @return
     */
    public int execute(Sql sql) {
        return execute(sql.getSql(), sql.getParams());
    }

    public int[] execute(Collection<Sql> sqls) {
        int[]         result   = new int[sqls.size()];
        int           index    = 0;
        Sql           sql;
        Iterator<Sql> iterator = sqls.iterator();
        while (iterator.hasNext()) {
            sql = iterator.next();
            result[index++] = execute(sql.getSql(), sql.getParams());
        }
        return result;
    }

    protected IDialect getDialect() {
        return getDatabaseTemplate().getDialect();
    }

    /**
     * 从缓存中读取数据
     *
     * @param sql    sql语句
     * @param params sql参数
     * @param <T>    返回的数据类型
     * @return T
     */
    protected <T> T getCache(
            String sql,
            Object[] params) {
        if (this.getCacheEnabled() && this.enableCached) {
            return (T) QueryCache.instance().get(tableMeta.getTableName(), clazz.getSimpleName(), sql, params);
        }
        return null;
    }

    private boolean getCacheEnabled() {return getDatabaseTemplate().getConfig().getCacheEnabled();}

    /**
     * 添加到缓存
     *
     * @param sql    sql语句
     * @param params sql参数
     * @param cache  要缓存的数据
     */
    protected void addCache(
            String sql,
            Object[] params,
            Object cache) {
        if (this.getCacheEnabled() && this.enableCached) {
            QueryCache.instance().add(tableMeta.getTableName(), clazz.getSimpleName(), sql, params, cache, tableMeta.getExpired());
        }
    }

    /**
     * 清除缓存 通过数据源名称＋表名称
     */
    public void purgeCache() {
        if (this.getCacheEnabled() && this.enableCached) {
            QueryCache.instance().purge(tableMeta.getTableName());
        }
    }

    /**
     * 删除指定sql＋params的缓存
     *
     * @param sql    sql语句
     * @param params sql参数
     */
    protected void removeCache(
            String sql,
            Object[] params) {
        if (this.enableCached) {
            QueryCache.instance().remove(tableMeta.getTableName(), clazz.getSimpleName(), sql, params);
        }
    }

    protected Class<TModel> getMClass() {
        return this.clazz;
    }

    /**
     * 把指定类型的数组转为Object数组
     *
     * @param params
     * @param <T>
     * @return
     */
    protected <T> Object[] convert(T... params) {
        if (ValidateUtil.isBlank(params)) {
            return new Object[0];
        }
        Object[] result = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            result[i] = params[i];
        }
        return result;
    }

    /**
     * 把指定类型的集合转为Object数组
     *
     * @param params
     * @param <T>
     * @return
     */
    protected <T> Object[] convert(Iterable<T> params) {
        if (params == null) {
            return new Object[0];
        }
        List        result   = new ArrayList<>();
        Iterator<T> iterator = params.iterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result.toArray();
    }

    private <TView> TView toEntity(
            Class<TView> clazz,
            ResultSet rs) {
        TView      entity     = null;                       //返回实体
        ColumnMeta columnMeta = null;                       //列 元数据
        String     columnName = null;                       //列名
        String     fieldName  = null;                       //属性名
        Method     setter     = null;                       //Setter方法
        Class<?>   paramType  = null;                       //Setter参数类型
        Object     propVal    = null;                       //属性值
        try {
            ResultSetMetaData rsmd        = rs.getMetaData();
            int               columnCount = rsmd.getColumnCount();
            if (columnCount > 1) {
                if (columnCount == 2 && rsmd.getColumnName(1).equals("rownumber")) {
                    //处理分页查询单个对象的情况
                    entity = (TView) rs.getObject(2);
                } else {
                    entity = clazz.newInstance();

                    for (Iterator<ColumnMeta> iterator = MetaData.columns(clazz, ColumnType.READ_ONLY).iterator(); iterator.hasNext(); ) {
                        columnMeta = iterator.next();

                        columnName = columnMeta.getColumnName();
                        fieldName = columnMeta.getFieldName();
                        //判断ResultSet中是否包含该列
                        if (hasColumn(rs, columnName) == false) continue;

                        //获取Setter方法
                        setter = ReflectUtil.getSetter(clazz, fieldName);
                        if (setter == null || setter.getParameterTypes().length != 1) continue;

                        //获取Setter参数值类型
                        paramType = setter.getParameterTypes()[0];

                        for (Iterator<PropertyHandler> propertyHandlerIterator = PropertyHandlerFactory.getHandlers().iterator(); propertyHandlerIterator
                                .hasNext(); ) {
                            PropertyHandler propertyHandler = propertyHandlerIterator.next();
                            if (propertyHandler.match(paramType) == false) continue;

                            propVal = propertyHandler.apply(rs, columnName);
                            setter.invoke(entity, propVal);
                            break;
                        }

                        //初始化字段
                        columnMeta = null;                      //列 元数据
                        columnName = null;                      //列名
                        fieldName = null;                       //属性名
                        setter = null;                          //Setter方法
                        paramType = null;                       //Setter参数类型
                        propVal = null;                         //属性值
                    }
                }
            } else if (columnCount == 1) {
                entity = (TView) rs.getObject(1);
            }
        } catch (Exception e) {
            StringBuilder errorMsg = new StringBuilder("转换ResultSet为实体对象时发生错误");
            if (ValidateUtil.isNotBlank(columnName)) errorMsg.append(", 数据库字段：").append(columnName);
            if (ValidateUtil.isNotBlank(fieldName)) errorMsg.append(", 属性名称：").append(fieldName);
            if (ValidateUtil.isNotBlank(paramType)) errorMsg.append(", 属性类型：").append(paramType.getName());
            if (ValidateUtil.isNotBlank(propVal)) errorMsg.append(", 属性值：").append(propVal);
            errorMsg.append(", 错误信息：").append(e.getMessage());
            logger.error(errorMsg.toString(), e);
            throw new DatabaseException(e);
        }
        return entity;
    }

    /**
     * 判断ResultSet中是否存在指定名称的列
     *
     * @param resultSet
     * @param columnName
     * @return
     */

    private boolean hasColumn(
            ResultSet resultSet,
            String columnName) {
        try {
            return resultSet.findColumn(columnName) > 0;
        } catch (SQLException sqlEx) {
            logger.info(String.format("ResultSet 的字段中不存在 %s 字段！", columnName));
            return false;
        }
    }
}
