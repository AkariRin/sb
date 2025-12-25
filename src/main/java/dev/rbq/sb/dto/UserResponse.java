package dev.rbq.sb.dto;

import dev.rbq.sb.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户信息响应DTO
 */
@Schema(description = "用户信息响应")
public class UserResponse {

    @Schema(description = "用户ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "用户邮箱", example = "user@example.com")
    private String email;

    @Schema(description = "用户名", example = "张三")
    private String username;

    @Schema(description = "用户角色", example = "USER")
    private Role role;

    @Schema(description = "邮箱是否已验证", example = "true")
    private Boolean emailVerified;

    @Schema(description = "是否被封禁", example = "false")
    private Boolean banned;

    @Schema(description = "注册时间", example = "2025-12-26T10:30:00")
    private LocalDateTime registerTime;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }
}
