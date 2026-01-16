package application.repository;

import application.model.User;
import config.DBConfig;

import java.sql.*;
import java.util.Optional;

public class H2UserRepository implements UserRepository {

    @Override
    public void addUser(Connection conn, User user) {
        String sql = "INSERT INTO users(user_id, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());

            int affected = pstmt.executeUpdate();
            if (affected != 1) {
                throw new RuntimeException("사용자 저장 실패: affectedRows=" + affected);
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }

    @Override
    public Optional<User> findByUserId(Connection conn, String userId) {
        String sql = "SELECT id, user_id, password, name, email, profile_image_url FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                User user = new User(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("profile_image_url")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }

    @Override
    public User findById(Connection conn, int id) {
        String sql = "SELECT id, user_id, password, name, email, profile_image_url FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("profile_image_url")
                );
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }

    @Override
    public boolean existsByUserId(Connection conn, String userId) {
        String sql = "SELECT 1 FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }

    @Override
    public boolean existsByUserName(Connection conn, String userName) {
        String sql = "SELECT 1 FROM users WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }

    public void updateProfile(Connection conn, int id, String newName, String newPw, boolean changeProfileImage, String newProfileUrl) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");
        boolean first = true;

        if (newName != null) {
            sql.append("name = ?");
            first = false;
        }
        if (newPw != null) {
            if (!first) sql.append(", ");
            sql.append("password = ?");
            first = false;
        }
        if(changeProfileImage){
            if (!first) sql.append(", ");
            sql.append("profile_image_url = ?");
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            if (newName != null) ps.setString(idx++, newName);
            if (newPw != null) ps.setString(idx++, newPw);
            if(changeProfileImage){
                if(newProfileUrl == null){
                    ps.setNull(idx++, Types.VARCHAR);
                }
                else{
                    ps.setString(idx++, newProfileUrl);
                }
            }
            ps.setInt(idx, id);

            int affected = ps.executeUpdate();
            if (affected != 1) throw new RuntimeException("프로필 변경 실패: affectedRows=" + affected);
        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 접근 에러 발생", e);
        }
    }
}
