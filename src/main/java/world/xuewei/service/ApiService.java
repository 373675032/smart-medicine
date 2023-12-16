package world.xuewei.service;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 智慧医生服务
 * <p>
 * ==========================================================================
 * 郑重说明：本项目免费开源！原创作者为：薛伟同学，严禁私自出售。
 * ==========================================================================
 * B站账号：薛伟同学
 * 微信公众号：薛伟同学
 * 作者博客：http://xuewei.world
 * ==========================================================================
 * 陆陆续续总会收到粉丝的提醒，总会有些人为了赚取利益倒卖我的开源项目。
 * 不乏有粉丝朋友出现钱付过去，那边只把代码发给他就跑路的，最后还是根据线索找到我。。
 * 希望各位朋友擦亮慧眼，谨防上当受骗！
 * ==========================================================================
 *
 * @author <a href="http://xuewei.world/about">XUEW</a>
 */
@Service
public class ApiService {

    @Value("${ai-key}")
    private String apiKey;

    public String query(String queryMessage) {
        Constants.apiKey = apiKey;
        try {
            Generation gen = new Generation();
            MessageManager msgManager = new MessageManager(10);
            Message systemMsg = Message.builder().role(Role.SYSTEM.getValue()).content("你是薛伟同学开发的智能医生，你只回答与医疗相关的问题，不要回答其他问题！").build();
            Message userMsg = Message.builder().role(Role.USER.getValue()).content(queryMessage).build();
            msgManager.add(systemMsg);
            msgManager.add(userMsg);
            QwenParam param = QwenParam.builder().model(Generation.Models.QWEN_TURBO).messages(msgManager.get()).resultFormat(QwenParam.ResultFormat.MESSAGE).build();
            GenerationResult result = gen.call(param);
            GenerationOutput output = result.getOutput();
            Message message = output.getChoices().get(0).getMessage();
            return message.getContent();
        } catch (Exception e) {
            return "智能医生现在不在线，请稍后再试～";
        }
    }
}
