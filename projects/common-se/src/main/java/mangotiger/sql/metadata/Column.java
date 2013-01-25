package mangotiger.sql.metadata;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * A column in a metadata table.
 * @author tom_gagnier@yahoo.com
 */
final class Column {
  public final String name;
  public final int type;
  public final Object value;

  /** Construct from a result set and an index into the result set. */
  Column(final ResultSet resultSet, final int index) throws SQLException {
    final ResultSetMetaData metaData = resultSet.getMetaData();
    name = metaData.getColumnName(index);
    type = metaData.getColumnType(index);
    value = resultSet.getObject(index);
  }

  @Override public String toString() {
    final StringBuffer buffer = new StringBuffer(name).append('=');
    if (value == null) {
      buffer.append("NULL");
    } else if (value.getClass().equals(String.class)) {
      buffer.append(quote(value.toString()));
    } else {
      buffer.append(value);
    }
    return buffer.toString();
  }

  private static String quote(final String s) {
    return '\'' + s + '\"';
  }
}
