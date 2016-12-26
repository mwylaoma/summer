package tam.summer.example.persistence.inter;

import tam.summer.database.meta.Page;
import tam.summer.database.persistence.IModelDao;
import tam.summer.example.domain.entity.Department;

import java.util.Date;
import java.util.List;

/**
 * Created by tanqimin on 2015/11/4.
 */
public interface IDepartmentDao extends IModelDao<Department> {
    Page<Department> findByPage(int pageNumber, int pageSize);

    List<Date> findAllIds();

    List<Department> findBy(List<String> ids);

    Page<String> findDepartmentNameByPage(
            int pageNumber, int pageSize);

    List<Department> findAllDepartment();

}
