package tam.summer.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tam.summer.example.business.inter.IDepartmentService;
import tam.summer.example.domain.entity.Department;
import tam.summer.restdoc.annotation.ClassComment;
import tam.summer.restdoc.annotation.MethodComment;
import tam.summer.restdoc.annotation.ModuleComment;
import tam.summer.restdoc.annotation.ParamComment;

/**
 * Created by tanqimin on 2015/10/28.
 */
@RestController
@RequestMapping("/api/position")
@ModuleComment(value = "基础档案", sortIndex = 1)
@ClassComment(value = "职位档案", description = "职位档案API")
public class PositionController {

    @Autowired
    private IDepartmentService departmentService;

/*    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "获取职位列表", description = "获取所有职位档案的列表", isAuth = true)
    public ResponseEntity<List<Department>> get(
            @PathVariable @ParamComment(value = "id", description = "ID") String id,
            @ParamComment(value = "name", description = "名称") HttpEntity<String> name,
            @ParamComment(value = "departments", description = "名称") HttpEntity<Department[]> departments) {
        return new ResponseEntity<>(departmentService.findAllDepartment(), HttpStatus.OK);
    }*/

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @MethodComment(value = "保存职位", description = "保存职位档案的列表", isAuth = true)
    public ResponseEntity<Integer> save(
            @PathVariable @ParamComment(value = "id", description = "ID") String id,
            @ParamComment(value = "name", description = "名称") HttpEntity<String> name,
            @ParamComment(value = "departments", description = "名称") HttpEntity<Department[]> departments) {
        return new ResponseEntity<Integer>(1, HttpStatus.OK);
    }
}
