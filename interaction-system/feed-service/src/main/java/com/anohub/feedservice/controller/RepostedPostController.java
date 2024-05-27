package com.anohub.feedservice.controller;

import com.anohub.feedservice.model.dto.RepostedPostDto;
import com.anohub.feedservice.service.RepostedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed-app/api/v1/reposted-posts")
public class RepostedPostController {

    private final RepostedPostService repostedPostService;
    
    @GetMapping("/user/{userId}")
    public Flux<RepostedPostDto> getRepostedPostsByUserId(@PathVariable String userId) {
        return repostedPostService.getRepostedPostsByUser(userId);
    }
}
