package world.xuewei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import world.xuewei.dao.FirstAidCategoryMapper;
import world.xuewei.dao.FirstAidGuideMapper;
import world.xuewei.entity.FirstAidCategory;
import world.xuewei.entity.FirstAidGuide;
import world.xuewei.service.FirstAidService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirstAidServiceImpl implements FirstAidService {

    @Resource
    private FirstAidCategoryMapper categoryMapper;
    
    @Resource
    private FirstAidGuideMapper guideMapper;

    @Override
    public List<FirstAidCategory> listCategories() {
        return categoryMapper.selectList(
            new QueryWrapper<FirstAidCategory>()
                .orderByAsc("sort")
        );
    }

    @Override
    public List<FirstAidGuide> listGuidesByCategory(Integer categoryId) {
        return guideMapper.selectList(
            new QueryWrapper<FirstAidGuide>()
                .eq("category_id", categoryId)
                .orderByAsc("sort")
        );
    }

    @Override
    public FirstAidGuide getGuideDetail(Integer id) {
        return guideMapper.selectById(id);
    }

    @Override
    public void saveCategory(FirstAidCategory category) {
        if (category.getId() == null) {
            categoryMapper.insert(category);
        } else {
            categoryMapper.updateById(category);
        }
    }

    @Override
    public void saveGuide(FirstAidGuide guide) {
        if (guide.getId() == null) {
            guide.setCreateTime(new Date());
            guideMapper.insert(guide);
        } else {
            guide.setUpdateTime(new Date());
            guideMapper.updateById(guide);
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.deleteById(id);
    }

    @Override
    public void deleteGuide(Integer id) {
        guideMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> page(Integer pageNum, Integer pageSize, Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        
        // 创建分页对象
        Page<FirstAidGuide> page = new Page<>(pageNum, pageSize);
        
        // 查询条件
        QueryWrapper<FirstAidGuide> wrapper = new QueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        wrapper.orderByAsc("sort");
        
        // 执行分页查询
        IPage<FirstAidGuide> iPage = guideMapper.selectPage(page, wrapper);
        
        // 获取所有分类
        List<FirstAidCategory> categories = categoryMapper.selectList(null);
        Map<Integer, String> categoryMap = new HashMap<>();
        for (FirstAidCategory category : categories) {
            categoryMap.put(category.getId(), category.getName());
        }
        
        // 设置分类名称
        List<FirstAidGuide> records = iPage.getRecords();
        for (FirstAidGuide guide : records) {
            guide.setCategoryName(categoryMap.get(guide.getCategoryId()));
        }
        
        // 封装结果
        result.put("list", records);
        result.put("total", iPage.getTotal());
        result.put("pages", iPage.getPages());
        result.put("current", iPage.getCurrent());
        
        return result;
    }

    @Override
    public Map<String, Object> pageCategories(Integer pageNum, Integer pageSize) {
        Page<FirstAidCategory> page = new Page<>(pageNum, pageSize);
        IPage<FirstAidCategory> iPage = categoryMapper.selectPage(page, 
            new QueryWrapper<FirstAidCategory>().orderByAsc("sort"));
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", iPage.getRecords());
        result.put("total", iPage.getTotal());
        result.put("pages", (int)Math.ceil(iPage.getTotal() * 1.0 / pageSize));
        result.put("current", pageNum);
        return result;
    }

    @Override
    public Map<String, Object> pageGuides(Integer pageNum, Integer pageSize, Integer categoryId) {
        Page<FirstAidGuide> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FirstAidGuide> wrapper = new QueryWrapper<FirstAidGuide>()
            .orderByAsc("sort");
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        IPage<FirstAidGuide> iPage = guideMapper.selectPage(page, wrapper);
        
        // 填充分类名称
        List<FirstAidGuide> records = iPage.getRecords();
        for (FirstAidGuide guide : records) {
            FirstAidCategory category = categoryMapper.selectById(guide.getCategoryId());
            if (category != null) {
                guide.setCategoryName(category.getName());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", records);
        result.put("total", iPage.getTotal());
        result.put("pages", (int)Math.ceil(iPage.getTotal() * 1.0 / pageSize));
        result.put("current", pageNum);
        return result;
    }

    @Override
    public IPage<FirstAidCategory> page(IPage<FirstAidCategory> page) {
        return categoryMapper.selectPage(page, new QueryWrapper<FirstAidCategory>().orderByAsc("sort"));
    }

    @Override
    public IPage<FirstAidGuide> page(IPage<FirstAidGuide> page, QueryWrapper<FirstAidGuide> wrapper) {
        IPage<FirstAidGuide> iPage = guideMapper.selectPage(page, wrapper);
        // 填充分类名称
        List<FirstAidGuide> records = iPage.getRecords();
        for (FirstAidGuide guide : records) {
            FirstAidCategory category = categoryMapper.selectById(guide.getCategoryId());
            if (category != null) {
                guide.setCategoryName(category.getName());
            }
        }
        return iPage;
    }

    @Override
    public IPage<FirstAidCategory> pageCategory(IPage<FirstAidCategory> page) {
        return categoryMapper.selectPage(page, new QueryWrapper<FirstAidCategory>().orderByAsc("sort"));
    }

    @Override
    public IPage<FirstAidGuide> pageGuide(IPage<FirstAidGuide> page, QueryWrapper<FirstAidGuide> wrapper) {
        IPage<FirstAidGuide> guidePage = guideMapper.selectPage(page, wrapper);
        // 填充分类名称
        List<FirstAidGuide> records = guidePage.getRecords();
        for (FirstAidGuide guide : records) {
            FirstAidCategory category = categoryMapper.selectById(guide.getCategoryId());
            if (category != null) {
                guide.setCategoryName(category.getName());
            }
        }
        return guidePage;
    }
} 