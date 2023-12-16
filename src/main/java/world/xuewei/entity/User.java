package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
     * 图片的地址
     */
    private String imgPath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
