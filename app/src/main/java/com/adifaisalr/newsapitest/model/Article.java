package com.adifaisalr.newsapitest.model;

import com.adifaisalr.newsapitest.database.AppDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Adi Faisal Rahman on 7/17/2017.
 */

@Table(database = AppDatabase.class)
public class Article extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String sourceId;

    @SerializedName("author")
    @Expose
    @Column
    private String author;

    @SerializedName("title")
    @Expose
    @Column
    private String title;

    @SerializedName("description")
    @Expose
    @Column
    private String description;

    @SerializedName("url")
    @Expose
    @Column
    private String url;

    @SerializedName("urlToImage")
    @Expose
    @Column
    private String urlToImage;

    @SerializedName("publishedAt")
    @Expose
    @Column
    private String publishedAt;

    @Column
    private Calendar publishedAtCalendar;

    public Article() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Calendar getPublishedAtCalendar() {
        return publishedAtCalendar;
    }

    public void setPublishedAtCalendar(Calendar publishedAtCalendar) {
        this.publishedAtCalendar = publishedAtCalendar;
    }

    public static void deleteBySource(String sourceId) {
        SQLite.delete()
                .from(Article.class)
                .where(Article_Table.sourceId.eq(sourceId))
                .execute();
    }

    public static List<Article> getAllBySource(String sourceId) {
        List<Article> articles = (SQLite.select()
                .from(Article.class)
                .where(Article_Table.sourceId.eq(sourceId))
                .queryList());

        return articles;
    }

    public static List<Article> getByTitleKeyword(String sourceId, String keyword) {
        List<Article> articles = new ArrayList<>();

        articles.addAll(SQLite.select()
                .from(Article.class)
                .where(Article_Table.sourceId.eq(sourceId))
                .and(Article_Table.title.like("%" + keyword + "%"))
                .queryList());

        return articles;
    }
}
