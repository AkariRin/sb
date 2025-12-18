package dev.rbq.sb.controller;

import dev.rbq.sb.dto.ApiResponse;
import dev.rbq.sb.dto.SendCodeRequest;
import dev.rbq.sb.service.VerificationCodeService;
import dev.rbq.sb.util.IpUtil;
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
public class VerificationController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 统一发送验证码接口
     * 支持多种业务场景：注册、登录、重置密码、修改邮箱
     */
    @PostMapping("/send-code")
    public ApiResponse<Void> sendVerificationCode(
            @Valid @RequestBody SendCodeRequest request,
            HttpServletRequest httpRequest,
            HttpSession session) {
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

