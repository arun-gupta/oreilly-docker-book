package org.couchbase.sample.javaee;

import com.couchbase.client.deps.com.fasterxml.jackson.core.JsonProcessingException;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author arungupta
 * 
 */
public class BookBean {
    private String id;
    private String isbn;
    private String name;
    private float cost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    static JsonDocument toJson(BookBean bean, long counter) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        bean.setId(String.valueOf(counter));
        String json = mapper.writeValueAsString(bean);
        
        return JsonDocument.create("book_" + bean.getId(), JsonObject.fromJson(json));
    }
    
    static JsonDocument toJson(BookBean bean) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(bean);
        
        return JsonDocument.create("book_" + bean.getId(), JsonObject.fromJson(json));
    }
    
    static BookBean fromJson(JsonDocument json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json.content().toString(), BookBean.class);
    }
    
    @Override
    public String toString() {
        try {
            return toJson(this).content().toString();
        } catch (JsonProcessingException ex) {
            Logger.getLogger(BookBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
