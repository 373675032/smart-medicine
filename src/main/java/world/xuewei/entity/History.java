package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 历史实体
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("history")
public class History {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 浏览历史关联用户id
     */
    private Integer userId;

    /**
     * 浏览历史类型
     */
    private Integer operateType;

    /**
     * 浏览历史关键字
     */
    private String keyword;

    /**
     * 浏览时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
