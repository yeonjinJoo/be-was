package application.repository;

import config.DBConfig;

import java.sql.Connection;
import java.sql.SQLException;

public final class JdbcTx {

    @FunctionalInterface
    public interface Work<T> {
        T run(Connection conn) throws Exception;
    }

    // read / 단일 update 용
    public <T> T execute(Work<T> work) {
        try (Connection conn = DBConfig.getConnection()) {
            return work.run(conn);
        } catch (Exception e) {
            throw rethrow(e);
        }
    }

    // transaction 필요한 경우
    public <T> T executeInTx(Work<T> work) {
        try (Connection conn = DBConfig.getConnection()) {
            boolean oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                T result = work.run(conn);
                conn.commit();
                return result;
            } catch (Exception e) {
                try { conn.rollback(); } catch (SQLException ignore) {}
                throw rethrow(e);
            } finally {
                try { conn.setAutoCommit(oldAutoCommit); } catch (SQLException ignore) {}
            }
        } catch (Exception e) {
            throw rethrow(e);
        }
    }

    private RuntimeException rethrow(Exception e) {
        return (e instanceof RuntimeException re) ? re : new RuntimeException(e);
    }
}
