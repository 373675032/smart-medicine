package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import world.xuewei.entity.DoctorRegistration;
import world.xuewei.dao.DoctorRegistrationDao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 医生挂号服务
 * @author xuewei
 */
@Service
@EnableScheduling
public class DoctorRegistrationService extends ServiceImpl<DoctorRegistrationDao, DoctorRegistration> {
    
    private static final Logger log = LoggerFactory.getLogger(DoctorRegistrationService.class);
    
    /**
     * 检查用户是否已挂号医生，且挂号在24小时有效期内
     */
    public boolean isRegistered(Integer userId, Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("doctor_id", doctorId);
        
        // 查询挂号记录
        DoctorRegistration registration = this.getOne(queryWrapper);
        
        if (registration == null) {
            return false; // 没有挂号记录
        }
        
        // 检查挂号是否在24小时内
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24); // 24小时前
        Date twentyFourHoursAgo = calendar.getTime();
        
        // 如果挂号时间早于24小时前，则挂号已过期
        return registration.getCreateTime().after(twentyFourHoursAgo);
    }
    
    /**
     * 获取用户的有效挂号记录（24小时内）
     */
    public List<DoctorRegistration> getUserRegistrations(Integer userId) {
        // 计算24小时前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        Date twentyFourHoursAgo = calendar.getTime();
        
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .gt("create_time", twentyFourHoursAgo) // 只查询24小时内的挂号
                    .orderByDesc("create_time");
                
        return this.list(queryWrapper);
    }

    /**
     * 获取医生的有效挂号记录（24小时内）
     */
    public List<DoctorRegistration> getDoctorRegistrations(Integer doctorId) {
        // 计算24小时前的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        Date twentyFourHoursAgo = calendar.getTime();
        
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", doctorId)
                    .gt("create_time", twentyFourHoursAgo) // 只查询24小时内的挂号
                    .orderByDesc("create_time");
                
        return this.list(queryWrapper);
    }

    /**
     * 清理过期挂号记录
     * 可以使用Spring的@Scheduled注解定期执行
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点执行
    public void cleanExpiredRegistrations() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        Date twentyFourHoursAgo = calendar.getTime();
        
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("create_time", twentyFourHoursAgo);
        
        // 可以选择删除过期记录或标记为过期
        this.remove(queryWrapper);
        
        log.info("已清理过期挂号记录");
    }
} 