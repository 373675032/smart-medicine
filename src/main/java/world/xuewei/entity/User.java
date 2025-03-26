package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户实体
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户真实名字
     */
    private String userName;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户年龄
     */
    private Integer userAge;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户电话
     */
    private String userTel;

    /**
     * 角色状态，1代表管理员，0普通用户
     */
    private Integer roleStatus;

    /**
     * 用户类型，0普通用户，1全科医生
     */
    private Integer userType;

    /**
     * 图片的地址
     */
    private String imgPath;

    /**
     * 医生资质证明图片路径，多个图片以逗号分隔
     */
    private String doctorQualificationImages;

    /**
     * 审核状态：0-待审核，1-已通过，2-已拒绝
     */
    private Integer auditStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否已挂号
     */
    @TableField(exist = false)
    private Boolean isRegistered = false;

}
