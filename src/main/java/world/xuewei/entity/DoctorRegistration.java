package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 医生挂号记录
 * @author xuewei
 */
@Data
@TableName("doctor_registration")
public class DoctorRegistration {
    
    @TableId(type = IdType.AUTO)
    private Integer id;
    
    /**
     * 患者用户ID
     */
    private Integer userId;
    
    /**
     * 医生用户ID
     */
    private Integer doctorId;
    
    /**
     * 挂号时间
     */
    private Date createTime = new Date();
} 