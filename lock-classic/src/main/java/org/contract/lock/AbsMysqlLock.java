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

public abstract class AbsMysqlLock implements DistributedLock {
    private final static Logger log = LogManager.getLogger(AbsMysqlLock.class);

    protected String driver;
    protected String url;
    protected String user;
    protected String password;
    protected Connection conn;

    @Override
    public void init(Config config) throws InitException {
        Map<String, String> map = config.getConfig();
        this.driver = map.get("mysql.driver");
        this.url = map.get("mysql.url");
        this.user = map.get("mysql.user");
        this.password = map.get("mysql.password");
        if(StringUtils.isBlank(this.driver) || StringUtils.isBlank(this.url) || StringUtils.isBlank(this.user) || StringUtils.isBlank(this.password)) {
            throw new InitException("mysql parameter(driver url user password) is a must");
        }
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new InitException("get mysql connect error");
        } catch (ClassNotFoundException e) {
            throw new InitException("driver is not found");
        }
    }

    public void beforeCheck(String key, String identity, long timeout, TimeUnit unit) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(querySQL(key, identity));
            rs.next();
            if(rs.getRow() == 0) {
                stmt.executeUpdate(insertSQL(key, identity, 0, 0));
            }
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
    }

    public void afterClear(String key, String identity, long timeout, TimeUnit unit) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            rs = stmt.executeQuery(querySQL(key, identity));
            rs.next();
            if(rs.getInt("count") == 0) {
                stmt.executeUpdate(deleteSQL(key, identity));
            }
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
    }

    @Override
    public void close() throws RunException {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private String insertSQL(String key, String identity, int version, int count) {
        String format = String.format("INSERT INTO test_lock (lock_key, lock_identity, version, count) VALUES ('%s','%s', %d, %d)", key, identity, version, count);
        log.info(format);
        return format;
    }

    private String updateSQL(String key, String identity, int count, int version) {
        String format = String.format("UPDATE test_lock SET count = %d WHERE lock_key='%s' and lock_identity='%s' and version=%d", count, key, identity, version);
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
