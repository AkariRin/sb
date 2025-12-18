package dev.rbq.sb.controller;

import dev.rbq.sb.dto.*;
import dev.rbq.sb.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(HttpSession session) {
        UserResponse user = userService.getCurrentUser(session);
        return ApiResponse.success(user);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public ApiResponse<UserResponse> updateUserInfo(
            @Valid @RequestBody UpdateUserInfoRequest request,
            HttpSession session) {
        UserResponse user = userService.updateUserInfo(session, request.getUsername());
        return ApiResponse.success("用户信息更新成功", user);
    }

    /**
     * 修改密码（已登录用户）
     */
    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpSession session) {
        userService.changePassword(session, request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success("密码修改成功", null);
    }

    /**
     * 重置密码（未登录用户通过验证码）
     */
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getNewPassword());
        return ApiResponse.success("密码重置成功", null);
    }


    /**
     * 修改邮箱
     */
    @PostMapping("/change-email")
    public ApiResponse<UserResponse> changeEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            HttpSession session) {
        UserResponse user = userService.changeEmail(session, request.getNewEmail(), request.getVerificationCode());
        return ApiResponse.success("邮箱修改成功", user);
    }
}

