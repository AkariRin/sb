package dev.rbq.sb.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 验证码发送日志实体
 */
@Entity
@Table(name = "verification_code_logs", indexes = {
        @Index(name = "idx_email_time", columnList = "email,request_time"),
        @Index(name = "idx_ip_time", columnList = "ip_address,request_time")
})
public class VerificationCodeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", length = 32, nullable = false)
    private String email;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "request_time", nullable = false, updatable = false)
    private LocalDateTime requestTime;

    @Column(name = "success", nullable = false)
    private Boolean success;

    // Constructors

    public VerificationCodeLog() {
    }

    public VerificationCodeLog(String email, String ipAddress, Boolean success) {
        this.email = email;
        this.ipAddress = ipAddress;
        this.success = success;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

