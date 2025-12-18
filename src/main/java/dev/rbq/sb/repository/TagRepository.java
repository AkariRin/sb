package dev.rbq.sb.repository;

import dev.rbq.sb.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * 标签仓库接口
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    /**
     * 根据标签值查询标签
     *
     * @param tag 标签值
     * @return 标签
     */
    Optional<Tag> findByTag(String tag);

    /**
     * 检查标签值是否已存在
     *
     * @param tag 标签值
     * @return 是否存在
     */
    boolean existsByTag(String tag);

    /**
     * 检查标签值是否已存在（排除指定ID）
     *
     * @param tag 标签值
     * @param id  要排除的标签ID
     * @return 是否存在
     */
    boolean existsByTagAndIdNot(String tag, UUID id);
}

