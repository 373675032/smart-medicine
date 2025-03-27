package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.xuewei.dao.MedicalNewsDao;
import world.xuewei.entity.MedicalNews;
import world.xuewei.utils.Assert;
import world.xuewei.utils.BeanUtil;
import world.xuewei.utils.VariableNameUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 咨询服务类
 *
 * @author XUEW
 */
@Service
public class MedicalNewsService extends BaseService<MedicalNews> {

    @Autowired
    protected MedicalNewsDao medicalNewsDao;

    @Override
    public List<MedicalNews> query(MedicalNews o) {
        QueryWrapper<MedicalNews> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return medicalNewsDao.selectList(wrapper);
    }

    @Override
    public List<MedicalNews> all() {
        return query(null);
    }

    @Override
    public MedicalNews save(MedicalNews o) {
        if (Assert.isEmpty(o.getId())) {
            medicalNewsDao.insert(o);
        } else {
            medicalNewsDao.updateById(o);
        }
        return medicalNewsDao.selectById(o.getId());
    }

    @Override
    public MedicalNews get(Serializable id) {
        return medicalNewsDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return medicalNewsDao.deleteById(id);
    }

    /**
     * 根据ID获取新闻详情
     */
    public MedicalNews getById(Long id) {
        if (id == null) {
            return null;
        }
        return medicalNewsDao.selectById(id);
    }

    /**
     * 获取所有新闻
     */
    public List<MedicalNews> allNews() {
        return medicalNewsDao.selectList(null);
    }

    /**
     * 保存新闻
     */
    public void saveNews(MedicalNews news) {
        if (Assert.isEmpty(news.getId())) {
            medicalNewsDao.insert(news);
        } else {
            medicalNewsDao.updateById(news);
        }
    }

    /**
     * 删除新闻
     */
    public void deleteNews(Long id) {
        if (id != null) {
            medicalNewsDao.deleteById(id);
        }
    }

    public Map<String, Object> page(Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>(4);
        
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 获取分页数据
        List<MedicalNews> newsList = medicalNewsDao.selectList(
            new QueryWrapper<MedicalNews>()
                .orderByDesc("create_time")
                .last("limit " + offset + "," + size)
        );
        
        // 获取总记录数
        Integer total = medicalNewsDao.selectCount(null);
        
        // 计算总页数
        int pages = (total + size - 1) / size;
        
        map.put("newsList", newsList);
        map.put("current", page);
        map.put("pages", pages);
        map.put("total", total);
        
        return map;
    }

    /**
     * 根据条件查询列表
     */
    public List<MedicalNews> list(QueryWrapper<MedicalNews> queryWrapper) {
        return medicalNewsDao.selectList(queryWrapper);
    }

    /**
     * 获取上一篇文章
     */
    public MedicalNews getPrevious(Long id) {
        return medicalNewsDao.selectOne(
            new QueryWrapper<MedicalNews>()
                .lt("id", id)
                .orderByDesc("id")
                .last("limit 1")
        );
    }

    /**
     * 获取下一篇文章
     */
    public MedicalNews getNext(Long id) {
        return medicalNewsDao.selectOne(
            new QueryWrapper<MedicalNews>()
                .gt("id", id)
                .orderByAsc("id")
                .last("limit 1")
        );
    }

    /**
     * 分页查询健康科普
     */
    public IPage<MedicalNews> getPage(Integer pageNum, Integer pageSize) {
        Page<MedicalNews> page = new Page<>(pageNum, pageSize);
        QueryWrapper<MedicalNews> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return medicalNewsDao.selectPage(page, wrapper);
    }
}