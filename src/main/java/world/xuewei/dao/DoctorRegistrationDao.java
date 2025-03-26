package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import world.xuewei.entity.DoctorRegistration;

/**
 * 医生挂号Mapper
 * @author xuewei
 */
@Mapper
public interface DoctorRegistrationDao extends BaseMapper<DoctorRegistration> {
} 