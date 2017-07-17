package com.adifaisalr.tokopediatest.model;

import com.adifaisalr.tokopediatest.database.AppDatabase;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = AppDatabase.class)
public class Source extends BaseModel{

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @SerializedName("name")
    @Expose
    @Column
    private String name;

    @SerializedName("description")
    @Expose
    @Column
    private String description;

    @SerializedName("url")
    @Expose
    @Column
    private String url;

    @SerializedName("category")
    @Expose
    @Column
    private String category;

    @SerializedName("language")
    @Expose
    @Column
    private String language;

    @SerializedName("country")
    @Expose
    @Column
    private String country;

    @SerializedName("urlsToLogos")
    @Expose
    private UrlsToLogos urlsToLogos;

    @SerializedName("sortBysAvailable")
    @Expose
    private List<String> sortBysAvailable = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UrlsToLogos getUrlsToLogos() {
        return urlsToLogos;
    }

    public void setUrlsToLogos(UrlsToLogos urlsToLogos) {
        this.urlsToLogos = urlsToLogos;
    }

    public List<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    public void setSortBysAvailable(List<String> sortBysAvailable) {
        this.sortBysAvailable = sortBysAvailable;
    }

}
