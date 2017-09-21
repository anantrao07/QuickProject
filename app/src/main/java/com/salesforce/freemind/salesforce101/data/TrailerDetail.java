package com.salesforce.freemind.salesforce101.data;

/**
 * Created by anant on 2017-09-21.
 */

public class TrailerDetail {
    private  String key;
    private String name;
    private  String site;
    private  String trailer_id;
    private String movie_id ;

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setTrailer_id(String trailer_id) {
        this.trailer_id = trailer_id;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getTrailer_id() {
        return trailer_id;
    }
}
