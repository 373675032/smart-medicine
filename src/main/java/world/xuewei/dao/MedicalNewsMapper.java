package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import world.xuewei.entity.MedicalNews;

/**
 * 健康科普数据访问层
 */
@Mapper
public interface MedicalNewsMapper extends BaseMapper<MedicalNews> {
    // BaseMapper 已经提供了基础的 CRUD 方法，这里不需要额外定义方法
} 