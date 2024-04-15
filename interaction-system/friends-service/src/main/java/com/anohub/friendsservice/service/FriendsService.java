package com.anohub.friendsservice.service;

import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.model.FriendsPK;
import com.anohub.friendsservice.repository.FriendsRepository;
import com.anohub.interactioncommon.event.FriendRequestAcceptedEvent;
import com.anohub.interactioncommon.event.NotificationEvent;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendsService {

    private final TransactionalOperator transactionalOperator;
    private final FriendsRepository friendsRepository;
    private final KafkaTemplate<String, FriendRequestAcceptedEvent> friendRequestKafkaTemplate;
    private final KafkaTemplate<String, NotificationEvent> notificationKafkaTemplate;
    private final MessageSource messageSource;

    @Value("${kafka.topics.friend-request-accepted}")
    private String friendRequestAcceptedTopic;

    @Value("${kafka.topics.send-notification}")
    private String sendNotificationTopic;

    public Mono<Friends> sendFriendRequest(String senderId, String receiverId, @Nullable String message) {

        FriendsPK compositeId = getCompositeId(senderId, receiverId);
        log.info("compositeId: {}", compositeId);

        return friendsRepository.findById(compositeId)
                .switchIfEmpty(Mono.defer(() -> {
                    Friends friendRequest = new Friends();
                    friendRequest.setId(compositeId);
                    friendRequest.setIsAccepted(false);
                    return friendsRepository.save(friendRequest)
                            .doOnSuccess(savedFriendRequest -> {
                                NotificationEvent event =
                                        new NotificationEvent(receiverId, getNotificationContent(message));
                                notificationKafkaTemplate.send(sendNotificationTopic, event);
                            });
                }))
                .as(transactionalOperator::transactional);
    }


    public Mono<Friends> acceptFriendRequest(String senderId, String receiverId) {

        return friendsRepository.findById(getCompositeId(senderId, receiverId))
                .flatMap(friendRequest -> {
                    friendRequest.setIsAccepted(true);
                    return friendsRepository.save(friendRequest)
                            .doOnSuccess(savedFriendRequest -> {
                                FriendRequestAcceptedEvent event =
                                        new FriendRequestAcceptedEvent(senderId, receiverId);
                                friendRequestKafkaTemplate.send(friendRequestAcceptedTopic, event);
                            });
                })
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> declineFriendRequestAcceptance(String senderId, String receiverId) {

        var compositeId = getCompositeId(senderId, receiverId);

        return friendsRepository.findById(compositeId)
                .flatMap(friendRequest -> friendsRepository.deleteById(compositeId))
                .as(transactionalOperator::transactional);
    }

    public Mono<Void> removeFriend(String senderId, String receiverId) {
        var compositeId = getCompositeId(senderId, receiverId);
        return friendsRepository.deleteById(compositeId);
    }

    public String getNotificationContent(String message) {
        if (message == null) {
            return messageSource.getMessage("friendRequest.default", null, Locale.getDefault());
        } else {
            return messageSource.getMessage("friendRequest.withMessage", new Object[]{message}, Locale.getDefault());
        }
    }

    private FriendsPK getCompositeId(String userId1, String userId2) {
        return FriendsPK.builder()
                .user1Id(userId1.compareTo(userId2) < 0 ? userId1 : userId2)
                .user2Id(userId1.compareTo(userId2) < 0 ? userId2 : userId1)
                .build();
    }

}
