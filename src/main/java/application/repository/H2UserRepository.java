package application.repository;

import application.model.User;
import config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class H2UserRepository implements UserRepository{
    public void addUser(User user){
        String sql = "INSERT INTO users(user_id, password, name, email) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            int affected = pstmt.executeUpdate();
            if (affected != 1) {
                throw new RuntimeException("사용자 저장 실패: affectedRows=" + affected);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 접근 에러 발생");
        }
    }

    public Optional<User> login(String userId, String password){
        String sql = "SELECT user_id, password, name, email FROM users WHERE user_id = ? AND password = ?";
        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            try(ResultSet rs = pstmt.executeQuery()){
                if (!rs.next()) {
                    return Optional.empty();
                }

                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );

                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 접근 에러 발생");
        }
    }

    public Optional<User> findUserById(String userId){
        String sql = "SELECT user_id, password, name, email FROM users WHERE user_id = ?";
        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, userId);

            try(ResultSet rs = pstmt.executeQuery()){
                if (!rs.next()) {
                    return Optional.empty();
                }

                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );

                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("데이터베이스 접근 에러 발생");
        }
    }
}
