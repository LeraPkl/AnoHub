package com.anohub.userprofileservice.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table("user_profile")
public class UserProfile {

    @Id
    private Long id;

    @NotBlank
    private String userId;

    private String bio;

    private RelationshipStatus relationship;

    private List<String> interests = new ArrayList<>();

    private List<String> favMusic = new ArrayList<>();

    private List<String> favMovies = new ArrayList<>();

    private List<String> favGames = new ArrayList<>();

    private List<String> favQuotes = new ArrayList<>();

    private List<String> inspiredBy = new ArrayList<>();

    private List<String> importantInOthers = new ArrayList<>();

    private List<String> personalPriority = new ArrayList<>();

    private List<String> hobbies = new ArrayList<>();

    private Attitude viewsOnAlcohol;

    private Attitude viewsOnSmoking;

    private Integer politicalViews;

}

