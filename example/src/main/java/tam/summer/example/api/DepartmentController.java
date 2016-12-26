package tam.summer.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tam.summer.database.meta.Page;
import tam.summer.example.business.inter.IDepartmentService;
import tam.summer.example.domain.entity.Department;
import tam.summer.restdoc.annotation.ClassComment;
import tam.summer.restdoc.annotation.MethodComment;
import tam.summer.restdoc.annotation.ModuleComment;
import tam.summer.restdoc.annotation.ParamComment;

import java.util.List;

/**
 * Created by tanqimin on 2015/10/28.
 */
@RestController
@RequestMapping("/api/department")
@ModuleComment(value = "基础档案", sortIndex = 1)
@ClassComment(value = "部门档案", description = "部门档案API")
public class DepartmentController {

/*    static {
        JSON.DEFFAULT_DATE_FORMAT = DateTimeFormat.DATE_TIME;
    }*/

    @Autowired
    private IDepartmentService departmentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "获取部门列表", description = "获取所有部门档案的列表", isAuth = true)
    public ResponseEntity<Page<Department>> getAll() throws Exception {
        return new ResponseEntity<>(departmentService.findAllDepartment(), HttpStatus.OK);
    }

    @RequestMapping(value = "/listDepartmentName", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "获取部门列表", description = "获取所有部门档案的列表", isAuth = true)
    public ResponseEntity<Page<String>> getAllDepartment() {
        return new ResponseEntity<>(departmentService.findAllDepartmentName(), HttpStatus.OK);
    }


    @RequestMapping(value = "/getIn", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "获取部门列表", description = "获取所有部门档案的列表", isAuth = true)
    public ResponseEntity<List<Department>> getIn() {
        return new ResponseEntity<>(departmentService.findByInClause(), HttpStatus.OK);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "获取部门列表", description = "获取所有部门档案的列表", isAuth = true)
    public ResponseEntity<Department> get(@PathVariable @ParamComment(value = "id", description = "ID") String id) {
        return new ResponseEntity<Department>(departmentService.findDepartmentById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "新增部门", description = "新增部门", isAuth = true)
    public ResponseEntity<Integer> save(@ParamComment(value = "departments", description = "名称") HttpEntity<Department[]> request) {
        Department[] departments = request.getBody();
        return new ResponseEntity<Integer>(departmentService.save(departments), HttpStatus.OK);
    }
}
