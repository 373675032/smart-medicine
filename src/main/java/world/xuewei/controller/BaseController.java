package world.xuewei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import world.xuewei.component.EmailClient;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.IllnessKind;
import world.xuewei.entity.User;
import world.xuewei.service.*;
import world.xuewei.utils.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * 基础控制器
 * <p>
 * ==========================================================================
 * 郑重说明：本项目免费开源！原创作者为：薛伟同学，严禁私自出售。
 * ==========================================================================
 * B站账号：薛伟同学
 * 微信公众号：薛伟同学
 * 作者博客：http://xuewei.world
 * ==========================================================================
 * 陆陆续续总会收到粉丝的提醒，总会有些人为了赚取利益倒卖我的开源项目。
 * 不乏有粉丝朋友出现钱付过去，那边只把代码发给他就跑路的，最后还是根据线索找到我。。
 * 希望各位朋友擦亮慧眼，谨防上当受骗！
 * ==========================================================================
 *
 * @author <a href="http://xuewei.world/about">XUEW</a>
 */
public class BaseController<T> {

    @Autowired
    protected ApiService apiService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected IllnessKindService illnessKindService;
    @Autowired
    protected IllnessMedicineService illnessMedicineService;
    @Autowired
    protected IllnessService illnessService;
    @Autowired
    protected MedicalNewsService medicalNewsService;
    @Autowired
    protected MedicineService medicineService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected FeedbackService feedbackService;

    @Autowired
    protected BaseService<T> service;

    @Autowired
    protected EmailClient emailClient;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected User loginUser;
    protected List<IllnessKind> kindList;

    /**
     * 保存、修改
     *
     * @param obj 目标对象
     * @return 响应结果
     */
    @ResponseBody
    @PostMapping("save")
    public RespResult save(T obj) {
        if (Assert.isEmpty(obj)) {
            return RespResult.fail("保存对象不能为空");
        }
        obj = service.save(obj);
        return RespResult.success("保存成功", obj);
    }

    /**
     * 删除
     *
     * @param id 主键ID
     * @return 响应结果
     */
    @ResponseBody
    @PostMapping("/delete")
    public RespResult delete(Integer id) {
        if (Assert.isEmpty(id)) {
            return RespResult.fail("删除ID不能为空");
        }
        if (service.delete(id) == 0) {
            T t = service.get(id);
            if (Assert.isEmpty(t)) {
                return RespResult.notFound("数据不存在");
            }
            return RespResult.fail("删除失败");
        }
        return RespResult.success("删除成功");
    }

    /**
     * 在每个子类方法调用之前先调用
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        loginUser = (User) session.getAttribute("loginUser");
        session.setAttribute("kindList", illnessKindService.findList());
    }
}
