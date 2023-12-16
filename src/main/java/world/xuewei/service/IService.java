package world.xuewei.service;

import java.io.Serializable;
import java.util.List;

/**
 * 基础服务接口
 *
 * @author XUEW
 */
public interface IService<T> {

    /**
     * 保存
     */
    T save(T t);

    /**
     * 根据主键获取
     */
    T get(Serializable id);

    /**
     * 根据主键删除
     */
    int delete(Serializable id);

    /**
     * 查询
     */
    List<T> query(T o);

    /**
     * 查询全部
     */
    List<T> all();
}
