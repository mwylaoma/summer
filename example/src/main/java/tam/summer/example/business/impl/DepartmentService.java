package tam.summer.example.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tam.summer.database.meta.Page;
import tam.summer.example.business.inter.IDepartmentService;
import tam.summer.example.domain.entity.Department;
import tam.summer.example.persistence.inter.IDepartmentDao;

import java.util.*;

/**
 * Created by tanqimin on 2015/10/28.
 */
@Service
public class DepartmentService
        implements IDepartmentService {
    @Autowired
    private IDepartmentDao departmentDao;

    @Override
    public Department findDepartmentById(String id) {
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            String     uuid       = UUID.randomUUID().toString();
            Department department = new Department();
            department.setId(uuid);
            department.setCode(uuid);
            department.setVersion(1);
            department.setName(uuid);
            department.setCreated(new Date());
            department.setModified(new Date());
            departments.add(department);
        }
        departmentDao.save(departments);

        departments.forEach(department -> department.setVersion(department.getVersion() + 1));
//        departmentDao.update(departments, "id, version");
//
//        for (Department department : departments) {
//            departmentDao.deleteById(department.getId());
//        }

        return departmentDao.getById(id);
    }

    @Override
    public int save(Department[] departments) {
        return departmentDao.save(Arrays.asList(departments)).length;
    }

    @Override
    public List<Department> findByInClause() {
        List<String> ids = new ArrayList<>();
        ids.add(null);
        //        ids.add("0000ed27-2825-40dd-a673-36628f11c555");
        //        ids.add("0001bb56-b26f-4be2-8791-388f8d6fe9a8");
        //        ids.add("0005d2de-448f-4f95-a7af-9ae072e3662e");
        //        ids.add("0008b997-fad5-438e-b5fc-681dc5a016bc");


        return departmentDao.findBy(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<Department> findAllDepartment() throws Exception {

        List<Department> departments = departmentDao.findAllDepartment();
        for (int i = 0; i < departments.size(); i++) {
            if(i == 2)
                throw new Exception("1111111111111111111");

            Department department = departments.get(i);
            department.setName("ABCD" + i);
        }

        return departmentDao.findByPage(1, 20);
    }

    @Override
    public Page<String> findAllDepartmentName() {
        return departmentDao.findDepartmentNameByPage(1, 20);
    }
}
