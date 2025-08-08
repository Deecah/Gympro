package model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Blog {
    private int id;
    private String title;
    private String content;
    private String thumbnail;
    private ArrayList<String> imagesUrl;
    private String tag;
    private Timestamp createdAt;

    // Constructor
    public Blog() {}

    public Blog(int id, String title, String content, String thumbnail, ArrayList<String> imagesUrl, String tag, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.imagesUrl = imagesUrl;
        this.tag = tag;
        this.createdAt = createdAt;
    }

    // Getter + Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public ArrayList<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(ArrayList<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
    
}
 