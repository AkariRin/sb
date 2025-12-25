package dev.rbq.sb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * 创建标签请求
 */
@Schema(description = "创建标签请求")
public class CreateTagRequest {

    @NotNull(message = "标签ID不能为空")
    @Schema(description = "标签ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID id;

    @NotBlank(message = "标签值不能为空")
    @Size(max = 20, message = "标签值长度不能超过20个字符")
    @Schema(description = "标签名称", example = "技术", required = true)
    private String tag;

    // Constructors

    public CreateTagRequest() {
    }

    public CreateTagRequest(UUID id, String tag) {
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
