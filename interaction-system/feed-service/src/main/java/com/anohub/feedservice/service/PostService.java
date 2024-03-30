package com.anohub.feedservice.service;

import com.anohub.feedservice.client.UserClient;
import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.repository.*;
import com.anohub.feedservice.utils.PopularityCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Comparator;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final TransactionalOperator transactionalOperator;
    private final PostSortingRepository postSortingRepository;
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserClient userClient;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final PopularityCalculator popularityCalculator;

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
                )
                .as(transactionalOperator::transactional);
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

                        }))
                .as(transactionalOperator::transactional);
    }

    public Mono<Post> updatePost(Post post) {
        return postRepository.findById(post.getId())
                .flatMap(dbPost -> {
                    dbPost.setTopic(post.getTopic());
                    dbPost.setUserId(post.getUserId());
                    dbPost.setContent(post.getContent());
                    dbPost.setUser(post.getUser());
                    return postRepository.save(dbPost);
                })
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> deletePost(String id) {
        return postRepository.deleteById(id);
    }

    public Mono<Page<Post>> findPostsPage(Pageable pageable) {
        return postSortingRepository.findAllBy(pageable)
                .as(transactionalOperator::transactional)
                .collectList()
                .map(list -> new PageImpl<>(list, pageable, list.size()));
    }

    public Mono<Page<Post>> findAllPageByPopularity(Pageable pageable) {
        return postRepository.findAll()
                .as(transactionalOperator::transactional)
                .flatMap(post -> Mono.zip(
                        postLikeRepository.countByPostIdAndIsLikeTrue(post.getId()),
                        postLikeRepository.countByPostIdAndIsLikeFalse(post.getId()),
                        commentRepository.countByPostId(post.getId()),
                        Mono.just(post)
                ))
                .map(tuple -> {
                    Long likes = tuple.getT1();
                    Long dislikes = tuple.getT2();
                    Long comments = tuple.getT3();
                    Post post = tuple.getT4();

                    double popularity = popularityCalculator.calculate(likes, dislikes, comments, post.getCreatedAt());
                    post.setPopularity(popularity);
                    return Tuples.of(popularity, post);
                })
                .collectList()
                .map(list -> {
                    list.sort(Comparator.comparing(Tuple2::getT1, Comparator.reverseOrder()));
                    int start = (int) pageable.getOffset();
                    int end = Math.min((start + pageable.getPageSize()), list.size());
                    return new PageImpl<>(list.subList(start, end).stream().map(Tuple2::getT2)
                            .collect(Collectors.toList()), pageable, list.size());
                });
    }
}


