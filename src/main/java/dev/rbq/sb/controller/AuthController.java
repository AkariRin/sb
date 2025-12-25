package dev.rbq.sb.controller;

import dev.rbq.sb.dto.*;
import dev.rbq.sb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口，包括注册、登录、登出")
public class AuthController {

    @Autowired
    private UserService userService;


    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "通过邮箱、用户名、密码和验证码注册新用户")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "注册成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "请求参数错误或验证码无效"
            )
    })
    public ApiResponse<UserResponse> register(
            @Parameter(description = "注册请求信息", required = true)
            @Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        return ApiResponse.success("注册成功", user);
    }

    /**
     * 邮箱密码登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用邮箱和密码进行登录")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "登录成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "邮箱或密码错误"
            )
    })
    public ApiResponse<UserResponse> login(
            @Parameter(description = "登录请求信息", required = true)
            @Valid @RequestBody LoginRequest request,
            @Parameter(hidden = true) HttpSession session) {
        UserResponse user = userService.login(request.getEmail(), request.getPassword(), session);
        return ApiResponse.success("登录成功", user);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "退出当前登录状态，清除会话")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "登出成功"
            )
    })
    public ApiResponse<Void> logout(@Parameter(hidden = true) HttpSession session) {
        session.invalidate();
        return ApiResponse.success("登出成功", null);
    }
}

