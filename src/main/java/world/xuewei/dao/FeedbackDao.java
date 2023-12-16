package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.Feedback;

/**
 * 反馈数据库访问
 *
 * @author XUEW
 */
@Repository
public interface FeedbackDao extends BaseMapper<Feedback> {

}
