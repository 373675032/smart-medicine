package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import world.xuewei.dao.DoctorRegistrationDao;
import world.xuewei.entity.DoctorRegistration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                    .eq("doctor_id", doctorId)
                    .eq("status", 0); // 只检查进行中的问诊
        
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
                    .eq("status", 0) // 只查询进行中的问诊
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
                    .eq("status", 0) // 只查询进行中的问诊
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
        queryWrapper.lt("create_time", twentyFourHoursAgo)
                    .eq("status", 0); // 只处理进行中的问诊
        
        // 将过期的问诊标记为已结束
        List<DoctorRegistration> expiredRegistrations = this.list(queryWrapper);
        
        if (!expiredRegistrations.isEmpty()) {
            for (DoctorRegistration reg : expiredRegistrations) {
                reg.setStatus(1); // 标记为已结束
                this.updateById(reg);
                log.info("已自动结束超时问诊: 医生ID={}, 患者ID={}", reg.getDoctorId(), reg.getUserId());
            }
        }
        
        log.info("已清理过期挂号记录");
    }
    
    /**
     * 医生主动结束问诊，标记为已结束状态
     */
    public boolean removeRegistration(Integer patientId, Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", patientId)
                    .eq("doctor_id", doctorId)
                    .eq("status", 0); // 只结束进行中的问诊
        
        DoctorRegistration registration = this.getOne(queryWrapper);
        
        if (registration != null) {
            // 更新状态为已结束
            registration.setStatus(1);
            boolean updated = this.updateById(registration);
            
            if (updated) {
                log.info("医生 {} 已结束与患者 {} 的问诊", doctorId, patientId);
            }
            
            return updated;
        }
        
        return false;
    }
    
    /**
     * 获取用户的所有问诊记录（包括已结束的）
     */
    public List<DoctorRegistration> getAllUserRegistrations(Integer userId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .orderByDesc("create_time");
                
        return this.list(queryWrapper);
    }
    
    /**
     * 获取医生的所有问诊记录（包括已结束的）
     */
    public List<DoctorRegistration> getAllDoctorRegistrations(Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", doctorId)
                    .orderByDesc("create_time");
                
        return this.list(queryWrapper);
    }
    
    /**
     * 获取用户的所有问诊记录（包括已结束的），活跃的排在前面
     */
    public List<DoctorRegistration> getAllUserRegistrationsPrioritizeActive(Integer userId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .orderByAsc("status")  // 状态0（活跃）排在前面
                    .orderByDesc("create_time");  // 然后按创建时间降序
                
        return this.list(queryWrapper);
    }
    
    /**
     * 获取医生的所有问诊记录（包括已结束的），活跃的排在前面
     */
    public List<DoctorRegistration> getAllDoctorRegistrationsPrioritizeActive(Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("doctor_id", doctorId)
                    .orderByAsc("status")  // 状态0（活跃）排在前面
                    .orderByDesc("create_time");  // 然后按创建时间降序
                
        return this.list(queryWrapper);
    }
    
    /**
     * 获取挂号记录（不考虑状态和时间）
     */
    public DoctorRegistration getRegistrationRecord(Integer userId, Integer doctorId) {
        QueryWrapper<DoctorRegistration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("doctor_id", doctorId);
        
        return this.getOne(queryWrapper);
    }
    
    /**
     * 重新激活或创建问诊记录
     * 如果记录存在，则更新状态为进行中，并更新创建时间
     * 如果记录不存在，则创建新记录
     */
    public boolean reactivateOrCreateRegistration(Integer userId, Integer doctorId) {
        // 查询是否存在记录
        DoctorRegistration registration = getRegistrationRecord(userId, doctorId);
        
        if (registration != null) {
            // 存在记录，更新状态和创建时间
            registration.setStatus(0); // 设置为进行中
            registration.setCreateTime(new Date()); // 更新创建时间为当前时间
            return this.updateById(registration);
        } else {
            // 不存在记录，创建新记录
            DoctorRegistration newRegistration = new DoctorRegistration();
            newRegistration.setUserId(userId);
            newRegistration.setDoctorId(doctorId);
            newRegistration.setStatus(0);
            newRegistration.setCreateTime(new Date());
            return this.save(newRegistration);
        }
    }
} 