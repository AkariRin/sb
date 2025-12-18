package dev.rbq.sb.enums;

/**
 * 验证码用途枚举
 */
public enum VerificationPurpose {
    /**
     * 用户注册
     */
    REGISTER("注册"),


    /**
     * 重置密码
     */
    RESET_PASSWORD("重置密码"),

    /**
     * 修改邮箱
     */
    CHANGE_EMAIL("修改邮箱");

    private final String description;

    VerificationPurpose(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

