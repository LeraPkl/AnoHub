package com.anohub.feedservice.model.dto;

public record RepostedPostDto(String postId, String content, String linkToPfp, Long likes, Long dislikes) {
}
