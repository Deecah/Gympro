
package model;

import java.sql.Timestamp;

public class Comment {
    private int id;
    private int userId;
    private int blogId;
    private Integer parentId; //thuoc tinh de luu xem comment nay co phai la comment goc khong
    private String content;
    private Timestamp createdAt;

    public Comment(int id, int userId, int blogId, int parentId, String content, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.blogId = blogId;
        this.parentId = parentId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    

    
}
