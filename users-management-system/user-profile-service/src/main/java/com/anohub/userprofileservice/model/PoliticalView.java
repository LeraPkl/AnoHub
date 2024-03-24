package com.anohub.userprofileservice.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "political_view")
public class PoliticalView {

    @Id
    private Integer id;

    private String name;

}