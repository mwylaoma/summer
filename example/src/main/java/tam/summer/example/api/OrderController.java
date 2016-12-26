package tam.summer.example.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tam.summer.restdoc.annotation.ClassComment;
import tam.summer.restdoc.annotation.ModuleComment;

/**
 * Created by tanqimin on 2015/10/28.
 */
@RestController
@RequestMapping("/api/order")
@ModuleComment(value = "业务管理", sortIndex = 2)
@ClassComment(value = "单据档案", description = "单据档案API")
public class OrderController {
}
