package com.anohub.userprofileservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Table("user_profile")
public class UserProfile {

    @PrimaryKey
    private UUID id;

    private String bio;

    private RelationshipStatus relationship;

    private List<String> interests = new ArrayList<>();

    @Column("fav_music")
    private List<String> favMusic = new ArrayList<>();

    @Column("fav_movies")
    private List<String> favMovies = new ArrayList<>();

    @Column("fav_games")
    private List<String> favGames = new ArrayList<>();

    @Column("fav_quotes")
    private List<String> favQuotes = new ArrayList<>();

    @Column("inspired_by")
    private List<String> inspiredBy = new ArrayList<>();

    @Column("important_in_others")
    private List<String> importantInOthers = new ArrayList<>();

    @Column("personal_priority")
    private List<String> personalPriority = new ArrayList<>();

    private List<String> hobbies = new ArrayList<>();

    @Column("political_views_id")
    private UUID politicalViewsId;

}

