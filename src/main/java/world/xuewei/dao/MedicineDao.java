package world.xuewei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import world.xuewei.entity.Medicine;

import java.util.List;
import java.util.Map;

/**
 * 药品数据库访问
 *
 * @author XUEW
 */
@Repository
public interface MedicineDao extends BaseMapper<Medicine> {

    /**
     * 根据疾病查询药物
     */
    List<Map<String, Object>> findMedicineList(Integer illnessId);

}
