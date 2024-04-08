package com.anohub.friendsservice.service;

import com.anohub.friendsservice.exceptions.FriendsAlreadyExistsException;
import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.repository.FriendsRepository;
import com.anohub.interactioncommon.event.FriendRequestAcceptedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendsService {

    private final TransactionalOperator transactionalOperator;
    private final FriendsRepository friendsRepository;
    private final KafkaTemplate<String, FriendRequestAcceptedEvent> kafkaTemplate;

    @Value("${kafka.topics.friend-request-accepted}")
    private String friendRequestAcceptedTopic;

    public Mono<Friends> sendFriendRequest(String senderId, String receiverId) {

        String compositeId = getCompositeId(senderId, receiverId);

        return friendsRepository.existsById(compositeId)
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new FriendsAlreadyExistsException(senderId, receiverId)))
                .then(Mono.defer(() -> {
                    Friends friendRequest = new Friends();
                    friendRequest.setId(senderId, receiverId);
                    friendRequest.setIsAccepted(false);
                    return friendsRepository.save(friendRequest);
                })).as(transactionalOperator::transactional);
    }

    public Mono<Friends> acceptFriendRequest(String senderId, String receiverId) {
        String compositeId = getCompositeId(senderId, receiverId);

        return friendsRepository.findById(compositeId)
                .flatMap(friendRequest -> {
                    friendRequest.setIsAccepted(true);
                    return friendsRepository.save(friendRequest)
                            .doOnSuccess(savedFriendRequest -> {
                                FriendRequestAcceptedEvent event =
                                        new FriendRequestAcceptedEvent(senderId, receiverId);
                                kafkaTemplate.send(friendRequestAcceptedTopic, event);
                            });
                })
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> declineFriendRequestAcceptance(String senderId, String receiverId) {

        String compositeId = getCompositeId(senderId, receiverId);

        return friendsRepository.findById(compositeId)
                .flatMap(friendRequest -> friendsRepository.deleteById(compositeId))
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> removeFriend(String senderId, String receiverId) {
        String compositeId = getCompositeId(senderId, receiverId);
        return friendsRepository.deleteById(compositeId);
    }

    private String getCompositeId(String userId1, String userId2) {
        String id;
        if (userId1.compareTo(userId2) < 0) {
            id = userId1 + '_' + userId2;
        } else {
            id = userId2 + '_' + userId1;
        }
        return id;
    }
}
