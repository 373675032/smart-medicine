package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.Pageview;

/**
 * 分页数据库访问
 *
 * @author XUEW
 */
@Repository
public interface PageviewDao extends BaseMapper<Pageview> {

}
