package world.xuewei.service;

import world.xuewei.entity.FirstAidCategory;
import world.xuewei.entity.FirstAidGuide;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface FirstAidService {
    
    /**
     * 获取所有分类
     */
    List<FirstAidCategory> listCategories();
    
    /**
     * 根据分类ID获取指南列表
     */
    List<FirstAidGuide> listGuidesByCategory(Integer categoryId);
    
    /**
     * 获取指南详情
     */
    FirstAidGuide getGuideDetail(Integer id);
    
    /**
     * 保存分类
     */
    void saveCategory(FirstAidCategory category);
    
    /**
     * 保存指南
     */
    void saveGuide(FirstAidGuide guide);
    
    /**
     * 删除分类
     */
    void deleteCategory(Integer id);
    
    /**
     * 删除指南
     */
    void deleteGuide(Integer id);
    Map<String, Object> page(Integer pageNum, Integer pageSize, Integer categoryId);
    IPage<FirstAidCategory> page(IPage<FirstAidCategory> page);
    IPage<FirstAidGuide> page(IPage<FirstAidGuide> page, QueryWrapper<FirstAidGuide> wrapper);
    /**
     * 分页查询分类
     */
    Map<String, Object> pageCategories(Integer pageNum, Integer pageSize);
    
    /**
     * 分页查询指南
     */
    Map<String, Object> pageGuides(Integer pageNum, Integer pageSize, Integer categoryId);
    
    /**
     * 分页查询分类
     */
    IPage<FirstAidCategory> pageCategory(IPage<FirstAidCategory> page);
    
    /**
     * 分页查询指南
     */
    IPage<FirstAidGuide> pageGuide(IPage<FirstAidGuide> page, QueryWrapper<FirstAidGuide> wrapper);
} 