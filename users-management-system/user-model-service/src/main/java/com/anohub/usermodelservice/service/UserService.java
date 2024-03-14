package com.anohub.usermodelservice.service;

import com.anohub.usermodelservice.model.User;
import com.anohub.usermodelservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Mono<User> createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepository.save(existingUser);
                });
    }

    public Mono<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
}

