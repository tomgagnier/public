/*
 * $Source: $
 *
 * Copyright (C) 2002-2005, Thomas Robert Gagnier, Jr., All Rights Reserved.
 * Unauthorized use, disclosure or reproduction of this source code is strictly
 * prohibited by United States copyright law and international treaty provisions.
 * Use of source code requires an appropriate source license.
 */
package mangotiger.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** @author tom_gagnier@yahoo.com */
public final class SQL {
  private SQL() {
    // intentionally empty
  }

  public static void close(final Statement statement) {
    try {
      if (null != statement) statement.close();
    } catch (SQLException ignore) {
      log().error("exception closing statement", ignore);
    }
  }

  public static void close(final ResultSet resultSet) {
    try {
      if (null != resultSet) resultSet.close();
    } catch (SQLException ignore) {
      log().error("exception closing result set", ignore);
    }
  }

  public static void close(final Statement statement, final ResultSet resultSet) {
    close(resultSet);
    close(statement);
  }

  public static void close(final Connection connection) {
    try {
      if (null != connection) connection.close();
    } catch (SQLException ignore) {
      log().error("exception closing connection", ignore);
    }
  }

  public static List<Object> list(final DataSource dataSource, final String sql, final Object parameter) throws SQLException {
    return list(dataSource, sql, new Object[]{parameter});
  }

  public static List<Object> list(final DataSource dataSource, final String sql, final Object[] parameters) throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      return list(connection, sql, parameters);
    } catch (SQLException e) {
      log().error(e);
      throw e;
    } finally {
      close(connection);
    }
  }

  public static List<Object> list(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
    ResultSet resultSet = null;
    PreparedStatement statement = null;
    try {
      final List<Object> list = new ArrayList<Object>();
      statement = newPreparedStatement(connection, sql, parameters);
      resultSet = statement.executeQuery();
      final ResultSetMetaData metaData = resultSet.getMetaData();
      final int columnCount = metaData.getColumnCount();
      while (resultSet.next()) {
        switch (columnCount) {
        case 1:
          list.add(resultSet.getObject(1));
          break;
        default:
          final Object[] row = new Object[columnCount];
          for (int i = 0; i < columnCount; ++i) {
            row[i] = resultSet.getObject(i + 1);
          }
          list.add(row);
          break;
        }
      }
      return list;
    } catch (SQLException e) {
      log().error("unable to execute: " + asString(sql, parameters), e);
      throw e;
    } finally {
      close(statement, resultSet);
    }
  }

  public static Map<String, List<Object>> map(final DataSource dataSource, final String sql, final Object[] parameters) throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      return map(connection, sql, parameters);
    } catch (SQLException e) {
      log().error(e);
      throw e;
    } finally {
      close(connection);
    }
  }

  public static Map<String, List<Object>> map(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
    ResultSet resultSet = null;
    PreparedStatement statement = null;
    try {
      final Map<String, List<Object>> map = new TreeMap<String, List<Object>>();
      statement = newPreparedStatement(connection, sql, parameters);
      resultSet = statement.executeQuery();
      final ResultSetMetaData metaData = resultSet.getMetaData();
      final int columnCount = metaData.getColumnCount();
      for (int i = 0; i < columnCount; ++i) {
        final String columnName = metaData.getColumnName(i);
        map.put(columnName, new ArrayList<Object>());
      }
      while (resultSet.next()) {
        for (int i = 0; i < columnCount; ++i) {
          final String columnName = metaData.getColumnName(i);
          map.get(columnName).add(resultSet.getObject(1));
        }
      }
      return map;
    } catch (SQLException e) {
      log().error("unable to execute: " + asString(sql, parameters), e);
      throw e;
    } finally {
      close(statement, resultSet);
    }
  }

  private static String asString(final Connection connection) throws SQLException {
    final DatabaseMetaData metaData = connection.getMetaData();
    return metaData.getURL() + ":username=" + metaData.getUserName();
  }

  private static String asString(final String sql, final Object[] parameters) {
    final StringBuffer buffer = new StringBuffer(sql);
    if (parameters != null && parameters.length > 0) {
      buffer.append(' ').append(Arrays.asList(parameters).toString());
    }
    return buffer.toString();
  }

  private static Log log() {
    return LogFactory.getLog(SQL.class);
  }

  public static int update(final DataSource dataSource, final String sql) throws SQLException {
    return update(dataSource, sql, null);
  }

  public static int update(final DataSource dataSource, final String sql, final Object[] parameters) throws SQLException {
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      return update(connection, sql, parameters);
    } catch (SQLException e) {
      log().error(e);
      throw e;
    } finally {
      close(connection);
    }
  }

  public static int update(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
    PreparedStatement statement = null;
    try {
      statement = newPreparedStatement(connection, sql, parameters);
      return statement.executeUpdate();
    } catch (SQLException e) {
      log().error("unable to execute " + sql + asString(sql, parameters), e);
      throw e;
    } finally {
      close(statement);
    }
  }

  private static PreparedStatement newPreparedStatement(final Connection connection, final String sql, final Object[] parameters) throws SQLException {
    if (log().isDebugEnabled()) {
      log().debug(asString(connection));
      log().debug(asString(sql, parameters));
    }
    final PreparedStatement statement = connection.prepareStatement(sql);
    if (parameters != null) {
      for (int i = 0; i < parameters.length; ++i) {
        statement.setObject(i + 1, parameters[i]);
      }
    }
    return statement;
  }
}

