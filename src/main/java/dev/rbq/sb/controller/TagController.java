package dev.rbq.sb.controller;

import dev.rbq.sb.dto.ApiResponse;
import dev.rbq.sb.dto.CreateTagRequest;
import dev.rbq.sb.dto.DeleteTagRequest;
import dev.rbq.sb.dto.TagResponse;
import dev.rbq.sb.dto.UpdateTagRequest;
import dev.rbq.sb.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签控制器
 */
@RestController
@RequestMapping("/api/tags")
@Tag(name = "标签管理", description = "标签管理相关接口，包括创建、更新、删除和查询标签")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 创建标签（仅管理员及以上）
     */
    @PostMapping("/create")
    @Operation(summary = "创建标签", description = "创建新标签（需要管理员及以上权限）")
    @SecurityRequirement(name = "session")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "标签创建成功",
                    content = @Content(schema = @Schema(implementation = TagResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "请求参数错误或标签已存在"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "权限不足"
            )
    })
    public ApiResponse<TagResponse> createTag(
            @Parameter(description = "创建标签请求", required = true)
            @Valid @RequestBody CreateTagRequest request,
            @Parameter(hidden = true) HttpSession session) {
        TagResponse tag = tagService.createTag(request.getId(), request, session);
        return ApiResponse.success("标签创建成功", tag);
    }

    /**
     * 更新标签（仅管理员及以上）
     */
    @PostMapping
    @Operation(summary = "更新标签", description = "更新现有标签信息（需要管理员及以上权限）")
    @SecurityRequirement(name = "session")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "标签更新成功",
                    content = @Content(schema = @Schema(implementation = TagResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "请求参数错误"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "权限不足"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "标签不存在"
            )
    })
    public ApiResponse<TagResponse> updateTag(
            @Parameter(description = "更新标签请求", required = true)
            @Valid @RequestBody UpdateTagRequest request,
            @Parameter(hidden = true) HttpSession session) {
        TagResponse tag = tagService.updateTag(request.getId(), request, session);
        return ApiResponse.success("标签更新成功", tag);
    }

    /**
     * 删除标签（仅管理员及以上）
     */
    @PostMapping("/delete")
    @Operation(summary = "删除标签", description = "删除指定标签（需要管理员及以上权限）")
    @SecurityRequirement(name = "session")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "标签删除成功"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "权限不足"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "标签不存在"
            )
    })
    public ApiResponse<Void> deleteTag(
            @Parameter(description = "删除标签请求", required = true)
            @Valid @RequestBody DeleteTagRequest request,
            @Parameter(hidden = true) HttpSession session) {
        tagService.deleteTag(request.getId(), session);
        return ApiResponse.success("标签删除成功", null);
    }


    /**
     * 获取所有标签（公开）
     */
    @GetMapping
    @Operation(summary = "获取所有标签", description = "获取系统中所有标签的列表（公开接口）")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "获取成功",
                    content = @Content(schema = @Schema(implementation = TagResponse.class))
            )
    })
    public ApiResponse<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ApiResponse.success(tags);
    }
}

