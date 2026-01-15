package application.model;

import java.time.LocalDateTime;

public class Article {
    private Integer id;
    private String content;
    private int authorId;
    private String imageUrl;
    private LocalDateTime createdAt;

    public Article(String content, int authorId) {
        this.content = content;
        this.authorId = authorId;
    }

    public Article(Integer id, String content, int authorId, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Article(String content, int authorId, String imageUrl) {
        this.content = content;
        this.authorId = authorId;
        this.imageUrl = imageUrl;
    }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public int getAuthorId() {
        return authorId;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
