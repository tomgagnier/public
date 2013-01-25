package mangotiger.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A very simple {@link DataSource}.
 * @author Tom Gagnier
 */
public class SimpleDataSource implements DataSource {
  private static final Class[] PARAMETER_TYPES = new Class[]{};
  private static final Object[] INITARGS = new Object[]{};
  private final String driver;
  private final String url;
  private String username;
  private String password;

  public SimpleDataSource(final String driver, final String url) {
    register(driver);
    this.driver = driver;
    this.url = url;
  }

  public SimpleDataSource(final String driver, final String url, final String user, final String password) {
    this(driver, url);
    setCredentials(user, password);
    testConnection();
  }

  public String getDriver() {
    return driver;
  }

  public String getUrl() {
    return url;
  }

  public final String getUsername() {
    return username;
  }

  public final void setCredentials(final String user, final String password) {
    username = user;
    this.password = password;
  }

  public final void testConnection() {
    try {
      getConnection().close();
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private static void register(final String driverClassName) {
    try {
      final Class driverClass = Class.forName(driverClassName);
      final Driver driver = (Driver)driverClass.getConstructor(PARAMETER_TYPES).newInstance(INITARGS);
      DriverManager.registerDriver(driver);
    } catch (Exception e) {
      final String message = "driver not found:" + driverClassName;
      log().error(message, e);
      final IllegalStateException exception = new IllegalStateException(message);
      exception.setStackTrace(e.getStackTrace());
      throw exception;
    }
  }

  public final int getLoginTimeout() {return 0;}

  public final void setLoginTimeout(final int seconds) {}

  public final PrintWriter getLogWriter() {return null;}

  public final void setLogWriter(final PrintWriter out) {}

  public final Connection getConnection() throws SQLException {
    return getConnection(username, password);
  }

  public final Connection getConnection(final String newUsername, final String newPassword) throws SQLException {
    return DriverManager.getConnection(url, newUsername, newPassword);
  }

  @Override public String toString() {
    return "SimpleDataSource{url='" + url + '\'' + ",username='" + username + '\'' + '}';
  }

  private static Log log() {
    return LogFactory.getLog(SimpleDataSource.class);
  }
}
