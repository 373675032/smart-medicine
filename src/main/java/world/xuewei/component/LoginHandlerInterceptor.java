package world.xuewei.component;

import org.springframework.web.servlet.HandlerInterceptor;
import world.xuewei.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author XUEW
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    /**
     * 在目标方式执行之前执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("loginUser");
        if (user == null) {
            //未登录,返回登录页面
            response.sendRedirect("/");
            return false;
        } else {
            //已登录,放行
            return true;
        }
    }
}
