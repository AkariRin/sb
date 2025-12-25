package dev.rbq.sb.controller;

import dev.rbq.sb.dto.*;
import dev.rbq.sb.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户信息管理相关接口，包括获取、更新用户信息，修改密码和邮箱")
@SecurityRequirement(name = "session")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     */
    @GetMapping
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "获取成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "未登录或会话已过期"
            )
    })
    public ApiResponse<UserResponse> getCurrentUser(@Parameter(hidden = true) HttpSession session) {
        UserResponse user = userService.getCurrentUser(session);
        return ApiResponse.success(user);
    }

    /**
     * 更新用户信息
     */
    @PostMapping
    @Operation(summary = "更新用户信息", description = "更新当前登录用户的用户名等信息")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "更新成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "请求参数错误"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "未登录或会话已过期"
            )
    })
    public ApiResponse<UserResponse> updateUserInfo(
            @Parameter(description = "更新用户信息请求", required = true)
            @Valid @RequestBody UpdateUserInfoRequest request,
            @Parameter(hidden = true) HttpSession session) {
        UserResponse user = userService.updateUserInfo(session, request.getUsername());
        return ApiResponse.success("用户信息更新成功", user);
    }

    /**
     * 修改密码（已登录用户）
     */
    @PostMapping("/change-password")
    @Operation(summary = "修改密码", description = "已登录用户通过提供旧密码来修改密码")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "密码修改成功"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "旧密码错误或新密码不符合要求"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "未登录或会话已过期"
            )
    })
    public ApiResponse<Void> changePassword(
            @Parameter(description = "修改密码请求", required = true)
            @Valid @RequestBody ChangePasswordRequest request,
            @Parameter(hidden = true) HttpSession session) {
        userService.changePassword(session, request.getOldPassword(), request.getNewPassword());
        return ApiResponse.success("密码修改成功", null);
    }

    /**
     * 重置密码（未登录用户通过验证码）
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "未登录用户通过邮箱验证码重置密码")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "密码重置成功"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "验证码错误或已过期"
            )
    })
    public ApiResponse<Void> resetPassword(
            @Parameter(description = "重置密码请求", required = true)
            @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getEmail(), request.getVerificationCode(), request.getNewPassword());
        return ApiResponse.success("密码重置成功", null);
    }


    /**
     * 修改邮箱
     */
    @PostMapping("/change-email")
    @Operation(summary = "修改邮箱", description = "通过新邮箱的验证码修改用户邮箱地址")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "邮箱修改成功",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "验证码错误或邮箱已被使用"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "未登录或会话已过期"
            )
    })
    public ApiResponse<UserResponse> changeEmail(
            @Parameter(description = "修改邮箱请求", required = true)
            @Valid @RequestBody ChangeEmailRequest request,
            @Parameter(hidden = true) HttpSession session) {
        UserResponse user = userService.changeEmail(session, request.getNewEmail(), request.getVerificationCode());
        return ApiResponse.success("邮箱修改成功", user);
    }
}

