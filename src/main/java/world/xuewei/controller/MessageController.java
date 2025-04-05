package world.xuewei.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.User;

import java.util.List;

/**
 * 消息控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController<User> {

    @Data
    public static class QueryRequest {
        private String content;
        @JsonProperty("image_urls")
        private List<String> imageUrls;
    }

    /**
     * 发送消息
     */
    @PostMapping("/query")
    public RespResult query(@RequestBody QueryRequest request) {
        if (StringUtils.isBlank(request.getContent())) {
            return RespResult.fail("请输入要发送的信息");
        }
        String result = apiService.query(request.getContent(), request.getImageUrls());
        return RespResult.success(result);
    }
}
