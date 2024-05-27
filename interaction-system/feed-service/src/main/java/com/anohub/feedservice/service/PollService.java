package com.anohub.feedservice.service;

import com.anohub.feedservice.exception.PollEndedException;
import com.anohub.feedservice.model.Post;
import com.anohub.feedservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PollService {

    private final PostRepository postRepository;

    public Mono<Post> vote(String postId, String userId, String optionContent) {
        return validatePoll(postId)
                .flatMap(post -> {
                    var options = post.getPoll().getOptions();
                    options.stream()
                            .filter(option -> option.getContent().equals(optionContent))
                            .findFirst()
                            .ifPresent(option -> {
                                boolean hasVoted = options.stream()
                                        .anyMatch(o -> o.getVoterIds().contains(userId));
                                if (!hasVoted) {
                                    option.getVoterIds().add(userId);
                                } else {
                                    post.getPoll()
                                            .getOptions()
                                            .forEach(o -> o.getVoterIds()
                                                    .removeIf(id -> id.equals(userId)));
                                }
                            });
                    return postRepository.save(post);
                });
    }

    public Mono<Post> cancelVote(String postId, String userId, String optionContent) {
        return validatePoll(postId)
                .flatMap(post -> {
                    post.getPoll().getOptions().stream()
                            .filter(option -> option.getContent().equals(optionContent))
                            .findFirst()
                            .ifPresent(option -> option.getVoterIds().remove(userId));
                    return postRepository.save(post);
                });
    }

    private Mono<Post> validatePoll(String postId) {
        return postRepository.findById(postId)
                .flatMap(post -> {
                    if (post.getPoll() == null) {
                        return Mono.error(new IllegalStateException("Poll not found"));
                    }
                    if (post.getPoll().getIsEnded()) {
                        return Mono.error(new PollEndedException("Poll has ended"));
                    }
                    return Mono.just(post);
                });
    }
}

