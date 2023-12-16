package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 疾病分类实体
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("illness_kind")
public class IllnessKind {

    /**
     * 主键ID
     */
    private int id;

    /**
     * 疾病种类名字
     */
    private String name;

    /**
     * 疾病描述
     */
    private String info;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
