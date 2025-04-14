package world.xuewei.controller;

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
import java.util.*;

/**
 * 医生消息控制器
 */
@Controller
public class DoctorMessageController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private ChatMessageService chatMessageService;
    
    @Autowired
    private DoctorRegistrationService doctorRegistrationService;
    
    @Autowired
    private HttpSession session;
    
    /**
     * 医生聊天页面 - 显示所有患者列表
     */
    @GetMapping("/doctor/messages")
    public String doctorMessageCenter(Model model) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return "redirect:/index.html";
        }
        
        // 获取所有挂号此医生的患者，包括历史记录，活跃的排在前面
        List<User> patients = new ArrayList<>();
        List<DoctorRegistration> registrations = doctorRegistrationService.getAllDoctorRegistrationsPrioritizeActive(loginUser.getId());
        
        // 记录状态信息
        Map<Integer, Integer> registrationStatus = new HashMap<>();
        
        // 遍历获取患者信息和未读消息数
        Map<Integer, Integer> unreadCounts = new HashMap<>();
        for (DoctorRegistration reg : registrations) {
            User patient = userService.getById(reg.getUserId());
            if (patient != null) {
                patients.add(patient);
                // 获取未读消息数
                int unreadCount = chatMessageService.countUnreadMessages(reg.getUserId(), loginUser.getId());
                unreadCounts.put(patient.getId(), unreadCount);
                registrationStatus.put(patient.getId(), reg.getStatus());
            }
        }
        
        model.addAttribute("patients", patients);
        model.addAttribute("unreadCounts", unreadCounts);
        model.addAttribute("registrationStatus", registrationStatus);
        
        return "doctor-messages";
    }
    
    /**
     * 医生-患者聊天页面
     */
    @GetMapping("/doctor-chat")
    public String doctorChat(Model model, @RequestParam("patientId") Integer patientId) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return "redirect:/index.html";
        }
        
        // 获取患者信息
        User patient = userService.getById(patientId);
        if (patient == null) {
            return "redirect:/doctor-consultation";
        }
        
        // 检查患者是否已挂号，但允许查看已结束的问诊
        DoctorRegistration registration = doctorRegistrationService.getRegistrationRecord(patientId, loginUser.getId());
        if (registration == null) {
            return "redirect:/doctor-consultation";
        }
        
        // 获取聊天记录 - 使用已有方法
        List<ChatMessage> messages = chatMessageService.getAllMessages(patientId, loginUser.getId());
        
        // 如果问诊未结束，将消息标记为已读
        if (registration.getStatus() == 0) {
            chatMessageService.markMessagesAsRead(patientId, loginUser.getId());
        }
        
        // 获取所有挂号此医生的患者 - 活跃的排在前面
        List<DoctorRegistration> registrations = doctorRegistrationService.getAllDoctorRegistrationsPrioritizeActive(loginUser.getId());
        List<User> allPatients = new ArrayList<>();
        Map<Integer, Integer> registrationStatus = new HashMap<>();
        
        for (DoctorRegistration reg : registrations) {
            User p = userService.getById(reg.getUserId());
            if (p != null) {
                allPatients.add(p);
                registrationStatus.put(p.getId(), reg.getStatus());
            }
        }
        
        // 为每个患者获取未读消息数
        Map<Integer, Integer> unreadCounts = new HashMap<>();
        for (User p : allPatients) {
            unreadCounts.put(p.getId(), chatMessageService.countUnreadMessages(p.getId(), loginUser.getId()));
        }
        
        model.addAttribute("doctor", loginUser);
        model.addAttribute("patient", patient);
        model.addAttribute("messages", messages);
        model.addAttribute("allPatients", allPatients);
        model.addAttribute("unreadCounts", unreadCounts);
        model.addAttribute("consultationStatus", registration.getStatus());
        model.addAttribute("registrationStatus", registrationStatus);
        
        return "doctor-patient-chat";
    }
    
    /**
     * 医生回复患者消息
     */
    @PostMapping("/doctor-reply")
    @ResponseBody
    public RespResult doctorReply(@RequestParam("patientId") Integer patientId,
                                 @RequestParam("content") String content) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return RespResult.fail("只有医生可以回复患者消息");
        }
        
        // 检查问诊是否已结束
        DoctorRegistration registration = doctorRegistrationService.getRegistrationRecord(patientId, loginUser.getId());
        if (registration == null) {
            return RespResult.fail("患者未挂号");
        }
        
        if (registration.getStatus() == 1) {
            return RespResult.fail("问诊已结束，无法发送消息");
        }
        
        // 创建新消息
        ChatMessage message = new ChatMessage();
        message.setUserId(patientId);
        message.setDoctorId(loginUser.getId());
        message.setContent(content);
        message.setFromDoctor(true); // 医生发送的消息
        message.setCreateTime(new Date());
        message.setIsRead(false); // 标记为未读
        
        // 保存消息
        chatMessageService.save(message);
        
        Map<String, Object> data = new HashMap<>();
        data.put("messageId", message.getId());
        
        return RespResult.success("回复成功", data);
    }

    /**
     * 医生在线问诊入口 - 直接进入聊天页面
     */
    @GetMapping("/doctor-consultation")
    public String doctorConsultation() {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return "redirect:/index.html";
        }
        
        // 获取所有挂号此医生的患者，活跃的排在前面
        List<DoctorRegistration> registrations = doctorRegistrationService.getAllDoctorRegistrationsPrioritizeActive(loginUser.getId());
        
        if (registrations.isEmpty()) {
            // 如果没有患者，显示一个信息页面
            return "doctor-no-patients";
        }
        
        // 立即重定向到聊天页面，与第一个患者聊天
        return "redirect:/doctor-chat?patientId=" + registrations.get(0).getUserId();
    }

    /**
     * 获取患者的聊天记录（医生视角）
     */
    @GetMapping("/doctor-patient-messages")
    @ResponseBody
    public RespResult getPatientMessages(@RequestParam("patientId") Integer patientId,
                                        @RequestParam(value = "lastMessageId", defaultValue = "0") Integer lastMessageId) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return RespResult.fail("只有医生可以访问");
        }
        
        // 查询新消息
        List<ChatMessage> messages = chatMessageService.getNewMessages(patientId, loginUser.getId(), lastMessageId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("messages", messages);
        
        return RespResult.success("获取成功", data);
    }

    /**
     * 医生直接进入最新或优先级最高的聊天
     */
    @GetMapping("/doctor-latest-chat")
    public String doctorLatestChat() {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return "redirect:/index.html";
        }
        
        // 获取所有挂号此医生的患者，活跃的排在前面
        List<DoctorRegistration> registrations = doctorRegistrationService.getAllDoctorRegistrationsPrioritizeActive(loginUser.getId());
        
        if (registrations.isEmpty()) {
            // 如果没有患者，跳转到首页或提示页面
            return "redirect:/index.html";
        }
        
        // 首先查找有未读消息的活跃患者
        Integer patientWithUnreadMessages = null;
        
        for (DoctorRegistration reg : registrations) {
            // 只考虑活跃的问诊
            if (reg.getStatus() == 0) {
                int unreadCount = chatMessageService.countUnreadMessages(reg.getUserId(), loginUser.getId());
                if (unreadCount > 0) {
                    patientWithUnreadMessages = reg.getUserId();
                    break;
                }
            }
        }
        
        // 如果有未读消息的患者，优先进入与该患者的聊天界面
        if (patientWithUnreadMessages != null) {
            return "redirect:/doctor-chat?patientId=" + patientWithUnreadMessages;
        }
        
        // 如果没有未读消息，优先选择第一个活跃的患者
        for (DoctorRegistration reg : registrations) {
            if (reg.getStatus() == 0) {
                return "redirect:/doctor-chat?patientId=" + reg.getUserId();
            }
        }
        
        // 如果没有活跃的患者，则显示历史记录中的第一个患者
        return "redirect:/doctor-chat?patientId=" + registrations.get(0).getUserId();
    }

    /**
     * 医生结束问诊
     */
    @PostMapping("/end-consultation")
    @ResponseBody
    public RespResult endConsultation(@RequestParam("patientId") Integer patientId) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return RespResult.fail("请先登录");
        }
        
        // 检查是否是医生
        if (loginUser.getUserType() != 1) {
            return RespResult.fail("只有医生可以结束问诊");
        }
        
        // 检查患者是否已挂号
        if (!doctorRegistrationService.isRegistered(patientId, loginUser.getId())) {
            return RespResult.fail("该患者未挂号或挂号已过期");
        }
        
        // 删除挂号记录，结束问诊
        boolean success = doctorRegistrationService.removeRegistration(patientId, loginUser.getId());
        
        if (success) {
            return RespResult.success("问诊已结束");
        } else {
            return RespResult.fail("操作失败，请稍后重试");
        }
    }
} 