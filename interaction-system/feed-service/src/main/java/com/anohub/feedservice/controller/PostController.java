package com.anohub.feedservice.controller;

import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed-app/api/v1/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Post> createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Post> getPost(@PathVariable String id) {
        return postService.getPost(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Page<Post>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return postService.findPostsPage(pageable);
    }

    @GetMapping("/page/popularity")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Page<Post>> findAllPageByPopularity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return postService.findAllPageByPopularity(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Post> updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePost(@PathVariable String id) {
        return postService.deletePost(id);
    }
}



