package dev.rbq.sb.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * 更新标签请求
 */
public class UpdateTagRequest {

    @NotNull(message = "标签ID不能为空")
    private UUID id;

    @NotBlank(message = "标签值不能为空")
    @Size(max = 20, message = "标签值长度不能超过20个字符")
    private String tag;

    // Constructors

    public UpdateTagRequest() {
    }

    public UpdateTagRequest(UUID id, String tag) {
        this.id = id;
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

