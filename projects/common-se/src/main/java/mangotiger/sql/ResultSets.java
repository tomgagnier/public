package mangotiger.sql;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.text.Formats;

/**
 * A collection of static method helpers for working with {@link ResultSet}.
 * @author Tom Gagnier
 */
public final class ResultSets {
  private static final int INITIAL_BUFFER_CAPACITY = 1024;
  private static final int DEFAULT_MAX_RESULTS = 100;

  private ResultSets() {
    // prevent construction
  }

  /**
   * Close a {@link ResultSet} and log any errors instead of throwing an exception.
   */
  public static void close(final ResultSet resultSet) {
    try {
      if (resultSet != null) {
        resultSet.close();
      }
    } catch (SQLException e) {
      log().error("unable to close connection", e);
    }
  }

  /**
   * Return a fixed with text table representation of a {@link ResultSet}.
   */
  public static String toString(final ResultSet rs) {
    return toString(rs, DEFAULT_MAX_RESULTS);
  }

  /**
   * Return a fixed with text table representation of a {@link ResultSet}.
   * @param maxResults the maximum number of results represented.
   */
  public static String toString(final ResultSet rs, final int maxResults) {
    final StringBuffer buffer = new StringBuffer(INITIAL_BUFFER_CAPACITY);
    try {
      final int[] padSizes = initPadSizes(rs.getMetaData());
      final List rows = initRows(rs, maxResults, padSizes);
      removeDuplicates(rows);
      appendColumnNames(buffer, rs, padSizes);
      appendLine(buffer, padSizes);
      appendRows(buffer, rows, padSizes);
      buffer.append("\n\n").append(rows.size()).append(" rows returned");
    } catch (SQLException e) {
      log().error(e, e);
    }
    return buffer.toString();
  }

  private static void appendColumnNames(final StringBuffer buffer, final ResultSet rs, final int[] padSizes) throws SQLException {
    buffer.append('\n');
    for (int i = 0; i < padSizes.length; ++i) {
      pad(buffer, rs.getMetaData().getColumnName(i + 1), padSizes[i], ' ');
    }
  }

  private static void appendLine(final StringBuffer buffer, final int[] padSizes) {
    buffer.append('\n');
    for (int i = 0; i < padSizes.length; ++i) {
      pad(buffer, "", padSizes[i], '-');
    }
  }

  private static void appendRows(final StringBuffer buffer, final List rows, final int[] padSizes) {
    for (Iterator i = rows.iterator(); i.hasNext();) {
      appendRow(buffer, (String[])i.next(), padSizes);
    }
  }

  private static void appendRow(final StringBuffer buffer, final String[] row, final int[] padSizes) {
    buffer.append('\n');
    for (int i = 0; i < padSizes.length; ++i) {
      pad(buffer, row[i], padSizes[i], ' ');
    }
  }

  private static List initRows(final ResultSet rs, final int maxCount, final int[] padSizes) throws SQLException {
    final List rows = new LinkedList();
    for (int count = 0; rs.next() && count < maxCount; ++count) {
      rows.add(newRow(rs, padSizes));
    }
    return rows;
  }

  private static void removeDuplicates(final List rows) {
    String[] lastRow = null;
    for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
      final String[] row = (String[])iterator.next();
      lastRow = removeDuplicates(lastRow, row);
    }
  }

  private static String[] removeDuplicates(final String[] lastRow, final String[] row) {
    if (lastRow == null) {
      return (String[])Arrays.asList(row).toArray(new String[row.length]);
    }
    for (int i = 0; i < lastRow.length && i < row.length; ++i) {
      if (lastRow[i].equals(row[i])) {
        row[i] = "\"";
      } else {
        lastRow[i] = row[i];
      }
    }
    return lastRow;
  }

  private static String[] newRow(final ResultSet rs, final int[] padSizes) throws SQLException {
    final String[] row = new String[padSizes.length];
    for (int i = 0; i < padSizes.length; ++i) {
      final Object object = getObject(rs, i + 1);
      row[i] = toString(object);
      padSizes[i] = Math.max(padSizes[i], row[i].length());
    }
    return row;
  }

  private static int[] initPadSizes(final ResultSetMetaData md) throws SQLException {
    final int[] padSizes = new int[md.getColumnCount()];
    for (int i = 0; i < padSizes.length; ++i) {
      padSizes[i] = toString(md.getColumnName(i + 1)).length();
    }
    return padSizes;
  }

  private static void pad(final StringBuffer buffer, final String s, final int pad, final char padchar) {
    final int requiredPad = Math.max(0, pad - toString(s).length());
    pad(buffer, requiredPad / 2, padchar);
    buffer.append(s);
    pad(buffer, (requiredPad + 1) / 2, padchar);
    buffer.append(' ');
  }

  private static void pad(final StringBuffer buffer, final int size, final char padchar) {
    for (int i = 0; i < size; ++i) {
      buffer.append(padchar);
    }
  }

  private static Object getObject(final ResultSet rs, final int i) throws SQLException {
    final ResultSetMetaData metaData = rs.getMetaData();
    switch (metaData.getColumnType(i)) {
    case Types.TIMESTAMP:
    case Types.DATE:
    case Types.TIME:
      final Date date = rs.getDate(i);
      return date == null ? null : isDate(metaData, i) ? date.toString() : Formats.timeFormatter12().format(date);
    default:
      return rs.getObject(i);
    }
  }

  private static boolean isDate(final ResultSetMetaData metaData, final int i) throws SQLException {
    return metaData.getColumnName(i).indexOf("TIME") == -1;
  }

  private static String toString(final Object object) {
    return object == null ? "null" : object.toString();
  }

  private static Log log() {
    return LogFactory.getLog(ResultSets.class);
  }

}
