package dev.rbq.sb.repository;

import dev.rbq.sb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 用户仓库接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
}

