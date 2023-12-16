package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.Illness;


/**
 * 疾病控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("illness")
public class IllnessController extends BaseController<Illness> {

}
