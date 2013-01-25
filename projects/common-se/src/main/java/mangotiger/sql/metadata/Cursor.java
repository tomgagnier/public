package mangotiger.sql.metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 * @author tom_gagnier@yahoo.com
 */
class Cursor {

  final Connection connection;
  final Statement statement;
  final ResultSet resultSet;

  public Cursor(Connection connection, Statement statement, ResultSet resultSet) {
    this.connection = connection;
    this.statement = statement;
    this.resultSet = resultSet;
  }

  public void close() throws SQLException {
    if (connection != null) connection.close();
  }
}
