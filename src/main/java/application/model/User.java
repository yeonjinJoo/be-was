package application.model;

public class User {
    private Integer id;
    private String userId;
    private String password;
    private String name;
    private String email;
    private String profileImageUrl;

    public User(String userId, String password, String name, String email) {
        this.id = null;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profileImageUrl = null;
    }

    public User(Integer id, String userId, String password, String name, String email, String profileImageUrl) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public Integer getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
}
