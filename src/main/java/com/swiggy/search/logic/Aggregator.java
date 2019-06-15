package com.swiggy.search.logic;

import com.swiggy.search.api.ExploreResponse;
import com.swiggy.search.api.SongDoc;
import com.swiggy.search.datalayer.index.Index;
import com.swiggy.search.datalayer.knowledgeGraph.KnowlegdeGraph;
import com.swiggy.search.datalayer.knowledgeGraph.RelevanceCalculator;
import org.apache.lucene.queryParser.ParseException;

import java.io.IOException;
import java.util.List;

public class Aggregator {


    private Index index;
    private KnowlegdeGraph knowlegdeGraph;


    public Aggregator() throws IOException {
        index = new Index();
        knowlegdeGraph = new KnowlegdeGraph(new RelevanceCalculator());
    }

    public ExploreResponse aggreateExplore(List<String> queries) throws IOException, ParseException {
        ExploreResponse response = new ExploreResponse();
        List<String> results = index.search(queries.toString());
        response.setSongDocs(results);
        if(queries.size() < 3) {
            List<String> topNearestNodes = knowlegdeGraph.getTopNearestNodes(queries, 10);
            response.setTags(topNearestNodes);
        }
        return response;
    }

    public void createDoc(SongDoc songDoc) throws IOException, IllegalAccessException {
        index.index(songDoc);
        songDoc.getTags().forEach(tag -> {
            knowlegdeGraph.addNode(tag);
        });

    }

    public void  createDocs(List<SongDoc> songDocs) throws IOException, IllegalAccessException {
        index.indexBatch(songDocs);
        songDocs.forEach(songDoc -> {
            songDoc.getTags().forEach(tag -> {
                knowlegdeGraph.addNode(tag);
            });
        });
    }



}
