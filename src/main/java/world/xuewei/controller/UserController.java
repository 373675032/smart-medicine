package world.xuewei.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.User;
import world.xuewei.utils.Assert;

/**
 * 用户控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping(value = "user")
public class UserController extends BaseController<User> {

    /**
     * 修改资料
     */
    @PostMapping("/saveProfile")
    public RespResult saveProfile(User user) {
        if (Assert.isEmpty(user)) {
            return RespResult.fail("保存对象不能为空");
        }
        user = userService.save(user);
        session.setAttribute("loginUser", user);
        return RespResult.success("保存成功");
    }

    /**
     * 修改密码
     */
    @PostMapping("/savePassword")
    public RespResult savePassword(String oldPass, String newPass) {
        if (!loginUser.getUserPwd().equals(oldPass)) {
            return RespResult.fail("旧密码错误");
        }
        loginUser.setUserPwd(newPass);
        loginUser = userService.save(loginUser);
        session.setAttribute("loginUser", loginUser);
        return RespResult.success("保存成功");
    }

    /**
     * 更新用户角色状态
     */
    @PostMapping("/updateRole")
    public RespResult updateRole(Integer userId, Integer roleStatus) {
        // 验证当前用户是否为管理员
        if (loginUser.getRoleStatus() != 1) {
            return RespResult.fail("无权限执行此操作");
        }
        
        // 查询要修改的用户
        User user = userService.getById(userId);
        if (user == null) {
            return RespResult.fail("用户不存在");
        }
        
        // 更新角色状态
        user.setRoleStatus(roleStatus);
        userService.updateById(user);
        
        return RespResult.success("更新成功");
    }

    /**
     * 审核医生账号
     */
    @PostMapping("/auditDoctor")
    public RespResult auditDoctor(Integer userId, Integer auditStatus) {
        // 验证当前用户是否为管理员
        if (loginUser.getRoleStatus() != 1) {
            return RespResult.fail("无权限执行此操作");
        }
        
        // 验证参数
        if (userId == null || auditStatus == null) {
            return RespResult.fail("参数错误");
        }
        
        if (auditStatus != 1 && auditStatus != 2) {
            return RespResult.fail("审核状态参数错误");
        }
        
        // 查询要审核的用户
        User user = userService.getById(userId);
        if (user == null) {
            return RespResult.fail("用户不存在");
        }
        
        // 确保是医生账号
        if (user.getUserType() != 1) {
            return RespResult.fail("只能审核医生账号");
        }
        
        // 更新审核状态
        user.setAuditStatus(auditStatus);
        userService.updateById(user);
        
        return RespResult.success("审核操作成功");
    }

    /**
     * 删除用户
     */
    @PostMapping("/deleteUser")
    public RespResult deleteUser(Integer userId) {
        // 验证当前用户是否为管理员
        if (loginUser.getRoleStatus() != 1) {
            return RespResult.fail("无权限执行此操作");
        }

        // 验证参数
        if (userId == null) {
            return RespResult.fail("参数错误");
        }

        // 查询要删除的用户
        User user = userService.getById(userId);
        if (user == null) {
            return RespResult.fail("用户不存在");
        }

        // 不允许删除admin账户
        if ("admin".equals(user.getUserAccount())) {
            return RespResult.fail("不允许删除管理员账户");
        }

        // 执行删除操作
        int result = userService.delete(userId);
        if (result > 0) {
            return RespResult.success("删除成功");
        } else {
            return RespResult.fail("删除失败");
        }
    }
}
