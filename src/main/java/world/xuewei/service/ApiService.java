package world.xuewei.service;

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.sun.xml.internal.ws.resources.ModelerMessages;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author <a href="http://xuewei.world/about">XUEW</a>
 */
@Service
@Slf4j
public class ApiService {

    @Value("${ai-key}")
    private String apiKey;

    private static StringBuilder finalContent = new StringBuilder();
    
    private static final String SYSTEM_PROMPT = "你是智能医生，你只回答与医疗相关的问题，不要回答其他问题！";

    public String query(String message, List<String> urls) {
        try {
            log.info("消息内容:{}", message);
            MultiModalConversation conv = new MultiModalConversation();
            List<MultiModalMessage> messages = new ArrayList<>();
            
            // 构建消息内容
            List<Map<String, Object>> content = new ArrayList<>();
            
            // 添加图片（如果有）
            if (urls != null && !urls.isEmpty()) {
                for (String url : urls) {
                    content.add(Collections.singletonMap("image", url));
                }
            }
            
            // 将系统提示和用户消息合并
            String combinedMessage = SYSTEM_PROMPT + "\n\n用户问题：" + message;
            content.add(Collections.singletonMap("text", combinedMessage));
            
            // 构建用户消息
            MultiModalMessage userMsg = MultiModalMessage.builder()
                    .role(Role.USER.getValue())
                    .content(content)
                    .build();
            messages.add(userMsg);
            
            // 重置StringBuilder
            finalContent.setLength(0);
            
            // 调用API
            MultiModalConversationParam param = MultiModalConversationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-vl-plus")
                    .messages(messages)
                    .incrementalOutput(true)
                    .build();
                    
            Flowable<MultiModalConversationResult> result = conv.streamCall(param);
            result.blockingForEach(this::handleGenerationResult);
            
            return finalContent.toString();
            
        } catch (Exception e) {
            log.error("Query failed: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
    
    private void handleGenerationResult(MultiModalConversationResult message) {
        List<Map<String, Object>> content = message.getOutput().getChoices().get(0).getMessage().getContent();
        if (Objects.nonNull(content) && !content.isEmpty()) {
            Object text = content.get(0).get("text");
            finalContent.append(text);
        }
    }
}
