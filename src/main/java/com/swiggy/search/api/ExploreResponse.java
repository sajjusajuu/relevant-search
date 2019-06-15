package com.swiggy.search.api;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class ExploreResponse {

    private List<String> tags;
    private List<String> songDocs;
}
