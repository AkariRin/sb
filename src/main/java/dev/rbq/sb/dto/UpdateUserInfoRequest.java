package dev.rbq.sb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 更新用户信息请求DTO
 */
public class UpdateUserInfoRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 32, message = "用户名长度不能超过32个字符")
    private String username;

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

