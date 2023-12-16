package world.xuewei.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.User;

/**
 * 消息控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController<User> {

    /**
     * 发送消息
     */
    @PostMapping("/query")
    public RespResult query(String content) {
        String result = apiService.query(content);
        return RespResult.success(result);
    }
}
