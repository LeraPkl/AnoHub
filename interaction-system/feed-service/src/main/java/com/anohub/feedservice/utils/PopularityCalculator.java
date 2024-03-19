package com.anohub.feedservice.utils;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PopularityCalculator {

    private static final double COEFFICIENT = 8640000.0;
    private static final int LIKE_WEIGHT = 1;
    private static final double DISLIKE_WEIGHT = 0.5;
    private static final int COMMENT_WEIGHT = 2;

    public double calculate(long likes,
                            long dislikes,
                            long comments,
                            LocalDateTime createdAt) {

        long timeDiff = (Duration.between(createdAt, LocalDateTime.now())).toSeconds();
        double timeDecay = 1.0 / (1.0 + Math.log(1.0 + timeDiff / COEFFICIENT));
        return timeDecay / (likes * LIKE_WEIGHT + dislikes * DISLIKE_WEIGHT + comments * COMMENT_WEIGHT);
    }

}
