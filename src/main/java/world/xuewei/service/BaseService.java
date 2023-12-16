package world.xuewei.service;

import org.springframework.beans.factory.annotation.Autowired;
import world.xuewei.dao.*;

/**
 * 基础服务类
 *
 * @author XUEW
 */
public abstract class BaseService<T> implements IService<T> {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected HistoryDao historyDao;

    @Autowired
    protected IllnessDao illnessDao;

    @Autowired
    protected IllnessKindDao illnessKindDao;

    @Autowired
    protected IllnessMedicineDao illnessMedicineDao;

    @Autowired
    protected MedicineDao medicineDao;

    @Autowired
    protected MedicalNewsDao medicalNewsDao;

    @Autowired
    protected PageviewDao pageviewDao;

}
