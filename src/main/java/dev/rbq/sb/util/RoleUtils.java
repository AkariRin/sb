package dev.rbq.sb.util;

import dev.rbq.sb.enums.Role;

/**
 * 角色工具类
 * 提供角色相关的辅助方法
 */
public class RoleUtils {

    /**
     * 检查角色1是否高于或等于角色2
     * 枚举值的 ordinal 越小，权限越高
     *
     * @param role1 角色1
     * @param role2 角色2
     * @return true 如果 role1 的权限 >= role2
     */
    public static boolean isRoleHigherOrEqual(Role role1, Role role2) {
        return role1.ordinal() <= role2.ordinal();
    }

    /**
     * 检查角色1是否高于角色2
     *
     * @param role1 角色1
     * @param role2 角色2
     * @return true 如果 role1 的权限 > role2
     */
    public static boolean isRoleHigher(Role role1, Role role2) {
        return role1.ordinal() < role2.ordinal();
    }

    /**
     * 检查是否为管理员级别（ADMIN 或 SUPER_ADMIN）
     *
     * @param role 要检查的角色
     * @return true 如果是管理员级别
     */
    public static boolean isAdminLevel(Role role) {
        return role == Role.SUPER_ADMIN || role == Role.ADMIN;
    }

    /**
     * 检查是否为超级管理员
     *
     * @param role 要检查的角色
     * @return true 如果是超级管理员
     */
    public static boolean isSuperAdmin(Role role) {
        return role == Role.SUPER_ADMIN;
    }

    /**
     * 检查是否为普通管理员
     *
     * @param role 要检查的角色
     * @return true 如果是普通管理员（不包括超级管理员）
     */
    public static boolean isAdmin(Role role) {
        return role == Role.ADMIN;
    }

    /**
     * 检查是否为普通用户
     *
     * @param role 要检查的角色
     * @return true 如果是普通用户
     */
    public static boolean isUser(Role role) {
        return role == Role.USER;
    }

    /**
     * 获取角色的中文显示名称
     *
     * @param role 角色
     * @return 中文名称
     */
    public static String getRoleDisplayName(Role role) {
        switch (role) {
            case SUPER_ADMIN:
                return "超级管理员";
            case ADMIN:
                return "管理员";
            case USER:
                return "普通用户";
            default:
                return "未知角色";
        }
    }

    /**
     * 获取角色的权限级别（数字越小，权限越高）
     *
     * @param role 角色
     * @return 权限级别
     */
    public static int getRoleLevel(Role role) {
        return role.ordinal();
    }
}

