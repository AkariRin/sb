package dev.rbq.sb.dto;

import dev.rbq.sb.enums.VerificationPurpose;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 发送验证码请求DTO
 */
@Schema(description = "发送验证码请求")
public class SendCodeRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 32, message = "邮箱长度不能超过32个字符")
    @Schema(description = "接收验证码的邮箱地址", example = "user@example.com", required = true)
    private String email;

    @NotNull(message = "验证码用途不能为空")
    @Schema(description = "验证码用途", example = "REGISTER", required = true, allowableValues = {"REGISTER", "LOGIN", "RESET_PASSWORD", "CHANGE_EMAIL"})
    private VerificationPurpose purpose;

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public VerificationPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(VerificationPurpose purpose) {
        this.purpose = purpose;
    }
}
