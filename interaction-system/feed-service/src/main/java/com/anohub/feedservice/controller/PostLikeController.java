package com.anohub.feedservice.controller;

import com.anohub.feedservice.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed-app/api/v1/post-likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PutMapping("/{postId}/like")
    public Mono<?> toggleLike(@PathVariable String postId, @RequestParam Long userId) {
        return postLikeService.toggleLike(postId, userId);
    }

    @PutMapping("/{postId}/dislike")
    public Mono<?> toggleDislike(@PathVariable String postId, @RequestParam Long userId) {
        return postLikeService.toggleDislike(postId, userId);
    }
}
