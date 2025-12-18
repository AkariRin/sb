package dev.rbq.sb.service;

import dev.rbq.sb.entity.VerificationCodeLog;
import dev.rbq.sb.enums.VerificationPurpose;
import dev.rbq.sb.repository.UserRepository;
import dev.rbq.sb.repository.VerificationCodeLogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码服务
 */
@Service
public class VerificationCodeService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private VerificationCodeLogRepository logRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.verification-code.length:6}")
    private int codeLength;

    @Value("${app.verification-code.expiration:300}")
    private long codeExpiration;

    @Value("${app.verification-code.rate-limit.email:60}")
    private long emailRateLimit;

    @Value("${app.verification-code.rate-limit.ip-count:5}")
    private int ipRateLimitCount;

    @Value("${app.verification-code.rate-limit.ip-window:300}")
    private long ipRateLimitWindow;

    private static final String CODE_KEY_PREFIX = "verify:code:";
    private static final String SENT_KEY_PREFIX = "verify:sent:";
    private static final String IP_KEY_PREFIX = "verify:ip:";

    /**
     * 发送验证码
     *
     * @param email     邮箱
     * @param purpose   验证码用途
     * @param ipAddress IP地址
     * @param session   HTTP会话（可选，CHANGE_EMAIL时必需）
     * @throws IllegalStateException 当触发防刷限制或业务规则校验失败时
     */
    public void sendVerificationCode(String email, VerificationPurpose purpose, String ipAddress, HttpSession session) {
        // 业务逻辑校验
        validateBusinessRules(email, purpose, session);

        // 检查邮箱发送频率限制（60秒内只能发送1次）
        String sentKey = SENT_KEY_PREFIX + email;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(sentKey))) {
            throw new IllegalStateException("验证码发送过于频繁，请稍后再试");
        }

        // 检查IP发送频率限制（5分钟内最多5次）
        String ipKey = IP_KEY_PREFIX + ipAddress;
        String ipCountStr = redisTemplate.opsForValue().get(ipKey);
        int ipCount = ipCountStr != null ? Integer.parseInt(ipCountStr) : 0;
        if (ipCount >= ipRateLimitCount) {
            throw new IllegalStateException("该IP发送次数过多，请稍后再试");
        }

        // 生成验证码
        String code = generateCode();

        try {
            // 发送邮件
            emailService.sendVerificationCode(email, code);

            // 存储验证码到Redis（5分钟过期）
            String codeKey = CODE_KEY_PREFIX + email;
            redisTemplate.opsForValue().set(codeKey, code, codeExpiration, TimeUnit.SECONDS);

            // 记录发送时间（60秒过期）
            redisTemplate.opsForValue().set(sentKey, String.valueOf(System.currentTimeMillis()), emailRateLimit, TimeUnit.SECONDS);

            // 增加IP计数（5分钟过期）
            redisTemplate.opsForValue().increment(ipKey);
            redisTemplate.expire(ipKey, ipRateLimitWindow, TimeUnit.SECONDS);

            // 记录发送日志
            logRepository.save(new VerificationCodeLog(email, ipAddress, true));

        } catch (Exception e) {
            // 记录失败日志
            logRepository.save(new VerificationCodeLog(email, ipAddress, false));
            throw new RuntimeException("验证码发送失败：" + e.getMessage(), e);
        }
    }

    /**
     * 验证业务规则
     *
     * @param email   邮箱
     * @param purpose 验证码用途
     * @param session HTTP会话
     */
    private void validateBusinessRules(String email, VerificationPurpose purpose, HttpSession session) {
        switch (purpose) {
            case REGISTER:
                // 注册：检查邮箱是否已存在
                if (userRepository.existsByEmail(email)) {
                    throw new IllegalArgumentException("该邮箱已被注册");
                }
                break;


            case RESET_PASSWORD:
                // 重置密码：检查用户是否存在
                if (!userRepository.existsByEmail(email)) {
                    throw new IllegalArgumentException("该邮箱未注册");
                }
                break;

            case CHANGE_EMAIL:
                // 修改邮箱：需要登录
                if (session == null || session.getAttribute("userId") == null) {
                    throw new IllegalStateException("请先登录");
                }
                // 检查新邮箱是否已被使用
                if (userRepository.existsByEmail(email)) {
                    throw new IllegalArgumentException("该邮箱已被使用");
                }
                break;

            default:
                throw new IllegalArgumentException("不支持的验证码用途");
        }
    }

    /**
     * 验证验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     */
    public boolean verifyCode(String email, String code) {
        String codeKey = CODE_KEY_PREFIX + email;
        String storedCode = redisTemplate.opsForValue().get(codeKey);

        if (storedCode == null) {
            return false;
        }

        boolean isValid = storedCode.equals(code);

        // 验证成功后删除验证码
        if (isValid) {
            redisTemplate.delete(codeKey);
        }

        return isValid;
    }

    /**
     * 生成随机验证码
     *
     * @return 验证码
     */
    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 检查邮箱是否可以发送验证码（用于提前检查）
     *
     * @param email 邮箱
     * @return 是否可以发送
     */
    public boolean canSendCode(String email) {
        String sentKey = SENT_KEY_PREFIX + email;
        return !Boolean.TRUE.equals(redisTemplate.hasKey(sentKey));
    }
}

