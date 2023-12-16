package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.User;

/**
 * 用户数据库访问
 *
 * @author XUEW
 */
@Repository
public interface UserDao extends BaseMapper<User> {

}
