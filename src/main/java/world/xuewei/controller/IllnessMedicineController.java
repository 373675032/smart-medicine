package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.IllnessMedicine;


/**
 * 疾病药品控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("illness_medicine")
public class IllnessMedicineController extends BaseController<IllnessMedicine> {

}
