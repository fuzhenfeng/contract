package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MysqlLock2 extends AbsMysqlLock {
    private final static Logger log = LogManager.getLogger(MysqlLock2.class);

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        beforeCheck(key, identity, timeout, unit);

        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            long t1 = System.currentTimeMillis();
            while (true) {
                if((System.currentTimeMillis() - t1)/1000 > timeout) {
                    break;
                }

                rs = stmt.executeQuery(querySQL(key, identity));
                rs.next();
                if(rs.getRow() == 0) {
                    return false;
                }
                int count = rs.getInt("count");
                int version = rs.getInt("version");
                int change = stmt.executeUpdate(updateSQL(key, identity, count + 1, version));
                if(change == 1) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
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
            stmt = conn.createStatement();

            long t1 = System.currentTimeMillis();
            while (true) {
                if ((System.currentTimeMillis() - t1) / 1000 > timeout) {
                    break;
                }
                rs = stmt.executeQuery(querySQL(key, identity));
                rs.next();
                int count = rs.getInt("count");
                int version = rs.getInt("version");
                if(count <= 0) {
                    return false;
                }
                int change = stmt.executeUpdate(updateSQL(key, identity, count - 1, version));
                if(change == 1) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
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

    private String updateSQL(String key, String identity, int count, int version) {
        String format = String.format("UPDATE test_lock SET count = %d, version=version+1 WHERE lock_key='%s' and lock_identity='%s' and version=%d", count, key, identity, version);
        log.info(format);
        return format;
    }

    private String querySQL(String key, String identity) {
        String format = String.format("SELECT count,version FROM test_lock WHERE lock_key='%s' and lock_identity = '%s'", key, identity);
        log.info(format);
        return format;
    }

    private String deleteSQL(String key, String identity) {
        String format = String.format("DELETE FROM test_lock WHERE lock_key='%s' and lock_identity = '%s'", key, identity);
        log.info(format);
        return format;
    }
}
