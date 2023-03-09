package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * @author fuzhenfeng
 */
public final class MysqlLock extends AbsMysqlLock {
    private final static Logger log = LogManager.getLogger(MysqlLock.class);

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        beforeCheck(key, identity, timeout, unit);

        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            rs = stmt.executeQuery(querySQL(key, identity));
            rs.next();
            if(rs.getRow() == 0) {
                return false;
            }
            int count = rs.getInt("count");
            stmt.executeUpdate(updateSQL(key, identity, count + 1));
            conn.commit();
            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();

            rs = stmt.executeQuery(querySQL(key, identity));
            rs.next();
            int count = rs.getInt("count");
            if(count <= 0) {
                conn.commit();
                return false;
            } else {
                stmt.executeUpdate(updateSQL(key, identity, count - 1));
                conn.commit();
                return true;
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                log.error(ex.getMessage(), ex);
            }
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    private String insertSQL(String key, String identity, int count) {
        String format = String.format("INSERT INTO test_lock (lock_key, lock_identity, count) VALUES ('%s','%s',%d)", key, identity, count);
        log.info(format);
        return format;
    }

    private String updateSQL(String key, String identity, int count) {
        String format = String.format("UPDATE test_lock SET count = %d WHERE lock_key='%s' and lock_identity='%s'", count, key, identity);
        log.info(format);
        return format;
    }

    private String querySQL(String key, String identity) {
        String format = String.format("SELECT count FROM test_lock WHERE lock_key='%s' and lock_identity = '%s' FOR UPDATE", key, identity);
        log.info(format);
        return format;
    }

    private String deleteSQL(String key, String identity) {
        String format = String.format("DELETE FROM test_lock WHERE lock_key='%s' and lock_identity = '%s'", key, identity);
        log.info(format);
        return format;
    }
}
