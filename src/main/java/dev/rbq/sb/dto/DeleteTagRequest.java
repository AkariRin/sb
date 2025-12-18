package dev.rbq.sb.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 删除标签请求
 */
public class DeleteTagRequest {

    @NotNull(message = "标签ID不能为空")
    private UUID id;

    // Constructors

    public DeleteTagRequest() {
    }

    public DeleteTagRequest(UUID id) {
        this.id = id;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

