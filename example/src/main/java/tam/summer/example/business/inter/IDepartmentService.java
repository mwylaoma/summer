package tam.summer.example.business.inter;

import tam.summer.database.meta.Page;
import tam.summer.example.domain.entity.Department;

import java.util.List;

/**
 * Created by tanqimin on 2015/10/28.
 */
public interface IDepartmentService {
    Page<Department> findAllDepartment() throws Exception;

    /**
     * 根据ID查询部门档案
     *
     * @param id
     * @return
     */
    Department findDepartmentById(String id);

    int save(Department[] departments);

    List<Department> findByInClause();

    Page<String> findAllDepartmentName();
}
