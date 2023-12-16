package world.xuewei.component;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件客户端
 *
 * @author XUEW
 */
@Component
public class EmailClient {

    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 发送方邮箱
     */
    @Value("${spring.mail.username}")
    private String email;

    /**
     * 有效时长
     */
    @Value("${spring.mail.valid}")
    private Integer valid;

    /**
     * 内容模版
     */
    @Value("${spring.mail.template}")
    private String template;

    /**
     * 标题
     */
    @Value("${spring.mail.title}")
    private String title;

    /**
     * 发送邮件验证码
     *
     * @param targetEmail 目标邮箱
     * @return 验证码
     */
    public String sendEmailCode(String targetEmail) {
        // 生成随机验证码
        String verifyCode = RandomUtil.randomNumbers(6);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject(title);
            helper.setText(String.format(template, verifyCode, valid), true);
            helper.setFrom(email);
            helper.setTo(targetEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
        return verifyCode;
    }

    /**
     * 发送邮箱
     *
     * @param targetEmail 目标邮箱
     * @param content     发送内容
     */
    public void sendEmail(String targetEmail, String title, String content) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setSubject(title);
            helper.setText(content, true);
            helper.setFrom(email);
            helper.setTo(targetEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
    }
}
