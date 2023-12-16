package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.MedicalNews;

/**
 * 咨询数据库访问
 *
 * @author XUEW
 */
@Repository
public interface MedicalNewsDao extends BaseMapper<MedicalNews> {

}
