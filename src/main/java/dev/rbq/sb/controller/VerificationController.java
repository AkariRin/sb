package dev.rbq.sb.controller;

import dev.rbq.sb.dto.ApiResponse;
import dev.rbq.sb.dto.SendCodeRequest;
import dev.rbq.sb.service.VerificationCodeService;
import dev.rbq.sb.util.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 验证码控制器
 */
@RestController
@RequestMapping("/api/verification")
@Tag(name = "验证码管理", description = "验证码发送相关接口，支持注册、登录、重置密码、修改邮箱等场景")
public class VerificationController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 统一发送验证码接口
     * 支持多种业务场景：注册、登录、重置密码、修改邮箱
     */
    @PostMapping("/send-code")
    @Operation(
            summary = "发送验证码",
            description = "根据不同的业务场景（注册、登录、重置密码、修改邮箱）向指定邮箱发送验证码"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "验证码发送成功"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "请求参数错误或邮箱格式不正确"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "429",
                    description = "请求过于频繁，请稍后再试"
            )
    })
    public ApiResponse<Void> sendVerificationCode(
            @Parameter(description = "发送验证码请求", required = true)
            @Valid @RequestBody SendCodeRequest request,
            @Parameter(hidden = true) HttpServletRequest httpRequest,
            @Parameter(hidden = true) HttpSession session) {
        String ipAddress = IpUtil.getClientIp(httpRequest);
        verificationCodeService.sendVerificationCode(
                request.getEmail(),
                request.getPurpose(),
                ipAddress,
                session
        );
        return ApiResponse.success("验证码已发送，请查收邮件", null);
    }
}

