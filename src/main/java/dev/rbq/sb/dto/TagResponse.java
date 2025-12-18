package dev.rbq.sb.dto;

import java.util.UUID;

/**
 * 标签响应
 */
public class TagResponse {

    private UUID id;
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

