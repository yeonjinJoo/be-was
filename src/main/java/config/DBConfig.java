package config;

import java.sql.*;

public class DBConfig {
    private static final String URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void init() {
        String dropPosts = "DROP TABLE IF EXISTS POST";
        String dropUsers = "DROP TABLE IF EXISTS USERS";

        String createUsers = """
        CREATE TABLE USERS (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            user_id VARCHAR(255) NOT NULL UNIQUE,
            name VARCHAR(255) NOT NULL UNIQUE,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL
        )
        """;

        String createPost = """
        CREATE TABLE POST (
            post_id BIGINT AUTO_INCREMENT PRIMARY KEY,
            content TEXT NOT NULL,
            author_id BIGINT NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            CONSTRAINT fk_post_user
                FOREIGN KEY (author_id)
                REFERENCES USERS(id)
        )
        """;

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(dropPosts);
            stmt.execute(dropUsers);

            stmt.execute(createUsers);
            stmt.execute(createPost);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 테이블 초기화 에러", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
