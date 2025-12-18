package dev.rbq.sb.service;

import dev.rbq.sb.dto.CreateTagRequest;
import dev.rbq.sb.dto.TagResponse;
import dev.rbq.sb.dto.UpdateTagRequest;
import dev.rbq.sb.entity.Tag;
import dev.rbq.sb.enums.Role;
import dev.rbq.sb.repository.TagRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 标签服务
 */
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * 检查是否为管理员或超级管理员
     *
     * @param session HTTP会话
     * @throws IllegalStateException 如果未登录或权限不足
     */
    private void checkAdminPermission(HttpSession session) {
        String roleStr = (String) session.getAttribute("role");
        if (roleStr == null) {
            throw new IllegalStateException("未登录");
        }

        Role role = Role.valueOf(roleStr);
        if (role != Role.ADMIN && role != Role.SUPER_ADMIN) {
            throw new IllegalStateException("权限不足：仅管理员及以上可操作");
        }
    }

    /**
     * 创建标签
     *
     * @param id      指定的标签ID
     * @param request 创建标签请求
     * @param session HTTP会话
     * @return 标签响应
     */
    @Transactional
    public TagResponse createTag(UUID id, CreateTagRequest request, HttpSession session) {
        // 检查权限
        checkAdminPermission(session);

        // 检查ID是否已存在
        if (tagRepository.existsById(id)) {
            throw new IllegalArgumentException("标签ID已存在");
        }

        // 检查标签值是否已存在
        if (tagRepository.existsByTag(request.getTag())) {
            throw new IllegalArgumentException("标签值已存在");
        }

        // 创建标签
        Tag tag = new Tag();
        tag.setId(id);
        tag.setTag(request.getTag());

        tag = tagRepository.save(tag);

        return convertToResponse(tag);
    }

    /**
     * 更新标签
     *
     * @param id      标签ID
     * @param request 更新标签请求
     * @param session HTTP会话
     * @return 标签响应
     */
    @Transactional
    public TagResponse updateTag(UUID id, UpdateTagRequest request, HttpSession session) {
        // 检查权限
        checkAdminPermission(session);

        // 查找标签
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("标签不存在"));

        // 检查新标签值是否与其他标签冲突
        if (!tag.getTag().equals(request.getTag()) && tagRepository.existsByTag(request.getTag())) {
            throw new IllegalArgumentException("标签值已存在");
        }

        // 更新标签值
        tag.setTag(request.getTag());

        tag = tagRepository.save(tag);

        return convertToResponse(tag);
    }

    /**
     * 删除标签
     *
     * @param id      标签ID
     * @param session HTTP会话
     */
    @Transactional
    public void deleteTag(UUID id, HttpSession session) {
        // 检查权限
        checkAdminPermission(session);

        // 检查标签是否存在
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("标签不存在");
        }

        tagRepository.deleteById(id);
    }

    /**
     * 根据ID获取标签
     *
     * @param id 标签ID
     * @return 标签响应
     */
    public TagResponse getTagById(UUID id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("标签不存在"));

        return convertToResponse(tag);
    }

    /**
     * 获取所有标签
     *
     * @return 标签响应列表
     */
    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 转换为响应对象
     *
     * @param tag 标签实体
     * @return 标签响应
     */
    private TagResponse convertToResponse(Tag tag) {
        TagResponse response = new TagResponse();
        response.setId(tag.getId());
        response.setTag(tag.getTag());
        return response;
    }
}
