package mangotiger.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Tom Gagnier
 */
public class Connections {

  private Connections() {
    // prevent construction
  }

  public static void close(Connection connection) {
    try {
      if (connection != null) {
        connection.close();
      }
    } catch (SQLException e) {
      log().error("unable to close connection", e);
    }
  }

  private static Log log() {
    return LogFactory.getLog(Connections.class);
  }

}
