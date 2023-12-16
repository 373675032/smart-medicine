package world.xuewei.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import world.xuewei.dao.HistoryDao;
import world.xuewei.entity.History;
import world.xuewei.entity.IllnessKind;
import world.xuewei.utils.Assert;
import world.xuewei.utils.BeanUtil;
import world.xuewei.utils.VariableNameUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 历史服务类
 *
 * @author XUEW
 */
@Service
public class HistoryService extends BaseService<History> {

    @Autowired
    protected HistoryDao historyDao;

    @Override
    public List<History> query(History o) {
        QueryWrapper<History> wrapper = new QueryWrapper();
        if (Assert.notEmpty(o)) {
            Map<String, Object> bean2Map = BeanUtil.bean2Map(o);
            for (String key : bean2Map.keySet()) {
                if (Assert.isEmpty(bean2Map.get(key))) {
                    continue;
                }
                wrapper.eq(VariableNameUtils.humpToLine(key), bean2Map.get(key));
            }
        }
        return historyDao.selectList(wrapper);
    }

    @Override
    public List<History> all() {
        return query(null);
    }

    @Override
    public History save(History o) {
        if (Assert.isEmpty(o.getId())) {
            historyDao.insert(o);
        } else {
            historyDao.updateById(o);
        }
        return historyDao.selectById(o.getId());
    }

    @Override
    public History get(Serializable id) {
        return historyDao.selectById(id);
    }

    @Override
    public int delete(Serializable id) {
        return historyDao.deleteById(id);
    }

    public boolean insetOne(Integer uid, Integer type, String nameValue) {
        History history = new History();
        history.setUserId(uid).setKeyword(nameValue).setOperateType(type);
        return historyDao.insert(history) > 0;
    }

    public List<Map<String, Object>> findList(Integer userId) {
        List<History> list = historyDao.selectList(new QueryWrapper<History>().eq("user_id", userId)
                .orderByDesc("create_time"));
        List<History> histories = list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(History::getKeyword))), LinkedList::new));
        histories.sort((h1, h2) -> -h1.getCreateTime().compareTo(h2.getCreateTime()));
        List<History> historyList = histories.stream().limit(10).collect(Collectors.toList());
        System.out.println(histories.size());
        List<Map<String, Object>> mapList = new LinkedList<>();
        historyList.forEach(his -> {
            Map<String, Object> map = cn.hutool.core.bean.BeanUtil.beanToMap(his);
            Integer operateType = MapUtil.getInt(map, "operateType");
            if (operateType == 1) {
                List<String> keyword = Arrays.asList((MapUtil.getStr(map, "keyword")).split(","));
                IllnessKind illnessKind = illnessKindDao.selectById(Integer.valueOf(keyword.get(0)));
                map.put("kind", illnessKind.getId());
                map.put("nameValue", keyword.get(1));
                map.put("searchValue", illnessKind.getName() + ("无".equals(keyword.get(1)) ? "" : ("|" + keyword.get(1))));
            } else if (operateType == 2) {
                map.put("nameValue", MapUtil.getStr(map, "keyword"));
                map.put("kind", "无");
                map.put("searchValue", MapUtil.getStr(map, "keyword"));
            } else if (operateType == 3) {
                map.put("nameValue", MapUtil.getStr(map, "keyword"));
                map.put("searchValue", MapUtil.getStr(map, "keyword"));
                map.put("kind", "无");
            }
            mapList.add(map);
        });
        return mapList;
    }
}