package dev.rbq.sb.service;

import dev.rbq.sb.dto.RegisterRequest;
import dev.rbq.sb.dto.UserResponse;
import dev.rbq.sb.entity.User;
import dev.rbq.sb.enums.Role;
import dev.rbq.sb.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 用户响应
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {
        // 验证验证码
        if (!verificationCodeService.verifyCode(request.getEmail(), request.getVerificationCode())) {
            throw new IllegalArgumentException("验证码错误或已过期");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEmailVerified(true); // 通过验证码注册，邮箱已验证
        user.setBanned(false);

        user = userRepository.save(user);

        return convertToResponse(user);
    }

    /**
     * 邮箱验证码登录
     *
     * @param email            邮箱
     * @param verificationCode 验证码
     * @param session          HTTP会话
     * @return 用户响应
     */
    @Transactional
    public UserResponse loginWithCode(String email, String verificationCode, HttpSession session) {
        // 验证验证码
        if (!verificationCodeService.verifyCode(email, verificationCode)) {
            throw new IllegalArgumentException("验证码错误或已过期");
        }

        // 查找用户
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 检查是否被封禁
        if (user.getBanned()) {
            throw new IllegalStateException("该账号已被封禁");
        }

        // 设置会话
        session.setAttribute("userId", user.getId().toString());
        session.setAttribute("email", user.getEmail());
        session.setAttribute("role", user.getRole().name());

        return convertToResponse(user);
    }

    /**
     * 重置密码
     *
     * @param email            邮箱
     * @param verificationCode 验证码
     * @param newPassword      新密码
     */
    @Transactional
    public void resetPassword(String email, String verificationCode, String newPassword) {
        // 验证验证码
        if (!verificationCodeService.verifyCode(email, verificationCode)) {
            throw new IllegalArgumentException("验证码错误或已过期");
        }

        // 查找用户
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param session HTTP会话
     * @return 用户响应
     */
    public UserResponse getCurrentUser(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }

        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return convertToResponse(user);
    }

    /**
     * 转换User实体为UserResponse
     *
     * @param user 用户实体
     * @return 用户响应DTO
     */
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setEmailVerified(user.getEmailVerified());
        response.setBanned(user.getBanned());
        response.setRegisterTime(user.getRegisterTime());
        return response;
    }
}

