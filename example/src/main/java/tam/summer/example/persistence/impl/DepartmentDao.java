package tam.summer.example.persistence.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tam.summer.database.DatabaseTemplate;
import tam.summer.database.meta.EmptyParamEnum;
import tam.summer.database.meta.Page;
import tam.summer.database.meta.Sql;
import tam.summer.database.persistence.ModelDao;
import tam.summer.example.domain.entity.Department;
import tam.summer.example.persistence.inter.IDepartmentDao;

import java.util.Date;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/4.
 */
@Repository
public class DepartmentDao
        extends ModelDao<Department>
        implements IDepartmentDao {
    @Autowired
    private DatabaseTemplate databaseTemplate;

    @Override
    public DatabaseTemplate getDatabaseTemplate() {
        return databaseTemplate;
    }

    @Override
    public Page<Department> findByPage(
            int pageNumber,
            int pageSize) {
        return super.findByPage(true, pageNumber, pageSize, "SELECT * FROM Department");
    }

    @Override
    public List<Date> findAllIds() {
        return super.find(Date.class, "SELECT created FROM DEPARTMENT");
    }

    @Override
    public List<Department> findBy(List<String> ids) {
        Sql sql = new Sql("SELECT * FROM Department").where(Sql.In("id", ids, EmptyParamEnum.FETCH_NONE));
        return super.find(sql);
    }

    @Override
    public Page<String> findDepartmentNameByPage(
            int pageNumber,
            int pageSize) {
        Sql sql = new Sql("SELECT name FROM Department");
        return super.findByPage(String.class, true, pageNumber, pageSize, sql);
    }

    @Override
    public List<Department> findAllDepartment() {
        return super.find("SELECT * FROM Department");
    }
}
