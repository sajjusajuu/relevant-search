package com.swiggy.search.api;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class SongDoc {

    private String id;
    private String title;
    private List<String> artist;
    private String genre;
    private List<String> tags;
}
