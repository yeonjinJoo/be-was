package application.repository;

import application.model.Article;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2ArticleRepository implements ArticleRepository {
    public void addArticle(Article article){
        String sql = "INSERT INTO post(content, author_id) VALUES (?, ?)";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, article.getContent());
            pstmt.setInt(2, article.getAuthorId());

            int affected = pstmt.executeUpdate();
            if (affected != 1) {
                throw new RuntimeException("게시글 저장 실패: affectedRows=" + affected);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 접근 에러 발생");
        }
    }
}
