package world.xuewei.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.xuewei.dao.MedicineDao;
import world.xuewei.entity.Medicine;
import world.xuewei.utils.Assert;
import world.xuewei.utils.BeanUtil;
import world.xuewei.utils.VariableNameUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品服务类
 *
 * @author XUEW
 */
@Service
public class MedicineService extends BaseService<Medicine> {

    @Autowired
    protected MedicineDao medicineDao;

    @Override
    public List<Medicine> query(Medicine o) {
        QueryWrapper<Medicine> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return medicineDao.selectList(wrapper);
    }

    @Override
    public List<Medicine> all() {
        return query(null);
    }

    @Override
    public Medicine save(Medicine o) {
        if (Assert.isEmpty(o.getId())) {
            medicineDao.insert(o);
        } else {
            medicineDao.updateById(o);
        }
        return medicineDao.selectById(o.getId());
    }

    @Override
    public Medicine get(Serializable id) {
        return medicineDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return medicineDao.deleteById(id);
    }

    public Map<String, Object> getMedicineList(String nameValue, Integer page) {

        List<Medicine> medicineList = null;
        Map<String, Object> map = new HashMap<>(4);
        if (Assert.notEmpty(nameValue)) {
            medicineList = medicineDao.selectList(new QueryWrapper<Medicine>().
                    like("medicine_name", nameValue)
                    .or().like("keyword", nameValue)
                    .or().like("medicine_effect", nameValue)
                    .last("limit " + (page - 1) * 9 + "," + page * 9));
        } else {
            medicineList = medicineDao.selectList(new QueryWrapper<Medicine>()
                    .last("limit " + (page - 1) * 9 + "," + page * 9));
        }

        map.put("medicineList", medicineList);
        map.put("size", medicineList.size() < 9 ? 1 : medicineList.size() / 9 + 1);
        return map;
    }
}