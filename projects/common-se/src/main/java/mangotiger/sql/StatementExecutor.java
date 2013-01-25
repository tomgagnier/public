package mangotiger.sql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Execute a sequence of SQL statements.
 * @author tom_gagnier@yahoo.com
 */
public final class StatementExecutor {
  private static final Log LOG = LogFactory.getLog(StatementExecutor.class);
  private final DataSource dataSource;

  /**
   * A new StatementExecutor.
   * @param dataSource the data source to execute against.
   */
  public StatementExecutor(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Execute the statements described by this input stream.
   * @param in the input stream containing the SQL statements.
   * @return the number of rows affected.
   */
  public int execute(final InputStream in) throws SQLException {
    Connection connection = null;
    int rowsAffected = 0;
    try {
      connection = dataSource.getConnection();
      final Statement statement = connection.createStatement();
      for (Iterator i = new StatementIterator(in); i.hasNext();) {
        final String sql = (String)i.next();
        rowsAffected += execute(connection, statement, sql);
      }
    } finally {
      if (connection != null) {
        connection.close();
      }
    }
    return rowsAffected;
  }

  private static int execute(final Connection connection, final Statement statement, final String sql) throws SQLException {
    int rowsAffected = 0;
    try {
      if (sql.trim().length() == 0) {
        return rowsAffected;
      }
      boolean hasResultSet = statement.execute(sql);
      statement.getResultSet();
      do {
        if (!hasResultSet) {
          final int updateCount = statement.getUpdateCount();
          if (updateCount != -1) {
            rowsAffected += updateCount;
          }
        }
        hasResultSet = statement.getMoreResults();
        statement.getResultSet();
      } while (hasResultSet);
      for (SQLWarning warning = connection.getWarnings(); warning != null; warning = warning.getNextWarning()) {
        LOG.warn(new StringBuilder().append("sql warning: ").append(warning));
      }
      connection.clearWarnings();
    } catch (SQLException e) {
      LOG.error("Failed to execute: " + sql);
      throw e;
    }
    return rowsAffected;
  }

  @Override public String toString() {
    return "StatementExecutor{dataSource=" + dataSource + '}';
  }
}
