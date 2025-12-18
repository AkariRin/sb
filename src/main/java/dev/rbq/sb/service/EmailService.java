package dev.rbq.sb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件服务
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送验证码邮件
     *
     * @param toEmail 收件人邮箱
     * @param code    验证码
     */
    public void sendVerificationCode(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("【超级大爆】邮箱验证码");
        message.setText(buildVerificationCodeText(code));

        mailSender.send(message);
    }

    /**
     * 构建验证码邮件内容
     *
     * @param code 验证码
     * @return 邮件内容
     */
    private String buildVerificationCodeText(String code) {
        return String.format("""
                您好！
                
                您的验证码是：%s
                
                验证码有效期为5分钟，请及时使用。
                
                如果这不是您的操作，请忽略此邮件。
                
                ——超级大爆团队
                """, code);
    }
}

