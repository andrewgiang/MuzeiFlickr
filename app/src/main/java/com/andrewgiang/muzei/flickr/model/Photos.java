package com.andrewgiang.muzei.flickr.model;

import java.util.List;

/**
 * Created by andrewgiang on 2/12/14.
 */
public class Photos {
    int page;
    int pages;
    int perpage;
    String total;

    public List<Photo> photo;

    @Override
    public String toString() {
        return "Photos{" +
                "page=" + page +
                ", pages=" + pages +
                ", perpage=" + perpage +
                ", total='" + total + '\'' +
                ", photo=" + photo +
                '}';
    }
}
