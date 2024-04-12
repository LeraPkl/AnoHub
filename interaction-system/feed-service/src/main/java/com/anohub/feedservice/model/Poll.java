package com.anohub.feedservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Poll {

    private Boolean isEnded;
    private List<Option> options;

    @Getter
    @Setter
    public static class Option {
        private String content;
        private Integer voteCount;
    }
}
