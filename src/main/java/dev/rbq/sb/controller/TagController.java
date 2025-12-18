package dev.rbq.sb.controller;

import dev.rbq.sb.dto.ApiResponse;
import dev.rbq.sb.dto.CreateTagRequest;
import dev.rbq.sb.dto.DeleteTagRequest;
import dev.rbq.sb.dto.TagResponse;
import dev.rbq.sb.dto.UpdateTagRequest;
import dev.rbq.sb.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 创建标签（仅管理员及以上）
     */
    @PostMapping("/create")
    public ApiResponse<TagResponse> createTag(
            @Valid @RequestBody CreateTagRequest request,
            HttpSession session) {
        TagResponse tag = tagService.createTag(request.getId(), request, session);
        return ApiResponse.success("标签创建成功", tag);
    }

    /**
     * 更新标签（仅管理员及以上）
     */
    @PostMapping("/update")
    public ApiResponse<TagResponse> updateTag(
            @Valid @RequestBody UpdateTagRequest request,
            HttpSession session) {
        TagResponse tag = tagService.updateTag(request.getId(), request, session);
        return ApiResponse.success("标签更新成功", tag);
    }

    /**
     * 删除标签（仅管理员及以上）
     */
    @PostMapping("/delete")
    public ApiResponse<Void> deleteTag(
            @Valid @RequestBody DeleteTagRequest request,
            HttpSession session) {
        tagService.deleteTag(request.getId(), session);
        return ApiResponse.success("标签删除成功", null);
    }


    /**
     * 获取所有标签（公开）
     */
    @GetMapping
    public ApiResponse<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAllTags();
        return ApiResponse.success(tags);
    }
}

