package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.IllnessKind;

/**
 * 疾病分类控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("illness_kind")
public class IllnessKindController extends BaseController<IllnessKind> {

}
