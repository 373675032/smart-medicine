package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.xuewei.dao.IllnessKindDao;
import world.xuewei.entity.IllnessKind;
import world.xuewei.utils.Assert;
import world.xuewei.utils.BeanUtil;
import world.xuewei.utils.VariableNameUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 疾病分类服务类
 *
 * @author XUEW
 */
@Service
public class IllnessKindService extends BaseService<IllnessKind> {

    @Autowired
    protected IllnessKindDao illnessKindDao;

    @Override
    public List<IllnessKind> query(IllnessKind o) {
        QueryWrapper<IllnessKind> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return illnessKindDao.selectList(wrapper);
    }

    @Override
    public List<IllnessKind> all() {
        return query(null);
    }

    @Override
    public IllnessKind save(IllnessKind o) {
        if (Assert.isEmpty(o.getId())) {
            illnessKindDao.insert(o);
        } else {
            illnessKindDao.updateById(o);
        }
        return illnessKindDao.selectById(o.getId());
    }

    @Override
    public IllnessKind get(Serializable id) {
        return illnessKindDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return illnessKindDao.deleteById(id);
    }

    public List<IllnessKind> findList() {
        return illnessKindDao.selectList(new QueryWrapper<IllnessKind>());
    }
}