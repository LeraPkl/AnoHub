package com.anohub.feedservice.service;

import com.anohub.feedservice.client.UserClient;
import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.repository.PostRepository;
import com.anohub.feedservice.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserClient userClient;

    public Mono<Post> createPost(Post post) {
        return postRepository.save(post);
    }

    public Mono<Post> getPost(String id) {
        return postRepository.findById(id)
                .flatMap(p -> userClient.getUser(p.getUserId())
                        .flatMap(u -> {
                            p.setUser(u);
                            return topicRepository.findById(p.getTopicId());
                        })
                        .flatMap(t -> {
                            p.setTopic(t);
                            return Mono.just(p);
                        })
                );
    }

    public Flux<Post> getAllPosts() {
        return postRepository.findAll()
                .flatMap(p -> userClient.getUser(p.getUserId())
                        .flatMap(u -> {
                            p.setUser(u);
                            return topicRepository.findById(p.getTopicId());
                        })
                        .flatMap(t -> {
                            p.setTopic(t);
                            return Mono.just(p);

                        }));
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

