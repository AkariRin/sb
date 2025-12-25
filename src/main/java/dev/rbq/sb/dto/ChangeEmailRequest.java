package dev.rbq.sb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改邮箱请求DTO
 */
@Schema(description = "修改邮箱请求")
public class ChangeEmailRequest {

    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 32, message = "邮箱长度不能超过32个字符")
    @Schema(description = "新邮箱地址", example = "newemail@example.com", required = true)
    private String newEmail;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6位数字")
    @Schema(description = "新邮箱的验证码", example = "123456", required = true)
    private String verificationCode;

    // Getters and Setters

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
