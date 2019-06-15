package com.swiggy.search.api;

import com.swiggy.search.logic.Aggregator;
import org.apache.lucene.queryParser.ParseException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


@Path("8tracks")
@Produces(MediaType.APPLICATION_JSON)
public class Api {


    private Aggregator aggregator;


    public Api() throws IOException {
        aggregator = new Aggregator();
    }

    @POST
    @Path("create")
    public Response createSong(SongDoc songDoc) throws IOException, IllegalAccessException {
        aggregator.createDoc(songDoc);
        return Response.ok().build();
    }

    @POST
    @Path("createBatch")
    public Response createSong(List<SongDoc> songDoc) throws IOException, IllegalAccessException {
        aggregator.createDocs(songDoc);
        return Response.ok().build();
    }

    @POST
    @Path("update")
    public Response updateSong(SongDoc songDoc) {
        return null;
    }
    @POST
    @Path("delete")
    public Response deleteSong(SongDoc songDoc) {
        return null;
    }

    @POST
    @Path("delete")
    public ExploreResponse explore(List<String> queries) throws IOException, ParseException {
        ExploreResponse response = aggregator.aggreateExplore(queries);
        return response;
    }

}
