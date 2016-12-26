//package tam.summer.database.persistence;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import tam.summer.database.meta.Page;
//import tam.summer.database.meta.Sql;
//import tam.summer.example.domain.entity.Department;
//import tam.summer.example.persistence.inter.IDepartmentDao;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Created by tanqimin on 2015/11/26.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"file:src/main/resources/applicationContext.xml"})
//public class ModelDaoTest {
//    @Autowired
//    IDepartmentDao departmentDao;
//    String department_id_1 = null;
//    String department_id_2 = null;
//
//    @Before
//    public void before() {
//        department_id_1 = UUID.randomUUID().toString();
//        department_id_2 = UUID.randomUUID().toString();
//        List<Department> departments = new ArrayList<>();
//        Department       department1 = new Department();
//        department1.markNew(department_id_1);
//        department1.setCode("TEST_001");
//        department1.setName("业务部");
//        department1.setDisable(false);
//        department1.setDescription("业务部门");
//
//        Department department2 = new Department();
//        department2.markNew(department_id_2);
//        department2.setCode("TEST_002");
//        department2.setName("行政部");
//        department2.setDisable(false);
//
//        departments.add(department1);
//        departments.add(department2);
//
//        departmentDao.save(departments);
//    }
//
//    @After
//    public void after() {
//        departmentDao.deleteByIds(department_id_1, department_id_2);
//    }
//
//    @Test
//    public void findTest() {
//        Sql sql = new Sql("DECLARE @CODE VARCHAR(100);");
//        sql.append("SET @CODE = ?;", "TEST_001");
//        sql.append("SELECT * FROM dbo.DEPARTMENT WHERE Code = @CODE");
//        List<Department> departments = departmentDao.find(sql);
//        Assert.assertTrue(departments.size() == 1);
//    }
//
//    @Test
//    public void findBy() {
//        List<Department> departments = departmentDao.findBy("Code = ?", "TEST_002");
//        Assert.assertTrue(departments.size() == 1);
//    }
//
//    @Test
//    public void findTop() {
//        List<Department> departments = departmentDao.findTop(2, "SELECT * FROM DEPARTMENT WHERE Code = ?", "TEST_002");
//        Assert.assertTrue(departments.size() == 1);
//    }
//
//    @Test
//    public void findTopBy() {
//        List<Department> departments = departmentDao.findTopBy(2, "Code = ?", "TEST_002");
//        Assert.assertTrue(departments.size() == 1);
//    }
//
//    @Test
//    public void getById() {
//        Department department = departmentDao.getById(department_id_1);
//        Assert.assertTrue(department != null);
//    }
//
//    @Test
//    public void get() {
//        Department department = departmentDao.get("SELECT * FROM DEPARTMENT WHERE Code = ?", "TEST_002");
//        Assert.assertTrue(department != null);
//    }
//
//    @Test
//    public void getBy() {
//        Department department = departmentDao.getBy("Code = ?", "TEST_001");
//        Assert.assertTrue(department != null);
//    }
//
//    @Test
//    public void findByIds() {
//        List<Department> departments = departmentDao.findByIds(department_id_1, department_id_2);
//        Assert.assertTrue(departments.size() == 2);
//    }
//
//    @Test
//    public void findByPage() {
//        Page<Department> page = departmentDao.findByPage(true, 1, 2, "SELECT * FROM Department ORDER BY Code");
//        Assert.assertTrue(page.getData().size() == 2);
//    }
//
//    @Test
//    public void countBy() {
//        int count = departmentDao.countBy("Code = ?", "TEST_001");
//        Assert.assertTrue(count == 1);
//    }
//
//    @Test
//    public void save() {
//        Department entity = new Department();
//        entity.markNew();
//        entity.setCode(UUID.randomUUID().toString());
//        entity.setName(UUID.randomUUID().toString());
//        int result = departmentDao.save(entity);
//        Assert.assertEquals(result, 1);
//
//        departmentDao.deleteById(entity.getId());
//    }
//
//    @Test
//    public void update() {
//        Department department = departmentDao.getById(department_id_1);
//        department.setName("开发部");
//        department.markModify();
//        departmentDao.update(department);
//
//        Department entity = departmentDao.getBy("name = ?", "开发部");
//        Assert.assertTrue(entity != null);
//    }
//
//    @Test
//    public void updateByColumn() {
//        Department department = departmentDao.getById(department_id_2);
//        department.setName("开发部");
//        department.markModify();
//        departmentDao.update(department, "name");
//
//        Department entity = departmentDao.getBy("name = ?", "开发部");
//        Assert.assertTrue(entity != null);
//    }
//
//    @Test
//    public void batchUpdate() {
//        List<Department> departments = departmentDao.find("SELECT * FROM Department WHERE version = ?", 1);
//        for (Department department : departments) {
//            department.markModify();
//        }
//        int[] update = departmentDao.update(departments, "version, modified, code");
//        Assert.assertTrue(update.length == departments.size());
//    }
//
//    @Test
//    public void inTest() {
//        Object[] objAry = new Object[100];
//        int      i      = 0;
//        for (int j = 0; j < objAry.length; j++) {
//            objAry[j] = i++;
//        }
//
//        Sql sql = new Sql("SELECT * FROM Department").where(Sql.In("code", objAry));
//        Assert.assertTrue(sql != null);
//    }
//
//    @Test
//    public void findByEmpty(){
//        Sql sql = new Sql("SELECT * FROM Department").where().and(Sql.Eq("id", ""));
//        List<Department> departments = departmentDao.find(sql.getSql(), sql.getParams());
//        Assert.assertTrue(departments.size() == 0);
//    }
///*
//    int[] update(List<TModel> models, String columns);
//    int delete(TModel model);
//    int delete(List<TModel> models);
//    int deleteById(Object primaryKey);
//    int deleteByIds(Object... primaryKeys);
//    int deleteBy(String where, Object... params);
//    int deleteByField(String fieldName, Object... params);
//    int execute(String sql, Object... params);
//    int[] execute(List<Sql> sqls);*/
//}
