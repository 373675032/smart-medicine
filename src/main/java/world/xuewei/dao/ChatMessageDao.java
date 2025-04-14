package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import world.xuewei.entity.ChatMessage;

@Mapper
public interface ChatMessageDao extends BaseMapper<ChatMessage> {
} 