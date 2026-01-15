package application.repository;

import application.model.Article;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2ArticleRepository implements ArticleRepository {
    public void addArticle(Connection conn, Article article){
        String sql = "INSERT INTO article(content, author_id, image_url) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.getContent());
            pstmt.setInt(2, article.getAuthorId());
            pstmt.setString(3, article.getImageUrl());

            int affected = pstmt.executeUpdate();
            if (affected != 1) {
                throw new RuntimeException("게시글 저장 실패: affectedRows=" + affected);
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }
}
