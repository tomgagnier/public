package mangotiger.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * A SQL Statement Recorder. <p/> This class executes SQL Statements and records the results for later display.
 * @author tom_gagnier@yahoo.com
 */
public final class SqlRecorder {
  private final DataSource dataSource;
  private final StringBuffer ddl = new StringBuffer();
  private static final String OK = "--OK";
  private static final String FAILED = "--FAILED!";

  /** Construct a SQL recorder from a connection data source. */
  public SqlRecorder(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Run a SQL statement and record results.
   * @param sql the SQL statement to run
   */
  public void run(final String sql) throws SQLException {
    run(sql, null);
  }

  /**
   * Run a SQL statement and record results, and ignore resulting errors. <p/> This is used to drop tables when it isn't
   * important if the table existed.
   * @param sql the SQL statement to run
   */
  public void runIgnoringErrors(final String sql) {
    try {
      run(sql);
    } catch (SQLException ignore) {
      // intentionally empty
    }
  }

  /**
   * Run a SQL statement and record results.
   * @param sql    the SQL statement to run, with ? denoting parameter positions
   * @param params an array of parameters to substitute into the SQL statement
   */
  public void run(final String sql, final Object[] params) throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      final PreparedStatement statement = connection.prepareStatement(sql);
      if (null != params) {
        for (int i = 0; i < params.length; ++i) {
          statement.setObject(i + 1, params[i]);
        }
      }
      statement.executeUpdate();
      ddl.append(OK).append('\n').append(sql).append(";\n\n");
    } catch (SQLException e) {
      ddl.append(FAILED).append('\n').append(sql).append(";\n\n");
      throw e;
    } finally {
      SQL.close(connection);
    }
  }

  public SqlRecorder clear() {
    ddl.setLength(0);
    return this;
  }

  public SqlRecorder append(final Object object) {
    ddl.append(object);
    return this;
  }

  public SqlRecorder appendln(final Object object) {
    return append(object).appendln();
  }

  private SqlRecorder appendln() {
    ddl.append('\n');
    return this;
  }

  @Override public String toString() {
    return ddl.toString();
  }

  public String toHtml() {
    final String header = "<font size=+1><pre>\n";
    final String trailer = "\n</font></pre>";
    final StringBuffer html = new StringBuffer(ddl.length() + header.length() + trailer.length());
    return html.append(header).append(ddl).append(trailer).toString();
  }

  public void dropAllTables() throws SQLException {
    final List dropTableStatements = newDropTableStatements();
    for (Object dropTableStatement1 : dropTableStatements) {
      final String dropTableStatement = (String)dropTableStatement1;
      run(dropTableStatement);
    }
  }

  /** @return a list of DDL statements to drop tables */
  private List newDropTableStatements() throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      final Statement statement = connection.createStatement();
      final String select = "select 'drop table ' || table_name || ' cascade constraints' from user_tables";
      final ResultSet rs = statement.executeQuery(select);
      final List<String> dropTableStatements = new ArrayList<String>();
      while (rs.next()) {
        dropTableStatements.add(rs.getString(1));
      }
      return dropTableStatements;
    } catch (SQLException e) {
      throw e;
    } finally {
      SQL.close(connection);
    }
  }
}
