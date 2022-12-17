package simpledb.jdbc.network;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class NetworkDataSource implements DataSource {

    private String database;
    private String server;

    @Override
    public Connection getConnection()
    throws SQLException {
        NetworkDriver driver = new NetworkDriver();
        if(getServer()==null)
            throw new SQLException("");

        StringBuilder connectionStr = new StringBuilder(String.format("jdbc:simpledb://%s",getServer()));
        if(getDatabase()!=null)
            connectionStr.append(String.format(":%s",getDatabase()));

        return driver.connect(connectionStr.toString(),null);
    }

    public String getDatabase() {
        return database;
    }

    public String getServer() {
        return server;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public Connection getConnection(String username, String password)
    throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface)
    throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
    throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter()
    throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out)
    throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds)
    throws SQLException {

    }

    @Override
    public int getLoginTimeout()
    throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger()
    throws SQLFeatureNotSupportedException {
        return null;
    }
}
