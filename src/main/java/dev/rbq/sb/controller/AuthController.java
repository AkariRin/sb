package dev.rbq.sb.controller;

import dev.rbq.sb.dto.*;
import dev.rbq.sb.service.UserService;
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


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ApiResponse.success("注册成功", user);
    }

    /**
     * 邮箱密码登录
     */
    @PostMapping("/login")
    public ApiResponse<UserResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpSession session) {
        UserResponse user = userService.login(request.getEmail(), request.getPassword(), session);
        return ApiResponse.success("登录成功", user);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.success("登出成功", null);
    }
}

