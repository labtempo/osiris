package com.rest.gson;

import java.util.List;
import com.google.gson.*;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main(String... args) throws Exception {
        String json
                = "{"
                + "'title': 'Computing and Information systems',"
                + "'id' : 1,"
                + "'children' : 'true',"
                + "'groups' : [{"
                + "'title' : 'Level one CIS',"
                + "'id' : 2,"
                + "'children' : 'true',"
                + "'groups' : [{"
                + "'title' : 'Intro To Computing and Internet',"
                + "'id' : 3,"
                + "'children': 'false',"
                + "'groups':[]"
                + "}]"
                + "}]"
                + "}";

        // Now do the magic.
        Data data = new Gson().fromJson(json, Data.class);

        // Show it.
        System.out.println("Imprimindo...");
        System.out.println(data);
    }
    
}

class Data {

    private String title;
    private Long id;
    private Boolean children;
    private List<Data> groups;
    
    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public Boolean getChildren() {
        return children;
    }

    public List<Data> getGroups() {
        return groups;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChildren(Boolean children) {
        this.children = children;
    }

    public void setGroups(List<Data> groups) {
        this.groups = groups;
    }
    
    public String toString() {
        return String.format("title:%s,id:%d,children:%s,groups:%s", title, id, children, groups);
    }
}
