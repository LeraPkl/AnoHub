package com.anohub.feedservice.service;

import com.anohub.feedservice.model.Comment;
import com.anohub.feedservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Mono<Comment> createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Mono<Comment> updateComment(String id, Comment updatedComment) {
        updatedComment.setId(id);
        return commentRepository.save(updatedComment);
    }

    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }
    public Flux<Comment> getAllCommentsWithReplies(String postId, Pageable pageable) {
        return commentRepository.findAllByPostId(postId, pageable)
                .flatMap(comment -> {
                    Flux<Comment> replies = commentRepository.findAllByCommentId(comment.getId());
                    return replies.collectList().map(list -> {
                        comment.setReplies(list);
                        return comment;
                    });
                });
    }

}
