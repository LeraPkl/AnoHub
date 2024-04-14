package com.anohub.feedservice.controller;

import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed-app/api/v1/posts/poll")
public class PollController {

    private final PollService pollService;

    @PutMapping("/{postId}/vote")
    public Mono<ResponseEntity<Post>> vote(@PathVariable String postId,
                                           @RequestParam String userId,
                                           @RequestParam String optionContent) {
        return pollService.vote(postId, userId, optionContent)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{postId}/cancel-vote")
    public Mono<ResponseEntity<Post>> cancelVote(@PathVariable String postId,
                                                 @RequestParam String userId,
                                                 @RequestParam String optionContent) {
        return pollService.cancelVote(postId, userId, optionContent)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
