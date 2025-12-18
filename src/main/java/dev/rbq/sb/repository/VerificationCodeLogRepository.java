package dev.rbq.sb.repository;

import dev.rbq.sb.entity.VerificationCodeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 验证码日志仓库接口
 */
@Repository
public interface VerificationCodeLogRepository extends JpaRepository<VerificationCodeLog, Long> {

    /**
     * 统计指定时间后某邮箱的发送次数
     *
     * @param email 邮箱
     * @param after 时间点
     * @return 发送次数
     */
    long countByEmailAndRequestTimeAfter(String email, LocalDateTime after);

    /**
     * 统计指定时间后某IP的发送次数
     *
     * @param ipAddress IP地址
     * @param after     时间点
     * @return 发送次数
     */
    long countByIpAddressAndRequestTimeAfter(String ipAddress, LocalDateTime after);
}


