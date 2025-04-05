package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 聊天消息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("chat_message")
public class ChatMessage {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 医生ID
     */
    private Integer doctorId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 是否来自医生的消息
     */
    private Boolean fromDoctor;

    /**
     * 消息发送时间
     */
    private Date createTime;

    /**
     * 是否已读
     */
    private Boolean isRead;
} 