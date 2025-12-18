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
     * 邮箱密码登录
     *
     * @param email    邮箱
     * @param password 密码
     * @param session  HTTP会话
     * @return 用户响应
     */
    @Transactional
    public UserResponse login(String email, String password, HttpSession session) {
        // 查找用户
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("邮箱或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("邮箱或密码错误");
        }

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
     * 更新用户信息
     *
     * @param session  HTTP会话
     * @param username 新用户名
     * @return 用户响应
     */
    @Transactional
    public UserResponse updateUserInfo(HttpSession session, String username) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }

        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        user.setUsername(username);
        user = userRepository.save(user);

        return convertToResponse(user);
    }

    /**
     * 修改密码（已登录用户）
     *
     * @param session     HTTP会话
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    @Transactional
    public void changePassword(HttpSession session, String oldPassword, String newPassword) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }

        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 修改邮箱
     *
     * @param session          HTTP会话
     * @param newEmail         新邮箱
     * @param verificationCode 验证码
     * @return 用户响应
     */
    @Transactional
    public UserResponse changeEmail(HttpSession session, String newEmail, String verificationCode) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }

        // 验证验证码
        if (!verificationCodeService.verifyCode(newEmail, verificationCode)) {
            throw new IllegalArgumentException("验证码错误或已过期");
        }

        // 检查新邮箱是否已被使用
        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("该邮箱已被使用");
        }

        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 更新邮箱
        user.setEmail(newEmail);
        user.setEmailVerified(true);
        user = userRepository.save(user);

        // 更新会话中的邮箱
        session.setAttribute("email", newEmail);

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

