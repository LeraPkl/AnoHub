package com.anohub.feedservice.service;

import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Mono<Post> createPost(Post post) {
        return postRepository.save(post);
    }

    public Mono<Post> getPost(String id) {
        return postRepository.findById(id);
    }

    public Flux<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Mono<Post> updatePost(Post post) {
        return postRepository.findById(post.getId())
                .flatMap(dbPost -> {
                    dbPost.setTopic(post.getTopic());
                    dbPost.setUserId(post.getUserId());
                    dbPost.setContent(post.getContent());
                    dbPost.setUser(post.getUser());
                    return postRepository.save(dbPost);
                });
    }

    public Mono<Void> deletePost(String id) {
        return postRepository.deleteById(id);
    }

}

