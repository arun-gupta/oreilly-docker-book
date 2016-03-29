package org.couchbase.sample.javaee;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.JsonLongDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import static com.couchbase.client.java.query.dsl.Expression.i;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.Select.select;

/**
 * @author Arun Gupta
 */
@Path("book")
public class BookResource {

    @Inject Database database;
    private static final String BOOK_KEY_PREFIX = "book_";
        
    @GET
    public String getAll() {
        N1qlQuery query = N1qlQuery
                .simple(select("*")
                .from(i(database.getBucket().name()))
                .limit(10));
//        N1qlQuery query = N1qlQuery.simple("SELECT * FROM `books` LIMIT 10");
        System.out.println(query.statement().toString());
        N1qlQueryResult result = database.getBucket().query(query);
        System.err.println(result.errors());
        System.out.println(result.toString());
        return result.allRows().toString();
    }
    
    @GET
    @Path("{id}")
    public String getAirline(@PathParam("id") String id) {
//        N1qlQuery query = N1qlQuery
//                .simple(select("*")
//                .from(i(database.getBucket().name()))
//                .where(x("id").eq(id)));
//        N1qlQuery query = N1qlQuery.simple("SELECT * from `books` WHERE id = " + id);
        
        N1qlQuery query = N1qlQuery.simple("SELECT * from `books` USE KEYS [\"" + BOOK_KEY_PREFIX + id + "\"]");
        System.out.println(query.statement().toString());        
        N1qlQueryResult result = database.getBucket().query(query);
        if (result.finalSuccess() && !result.allRows().isEmpty()) {
            return result.allRows().get(0).toString();
        }

        return null;
    }
    
    @POST
    @Consumes("application/json")
    public String addAirline(BookBean airline) throws JsonProcessingException {
        System.out.println("POST: " + airline.toString());
        JsonLongDocument id = database.getBucket().counter("books_sequence", 1);

        JsonDocument document = database.getBucket().insert(BookBean.toJson(airline, id.content()));
        return document.content().toString();
    }

    @PUT
    @Path("{id}")
    public String updateAirline(BookBean airline) throws JsonProcessingException {
        System.out.println("PUT: " + airline.toString());
        JsonDocument document = database.getBucket().replace(BookBean.toJson(airline));
        
        return document.content().toString();
    }
    
    @DELETE
    @Path("{id}")
    public String delete(@PathParam("id")String id) {
        String airline = getAirline(id);
        System.out.println("DELETE: " + airline);
        database.getBucket().remove(BOOK_KEY_PREFIX + id);
        
        return airline;
    }
}
