package com.salesforce.freemind.salesforce101.data;

/**
 * Created by anant on 2017-09-21.
 */

public class ReviewsData {

    private String review_author;
    private String review_content;
    private String review_id;

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    private String movie_id;

    public String getReview_author() {
        return review_author;
    }

    public String getReview_content() {
        return review_content;
    }

    public void setReview_author(String review_author) {
        this.review_author = review_author;
    }

    public void setReview_content(String review_content) {
        this.review_content = review_content;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public String getReview_id() {

        return review_id;
    }
}
