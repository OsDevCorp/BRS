package com.misis.brs.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class News {
    private long dateNews;
    private String header;
    @NonNull
    @PrimaryKey()
    private String description;
    //TODO поле для хранения картинок

    public long getDateNews() {
        return dateNews;
    }

    public void setDateNews(long dateNews) {
        this.dateNews = dateNews;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
