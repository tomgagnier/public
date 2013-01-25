package mangotiger.sql.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

/**
 * A metadata table.
 *
 * @author tom_gagnier@yahoo.com
 */
final class Table {

  final String name;
  private final Set keySet;
  private final String[] keys;
  private final List rows;
  private final String where;

  static Table newTable(final DataSource dataSource, final String tableName) throws SQLException {
    return new Table(dataSource, tableName, "");
  }

  static Table newTable(final DataSource dataSource, final String tableName, final String whereClause)
      throws SQLException {
    return new Table(dataSource, tableName, whereClause);
  }

  static Table newTable(final String xml) {
    return (Table)new XStream().fromXML(xml);
  }

  private Table(final DataSource dataSource, final String tableName, final String whereClause) throws SQLException {
    name = tableName;
    where = whereClause;
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      keys = newKeyNames(connection, tableName);
      keySet = new HashSet(Arrays.asList(keys));
      rows = newRows(connection, tableName);
    } finally {
      if (connection != null) connection.close();
    }
  }

  /**
   * Construct a list of primary key names in the order of their sequence number.
   *
   * @param connection
   * @param tableName
   *
   * @return a list of primary key names in key order
   *
   * @throws SQLException
   */
  static String[] newKeyNames(final Connection connection, final String tableName) throws SQLException {
    final Map keys = new TreeMap();
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet resultSet = metaData.getPrimaryKeys(null, null, tableName.toUpperCase());
    while (resultSet.next()) {
      final Short keySeq = new Short(resultSet.getShort("KEY_SEQ"));
      final String columnName = resultSet.getString("COLUMN_NAME");
      keys.put(keySeq, columnName);
    }
    return (String[])keys.values().toArray(new String[keys.size()]);
  }

  private List newRows(final Connection connection, final String tableName) throws SQLException {
    final List newRows = new ArrayList(rowCount(connection, tableName));
    final ResultSet rs = connection.createStatement().executeQuery(select());
    final ResultSetMetaData metaData = rs.getMetaData();
    final int columnCount = metaData.getColumnCount();
    while (rs.next()) {
      newRows.add(newRow(columnCount, rs));
    }
    return newRows;
  }

  private static Column[] newRow(final int columnCount, final ResultSet rs) throws SQLException {
    final Column[] row = new Column[columnCount];
    for (int i = 0; i < columnCount; ++i) {
      row[i] = new Column(rs, i + 1);
    }
    return row;
  }

  private static int rowCount(final Connection c, final String name) throws SQLException {
    final String sql = "select count(*) from " + name;
    final ResultSet countResultSet = c.createStatement().executeQuery(sql);
    if (!countResultSet.next()) {
      throw new IllegalStateException("unable to count rows in " + name);
    }
    return countResultSet.getInt(1);
  }

  private static Log log() {
    return LogFactory.getLog(Table.class);
  }

  public void write(final DataSource datasource) throws SQLException {
    if (rows.size() == 0) return;
    Connection c = null;
    try {
      c = datasource.getConnection();
      final PreparedStatement select = c.prepareStatement(selectRow());
      final PreparedStatement insert = c.prepareStatement(insert());
      final PreparedStatement update = c.prepareStatement(update());
      for (int i = 0; i < rows.size(); ++i) {
        final Column[] columns = (Column[])rows.get(i);
        if (isUpdate(select, columns)) {
          executeUpdate(columns, update);
        } else {
          executeInsert(columns, insert);
        }
      }
    } finally {
      if (c != null) c.close();
    }
  }

  private boolean isUpdate(final PreparedStatement select, final Column[] columns) throws SQLException {
    int count = 0;
    for (int i = 0; i < columns.length; ++i) {
      final Column column = columns[i];
      if (keySet.contains(column.name)) {
        setPreparedColumn(select, column, ++count);
      }
    }
    return select.executeQuery().next();
  }

  private static void setPreparedColumn(final PreparedStatement statement, final Column column, final int index)
      throws SQLException {
    if (column.value == null) {
      statement.setNull(index, column.type);
    } else {
      statement.setObject(index, column.value);
    }
  }

  private static void executeInsert(final Column[] columns, final PreparedStatement insert) throws SQLException {
    for (int j = 0; j < columns.length; ++j) {
      final Column column = columns[j];
      setPreparedColumn(insert, column, j + 1);
    }
    insert.executeUpdate();
  }

  private void executeUpdate(final Column[] columns, final PreparedStatement update) throws SQLException {
    int setIndex = 0;
    int whereIndex = columns.length - keySet.size();
    boolean updatePossible = false;
    for (int i = 0; i < columns.length; ++i) {
      final Column column = columns[i];
      if (keySet.contains(column.name)) {
        setPreparedColumn(update, column, ++whereIndex);
      } else {
        updatePossible = true;
        setPreparedColumn(update, column, ++setIndex);
      }
    }
    if (updatePossible) update.executeUpdate();
  }

  String select() {
    final StringBuffer select = new StringBuffer("SELECT * FROM ").append(name);
    if (where.length() > 0) select.append(" WHERE ").append(where);
    for (int i = 0; i < keys.length; ++i) {
      select.append(i == 0 ? " ORDER BY " : ",").append(keys[i]);
    }
    log().debug(select);
    return select.toString();
  }

  String selectRow() {
    final StringBuffer select = new StringBuffer("SELECT * FROM ").append(name);
    for (int i = 0; i < keys.length; ++i) {
      select.append(i == 0 ? " WHERE " : " AND ").append(keys[i]).append("=?");
    }
    log().debug(select);
    return select.toString();
  }

  /**
   * An insert statement parameterized by column name.
   *
   * @return An insert statement parameterized by column name.
   */
  String insert() {
    final StringBuffer insert = new StringBuffer("INSERT INTO ").append(name).append(' ');
    final Column[] columns = (Column[])rows.get(0);
    insert.append('(');
    for (int i = 0; i < columns.length; ++i) {
      insert.append(columns[i].name).append(',');
    }
    insert.setCharAt(insert.length() - 1, ')');
    insert.append(" VALUES(");
    for (int i1 = 0; i1 < columns.length; ++i1) {
      insert.append('?').append(',');
    }
    insert.setCharAt(insert.length() - 1, ')');
    log().debug(insert);
    return insert.toString();
  }

  /**
   * An update statement parameterized by column name.
   *
   * @return An update statement parameterized by column name.
   */
  String update() {
    if (rows.size() == 0) return "";
    final StringBuffer update = new StringBuffer("UPDATE ").append(name);
    final StringBuffer whereClause = new StringBuffer();
    final int start = update.length();
    final Column[] columns = (Column[])rows.get(0);
    for (int i = 0; i < columns.length; ++i) {
      final Column column = columns[i];
      if (keySet.contains(column.name)) {
        whereClause.append(whereClause.length() == 0 ? " WHERE " : " AND ").append(column.name).append("=?");
      } else {
        update.append(update.length() == start ? " SET " : ",").append(column.name).append("=?");
      }
    }
    update.append(whereClause);
    log().debug(update);
    return update.toString();
  }

  public String toString() {
    return new XStream().toXML(this);
  }

}