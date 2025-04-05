package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import world.xuewei.dao.ChatMessageDao;
import world.xuewei.entity.ChatMessage;

import java.util.List;

/**
 * 聊天消息服务
 */
@Service
public class ChatMessageService extends ServiceImpl<ChatMessageDao, ChatMessage> {
    
    /**
     * 获取用户与医生之间的所有消息
     */
    public List<ChatMessage> getUserDoctorMessages(Integer userId, Integer doctorId) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("doctor_id", doctorId)
                    .orderByAsc("create_time");
        return list(queryWrapper);
    }
    
    /**
     * 获取新消息
     */
    public List<ChatMessage> getNewMessages(Integer userId, Integer doctorId, Integer lastMessageId) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("doctor_id", doctorId)
                    .gt("id", lastMessageId)
                    .orderByAsc("create_time");
        return list(queryWrapper);
    }

    /**
     * 获取未读消息数
     */
    public int countUnreadMessages(Integer userId, Integer doctorId) {
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("doctor_id", doctorId)
                    .eq("is_read", false)
                    .eq("from_doctor", false); // 只计算患者发送给医生的未读消息
        return count(queryWrapper);
    }

    /**
     * 将消息标记为已读
     */
    public void markMessagesAsRead(Integer userId, Integer doctorId) {
        UpdateWrapper<ChatMessage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                    .eq("doctor_id", doctorId)
                    .eq("is_read", false)
                    .eq("from_doctor", false)
                    .set("is_read", true);
        update(updateWrapper);
    }

    /**
     * 获取用户与医生之间的所有消息
     */
    public List<ChatMessage> getAllMessages(Integer patientId, Integer doctorId) {
        // 创建查询条件
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        
        // 在聊天消息表中，消息使用 userId(患者ID) 和 doctorId(医生ID) 来标识
        // 所有消息必须是指定的患者和医生之间的
        queryWrapper.eq("user_id", patientId)
                    .eq("doctor_id", doctorId)
                    .orderByAsc("create_time");
        
        // 执行查询并返回结果
        return this.list(queryWrapper);
    }
}