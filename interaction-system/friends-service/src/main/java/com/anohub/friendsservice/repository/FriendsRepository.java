package com.anohub.friendsservice.repository;

import com.anohub.friendsservice.model.Friends;
import com.anohub.friendsservice.model.FriendsPK;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface FriendsRepository extends ReactiveCrudRepository<Friends, FriendsPK> {

    @Query("SELECT * FROM friends WHERE user1_id = :#{#friendsPK.user1Id} AND user2_id = :#{#friendsPK.user2Id}")
    @Override
    Mono<Friends> findById(FriendsPK friendsPK);

    @Query("INSERT INTO friends (user1_id, user2_id, is_accepted) " +
            "VALUES (:#{#friends.user1Id}, :#{#friends.user2Id}, :#{#friends.isAccepted}) " +
            "ON CONFLICT (user1_id, user2_id) " +
            "DO UPDATE SET is_accepted = :#{#friends.isAccepted}")
    @Override
    Mono<Friends> save(Friends friends);
}
