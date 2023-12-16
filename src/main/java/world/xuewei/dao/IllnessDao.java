package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.Illness;

/**
 * 疾病数据库访问
 *
 * @author XUEW
 */
@Repository
public interface IllnessDao extends BaseMapper<Illness> {

}
