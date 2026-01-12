package application.model;

public class Post {
    private Integer id;
    private String content;
    private String userId;

    public Post(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }

    public Post(Integer id, String content, String userId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public String getUserId() {
        return userId;
    }

}
