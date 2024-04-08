package com.anohub.friendsservice.repository;

import com.anohub.friendsservice.model.Friends;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FriendsRepository extends ReactiveCrudRepository<Friends, String> {
}
