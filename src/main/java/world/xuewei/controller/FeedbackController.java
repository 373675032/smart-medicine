package world.xuewei.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.entity.Feedback;

/**
 * 反馈控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping(value = "feedback")
public class FeedbackController extends BaseController<Feedback> {

}
