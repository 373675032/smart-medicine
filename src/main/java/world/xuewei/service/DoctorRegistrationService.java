package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import world.xuewei.entity.DoctorRegistration;
import world.xuewei.dao.DoctorRegistrationDao;

import java.util.List;

/**
 * 医生挂号服务
 * @author xuewei
 */
@Service
public class DoctorRegistrationService extends ServiceImpl<DoctorRegistrationDao, DoctorRegistration> {
    
    /**
     * 检查用户是否已经挂号医生
     */
    public boolean isRegistered(Integer userId, Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("doctor_id", doctorId);
        return count(queryWrapper) > 0;
    }
    
    /**
     * 获取用户的所有挂号记录
     */
    public List<DoctorRegistration> getUserRegistrations(Integer userId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return list(queryWrapper);
    }
} 