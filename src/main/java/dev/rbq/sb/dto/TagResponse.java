package dev.rbq.sb.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * 标签响应
 */
@Schema(description = "标签响应")
public class TagResponse {

    @Schema(description = "标签ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "标签名称", example = "技术")
    private String tag;

    // Constructors

    public TagResponse() {
    }

    public TagResponse(UUID id, String tag) {
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
