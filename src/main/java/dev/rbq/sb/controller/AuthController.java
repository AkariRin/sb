package dev.rbq.sb.controller;

import dev.rbq.sb.dto.*;
import dev.rbq.sb.service.UserService;
import dev.rbq.sb.service.VerificationCodeService;
import dev.rbq.sb.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 发送验证码
     */
    @PostMapping("/send-code")
    public ApiResponse<Void> sendVerificationCode(
            @Valid @RequestBody SendCodeRequest request,
            HttpServletRequest httpRequest) {
        String ipAddress = IpUtil.getClientIp(httpRequest);
        verificationCodeService.sendVerificationCode(request.getEmail(), ipAddress);
        return ApiResponse.success("验证码已发送，请查收邮件", null);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ApiResponse.success("注册成功", user);
    }

    /**
     * 邮箱验证码登录
     */
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        UserResponse user = userService.loginWithCode(request.getEmail(), request.getVerificationCode(), session);
        return ApiResponse.success("登录成功", user);
    }

    /**
     * 忘记密码（发送重置密码验证码）
     */
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(
            @Valid @RequestBody SendCodeRequest request,
            HttpServletRequest httpRequest) {
        String ipAddress = IpUtil.getClientIp(httpRequest);
        verificationCodeService.sendVerificationCode(request.getEmail(), ipAddress);
        return ApiResponse.success("验证码已发送，请查收邮件", null);
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getNewPassword());
        return ApiResponse.success("密码重置成功", null);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("登出成功", null);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(HttpSession session) {
        UserResponse user = userService.getCurrentUser(session);
        return ApiResponse.success(user);
    }
}

