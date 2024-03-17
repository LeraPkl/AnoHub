package com.anohub.usermodelservice.service;

import com.anohub.usermodelservice.exceptions.FriendsAlreadyExistsException;
import com.anohub.usermodelservice.model.Friends;
import com.anohub.usermodelservice.repository.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;

    public Mono<Friends> sendFriendRequest(Long senderId, Long receiverId) {
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
                }));
    }
}
