package application.repository;

import application.model.Post;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2PostRepository implements PostRepository {
    public void addPost(Post post){
        String sql = "INSERT INTO post(content, author_id) VALUES (?, ?)";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, post.getContent());
            pstmt.setString(2, post.getUserId());

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
