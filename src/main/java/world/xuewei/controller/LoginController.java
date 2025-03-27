package world.xuewei.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.User;
import world.xuewei.utils.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录控制器

 * @author <a href="http://xuewei.world/about">XUEW</a>
 */
@RestController
@RequestMapping(value = "login")
public class LoginController extends BaseController<User> {

    /**
     * 注册
     */
    @PostMapping("/register")
    public RespResult register(User user, String code, String doctorQualificationImages) {
        String email = user.getUserEmail();
        if (Assert.isEmpty(email)) {
            return RespResult.fail("邮箱不能为空");
        }
        Map<String, Object> codeData = (Map<String, Object>) session.getAttribute("EMAIL_CODE" + email);
        if (codeData == null) {
            return RespResult.fail("尚未发送验证码");
        }
        String sentCode = (String) codeData.get("code");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) codeData.get("time"));
        calendar.add(Calendar.MINUTE, 5);
        if (System.currentTimeMillis() > calendar.getTime().getTime()) {
            session.removeAttribute("EMAIL_CODE" + email);
            return RespResult.fail("验证码已经超时");
        }
        if (!sentCode.equals(code)) {
            return RespResult.fail("验证码错误");
        }
        List<User> query = userService.query(User.builder().userAccount(user.getUserAccount()).build());
        if (Assert.notEmpty(query)) {
            return RespResult.fail("账户已被注册");
        }
        // 如果是医生类型，检查资质图片
        if (user.getUserType() == 1) {
            if (StringUtils.isEmpty(doctorQualificationImages)) {
                return RespResult.fail("请上传医生资质证明");
            }
            // 设置医生资质图片路径
            user.setDoctorQualificationImages(doctorQualificationImages);
            // 医生默认为待审核状态
            user.setAuditStatus(0);
        } else {
            // 普通用户默认为已审核通过
            user.setAuditStatus(1);
        }
        // 设置角色状态
        user.setRoleStatus(0); // 普通用户
        // 设置默认头像
        user.setImgPath("/assets/images/default.jpg");
        user = userService.save(user);
        session.setAttribute("loginUser", user);
        
        // 如果是医生，提示需要等待审核
        if (user.getUserType() == 1) {
            return RespResult.success("注册成功，请等待管理员审核后方可登录", user);
        }
        return RespResult.success("注册成功", user);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public RespResult login(User user) {
        List<User> users = userService.query(user);
        if (Assert.notEmpty(users)) {
            User loginUser = users.get(0);
            
            // 检查医生账号的审核状态
            if (loginUser.getUserType() == 1) {
                if (loginUser.getAuditStatus() == 0) {
                    return RespResult.fail("您的医生账号正在审核中，请耐心等待或联系管理员");
                } else if (loginUser.getAuditStatus() == 2) {
                    return RespResult.fail("您的医生账号审核未通过，请联系管理员了解详情");
                }
            }
            
            session.setAttribute("loginUser", loginUser);
            return RespResult.success("登录成功");
        }
        if (Assert.isEmpty(userService.query(User.builder().userAccount(user.getUserAccount()).build()))) {
            return RespResult.fail("账户尚未注册");
        }
        return RespResult.fail("密码错误");
    }

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/sendEmailCode")
    public RespResult sendEmailCode(String email, Map<String, Object> map) {
        if (StrUtil.isEmpty(email)) {
            return RespResult.fail("邮箱不可为空");
        }
        // 发送验证码
        String verifyCode = emailClient.sendEmailCode(email);
        map.put("email", email);
        map.put("code", verifyCode);
        map.put("time", new Date());
        session.setAttribute("EMAIL_CODE" + email, map);
        return RespResult.success("发送成功");
    }
}
