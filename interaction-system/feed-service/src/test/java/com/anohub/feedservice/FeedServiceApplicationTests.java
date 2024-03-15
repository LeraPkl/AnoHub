package com.anohub.feedservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FeedServiceApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> {
            FeedServiceApplication.main(new String[]{});
        });
    }

}
