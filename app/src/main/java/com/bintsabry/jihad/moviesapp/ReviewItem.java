package com.bintsabry.jihad.moviesapp;

import java.io.Serializable;

/**
 * Created by JihadSabry on 8/12/2016.
 */
public class ReviewItem implements Serializable{

    private String id;
    private String author;
    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }





}
