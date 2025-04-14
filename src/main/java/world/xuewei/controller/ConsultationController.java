package world.xuewei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.ChatMessage;
import world.xuewei.entity.DoctorRegistration;
import world.xuewei.entity.User;
import world.xuewei.service.ChatMessageService;
import world.xuewei.service.DoctorRegistrationService;
import world.xuewei.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 在线问诊控制器
 * @author xuewei
 */
@Controller
public class ConsultationController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorRegistrationService doctorRegistrationService;
    
    @Autowired
    private HttpSession session;

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 在线问诊页面 - 显示审核通过的医生列表
     */
    @GetMapping("/consultation")
    public String consultation(Model model, 
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "6") Integer size) {
        // 验证用户登录
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/index.html";
        }
        
        // 查询所有审核通过的医生
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_type", 1) // 医生类型
                    .eq("audit_status", 1) // 审核通过
                    .orderByDesc("create_time");
        
        IPage<User> pageData = userService.page(new Page<>(page, size), queryWrapper);
        
        // 查询当前用户所有挂号的医生，活跃的排在前面
        List<DoctorRegistration> registrations = doctorRegistrationService.getAllUserRegistrationsPrioritizeActive(loginUser.getId());
        Map<Integer, DoctorRegistration> registrationMap = registrations.stream()
                .collect(Collectors.toMap(DoctorRegistration::getDoctorId, registration -> registration, (r1, r2) -> r1));
        
        // 创建一个Map来存储医生ID与其挂号状态
        Map<Integer, Integer> doctorRegistrationStatus = new HashMap<>();
        
        // 标记当前用户已挂号的医生
        for (User doctor : pageData.getRecords()) {
            DoctorRegistration reg = registrationMap.get(doctor.getId());
            if (reg != null) {
                doctor.setIsRegistered(reg.getStatus() == 0); // 只将活跃的标记为已挂号
                doctorRegistrationStatus.put(doctor.getId(), reg.getStatus());
            } else {
                doctor.setIsRegistered(false);
            }
        }
        
        model.addAttribute("doctors", pageData.getRecords());
        model.addAttribute("doctorPage", pageData);
        model.addAttribute("allRegistrations", registrations);
        model.addAttribute("doctorRegistrationStatus", doctorRegistrationStatus);
        
        return "consultation";
    }
    
    /**
     * 挂号医生
     */
    @PostMapping("/consultation/register")
    @ResponseBody
    public RespResult registerDoctor(@RequestParam("doctorId") Integer doctorId) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 检查医生是否存在且审核通过
        User doctor = userService.getById(doctorId);
        if (doctor == null || doctor.getUserType() != 1 || doctor.getAuditStatus() != 1) {
            return RespResult.fail("医生不存在或未通过审核");
        }
        
        // 检查是否已经有活动的挂号
        if (doctorRegistrationService.isRegistered(loginUser.getId(), doctorId)) {
            return RespResult.fail("您已经挂号该医生，可以直接进行问诊");
        }
        
        // 尝试重新激活或创建挂号记录
        boolean success = doctorRegistrationService.reactivateOrCreateRegistration(loginUser.getId(), doctorId);
        
        if (success) {
            return RespResult.success("挂号成功");
        } else {
            return RespResult.fail("挂号失败，请稍后重试");
        }
    }
    
    /**
     * 医生聊天页面
     */
    @GetMapping("/consultation-chat")
    public String doctorChat(Model model, 
                           @RequestParam("doctorId") Integer doctorId,
                           @RequestParam("doctorName") String doctorName) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 获取挂号记录
        DoctorRegistration registration = doctorRegistrationService.getRegistrationRecord(loginUser.getId(), doctorId);
        
        // 检查是否已挂号
        if (registration == null) {
            model.addAttribute("message", "您尚未挂号该医生，请先挂号");
            return "redirect:/consultation";
        }
        
        // 获取医生信息
        User doctor = userService.getById(doctorId);
        model.addAttribute("doctor", doctor);
        model.addAttribute("user", loginUser);
        model.addAttribute("consultationStatus", registration.getStatus());
        
        // 获取所有聊天记录
        List<ChatMessage> messages = chatMessageService.getAllMessages(loginUser.getId(), doctorId);
        model.addAttribute("messages", messages);
        
        return "doctor-chat";
    }

    /**
     * 发送消息给医生
     */
    @PostMapping("/consultation-send")
    @ResponseBody
    public RespResult sendMessage(@RequestParam("doctorId") Integer doctorId,
                                 @RequestParam("content") String content) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 检查挂号状态
        DoctorRegistration registration = doctorRegistrationService.getRegistrationRecord(loginUser.getId(), doctorId);
        if (registration == null) {
            return RespResult.fail("您尚未挂号该医生");
        }
        
        // 检查问诊是否已结束
        if (registration.getStatus() == 1) {
            return RespResult.fail("问诊已结束，无法发送消息");
        }
        
        // 创建新消息
        ChatMessage message = new ChatMessage();
        message.setUserId(loginUser.getId());
        message.setDoctorId(doctorId);
        message.setContent(content);
        message.setFromDoctor(false); // 用户发送的消息
        message.setCreateTime(new Date());
        message.setIsRead(false); // 标记为未读
        
        // 保存消息
        chatMessageService.save(message);
        
        Map<String, Object> data = new HashMap<>();
        data.put("messageId", message.getId());
        
        return RespResult.success("消息发送成功", data);
    }

    /**
     * 获取与医生的聊天记录
     */
    @GetMapping("/consultation-messages")
    @ResponseBody
    public RespResult getMessages(@RequestParam("doctorId") Integer doctorId,
                                 @RequestParam(value = "lastMessageId", defaultValue = "0") Integer lastMessageId) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 查询新消息
        List<ChatMessage> messages = chatMessageService.getNewMessages(loginUser.getId(), doctorId, lastMessageId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);
        
        return RespResult.success("获取成功", data);
    }

    /**
     * 模拟医生回复（实际系统中应由医生端发送）
     */
    private ChatMessage simulateDoctorReply(Integer doctorId, Integer userId, String userMessage) {
        // 获取医生信息
        User doctor = userService.getById(doctorId);
        
        // 创建医生回复消息
        ChatMessage replyMessage = new ChatMessage();
        replyMessage.setUserId(userId);
        replyMessage.setDoctorId(doctorId);
        replyMessage.setFromDoctor(true);
        replyMessage.setCreateTime(new Date());
        
        // 这里可以接入AI服务或简单回复
        String reply = "您好，我是" + doctor.getUserName() + "医生。我已收到您的消息：\"" + userMessage + 
                       "\"。请稍等，我正在分析您的情况...";
        replyMessage.setContent(reply);
        
        // 保存回复消息
        chatMessageService.save(replyMessage);
        
        return replyMessage;
    }
} 