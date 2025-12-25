package dev.rbq.sb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 删除标签请求
 */
@Schema(description = "删除标签请求")
public class DeleteTagRequest {

    @NotNull(message = "标签ID不能为空")
    @Schema(description = "要删除的标签ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
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
