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
 * 疾病药品实体
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("illness_medicine")
public class IllnessMedicine {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 疾病id
     */
    private Integer illnessId;

    /**
     * 药物id
     */
    private Integer medicineId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
