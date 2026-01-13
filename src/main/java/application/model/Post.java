package application.model;

import java.time.LocalDateTime;

public class Post {
    private Integer id;
    private String content;
    private int authorId;
    private LocalDateTime createdAt;

    public Post(String content, int authorId) {
        this.content = content;
        this.authorId = authorId;
    }

    public Post(Integer id, String content, int authorId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
