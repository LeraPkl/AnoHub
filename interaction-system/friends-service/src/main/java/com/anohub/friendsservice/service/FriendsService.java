package com.anohub.friendsservice.service;

import com.anohub.friendsservice.exceptions.FriendsAlreadyExistsException;
import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final TransactionalOperator transactionalOperator;
    private final FriendsRepository friendsRepository;

    public Mono<Friends> sendFriendRequest(String senderId, String receiverId) {
        return Mono.zip(
                        friendsRepository.existsByUser1IdAndUser2Id(senderId, receiverId).filter(exists -> !exists),
                        friendsRepository.existsByUser1IdAndUser2Id(receiverId, senderId).filter(exists -> !exists)
                )
                .filter(bothEmpty -> bothEmpty.getT1() && bothEmpty.getT2())
                .switchIfEmpty(Mono.error(new FriendsAlreadyExistsException(senderId, receiverId)))
                .then(Mono.defer(() -> {
                    Friends friendRequest = Friends.builder()
                            .user1Id(senderId)
                            .user2Id(receiverId)
                            .build();
                    return friendsRepository.save(friendRequest);
                }))
                .as(transactionalOperator::transactional);
    }
}
