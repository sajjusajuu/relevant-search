package com.swiggy.search.datalayer.knowledgeGraph;

import org.apache.commons.collections4.CollectionUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class KnowlegdeGraph {

    private Graph<String, DefaultWeightedEdge> graph;
    private RelevanceCalculator relevanceCalculator;

    public KnowlegdeGraph(RelevanceCalculator relevanceCalculator) {
        this.relevanceCalculator = relevanceCalculator;
        graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void addNode(String tag) {
        Set<String> vertexSet = graph.vertexSet();
        if (CollectionUtils.isNotEmpty(vertexSet)) {
            vertexSet.forEach(vertex -> {
                int relevancyScore = relevanceCalculator.calculateRelevancyScore(tag, vertex);
                graph.addVertex(tag);
                if(relevancyScore > 10) {
                    graph.addEdge(tag, vertex);
                }

            });
        }


    }

    public List<String> getNearestNodes(String tag,  int n) {

        List<String> relevantTags = new ArrayList<>();
        String start = graph
                .vertexSet().stream().filter(vertex -> vertex.equals(tag)).findAny()
                .get();

        Iterator<String> iterator = new BreadthFirstIterator<>(graph, start);
        while(iterator.hasNext() && n-- > 0) {
            relevantTags.add(iterator.next());
        }
        return relevantTags;

    }

    public List<String> getTopNearestNodes(List<String> tags, int n) {
        List<String> relevantTags = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> {
                relevantTags.addAll(getNearestNodes(tag, (int) Math.ceil( n/tags.size())));
            });
        }
        return relevantTags;
    }


}
