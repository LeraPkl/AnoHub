package com.anohub.feedservice.service;

import com.anohub.feedservice.client.UserClient;
import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.model.dto.RepostedPostDto;
import com.anohub.feedservice.model.dto.UserDto;
import com.anohub.feedservice.repository.RepostedPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepostedPostService {

    private final RepostedPostRepository repostedPostRepository;
    private final UserClient userClient;
    private final PostService postService;
    private final PostLikeService postLikeService;

    public Flux<RepostedPostDto> getRepostedPostsByUser(String userId) {
        return repostedPostRepository.findByUserProfileId(userId)
                .flatMap(repostedPost -> {
                    Mono<UserDto> userMono = userClient.getUser(userId);
                    Mono<Post> postMono = postService.getPost(repostedPost.getPostId());
                    Mono<Long> likesMono = postLikeService.countLikesByPostId(repostedPost.getPostId());
                    Mono<Long> dislikesMono = postLikeService.countDislikesByPostId(repostedPost.getPostId());

                    return Mono.zip(userMono, postMono, likesMono, dislikesMono)
                            .map(tuple -> {
                                UserDto user = tuple.getT1();
                                Post post = tuple.getT2();
                                Long likes = tuple.getT3();
                                Long dislikes = tuple.getT4();

                                return new RepostedPostDto(
                                        post.getId(),
                                        post.getContent(),
                                        user.linkToPfp(),
                                        likes,
                                        dislikes
                                );
                            });
                });
    }

}
