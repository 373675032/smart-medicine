package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.History;

/**
 * 历史数据库访问
 *
 * @author XUEW
 */
@Repository
public interface HistoryDao extends BaseMapper<History> {

}
