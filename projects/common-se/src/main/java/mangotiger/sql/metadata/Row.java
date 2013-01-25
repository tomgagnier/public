package mangotiger.sql.metadata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 * @author tom_gagnier@yahoo.com
 */
public class Row {

  final Column[] columns;

  Row(ResultSet resultSet) throws SQLException {
    columns = newColumns(resultSet);
  }

  Row(DataSource dataSource, String sql, Object[] params) throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      if (params != null) {
        for (int i = 0; i < params.length; ++i) {
          final Object param = params[i];
          final int parameterIndex = i + 1;
          statement.setObject(parameterIndex, param);
        }
      }
      final ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        columns = newColumns(resultSet);
        if (resultSet.next()) {
          throw new IllegalArgumentException("failed to return unique result set");
        }
      } else {
        columns = null;
      }
    } finally {
      if (statement != null) statement.close();
      if (connection != null) connection.close();
    }
  }

  Row(DataSource dataSource, String sql, Object param) throws SQLException {
    this(dataSource, sql, new Object[]{param});
  }

  Row(DataSource dataSource, String sql, Object param1, Object param2) throws SQLException {
    this(dataSource, sql, new Object[]{param1, param2});
  }

  static Row[] newRows(final DataSource dataSource, final String sql, final Object param) throws SQLException {
    return newRows(dataSource, sql, new Object[]{param});
  }

  static Row[] newRows(final DataSource dataSource, final String sql, final Object[] params) throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      final PreparedStatement statement = connection.prepareStatement(sql);
      if (params != null) {
        for (int i = 0; i < params.length; ++i) {
          final int parameterIndex = i + 1;
          final Object param = params[i];
          statement.setObject(parameterIndex, param);
        }
      }
      final ResultSet resultSet = statement.executeQuery();
      final List rows = new ArrayList();
      while (resultSet.next()) {
        final Row row = new Row(resultSet);
        rows.add(row);
      }
      return (Row[]) rows.toArray(new Row[rows.size()]);
    } finally {
      if (connection != null) connection.close();
    }
  }



  /**
   * Compute a new row of columns from a result set.
   *
   * @param resultSet
   *
   * @return a new row of columns from a result set
   *
   * @throws java.sql.SQLException
   */
  static Column[] newColumns(final ResultSet resultSet) throws SQLException {
    final int columnCount = resultSet == null ? 0 : resultSet.getMetaData().getColumnCount();
    final Column[] row = new Column[columnCount];
    for (int i = 0; i < columnCount; ++i) {
      row[i] = new Column(resultSet, i + 1);
    }
    return row;
  }

  public Column get(String columnName) {
    for (int i = 0; i < columns.length; ++i) {
      final Column column = columns[i];
      if (column.name.equalsIgnoreCase(columnName)) {
        return column;
      }
    }
    return null;
  }
}
