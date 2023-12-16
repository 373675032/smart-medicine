package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.History;


/**
 * 历史控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("history")
public class HistoryController extends BaseController<History> {

}
