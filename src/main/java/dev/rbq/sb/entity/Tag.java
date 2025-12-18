package dev.rbq.sb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * 标签实体
 */
@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private UUID id;

    @NotBlank(message = "标签值不能为空")
    @Size(max = 20, message = "标签值长度不能超过20个字符")
    @Column(name = "tag", length = 20, unique = true, nullable = false)
    private String tag;

    // Constructors

    public Tag() {
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

