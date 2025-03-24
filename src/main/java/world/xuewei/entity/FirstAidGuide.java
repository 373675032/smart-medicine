package world.xuewei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("first_aid_guide")
public class FirstAidGuide {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer categoryId;
    private String title;
    private String content;
    private String imgPath;
    private Integer sort;
    private Date createTime;
    private Date updateTime;
    @TableField(exist = false)  // 标记为非数据库字段
private String categoryName;  // 分类名称

}