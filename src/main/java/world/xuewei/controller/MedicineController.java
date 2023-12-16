package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.Medicine;


/**
 * 药品控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("medicine")
public class MedicineController extends BaseController<Medicine> {

}
