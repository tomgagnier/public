package mangotiger.sql.metadata;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import javax.sql.DataSource;

import junit.framework.TestCase;

import com.thoughtworks.xstream.XStream;

/** @author tom_gagnier@yahoo.com */
@SuppressWarnings({"ClassWithoutToString"})
public abstract class TableTest extends TestCase {
  private DataSource sourceDataSource = null;
  private DataSource targetDataSource = null;
  private final XStream xStream = new XStream();
  private static final String ENTITY_TYPE = "ENTITY_TYPE";
  private static final String ENTITY_TYPE_ASSOCIATION_LHS = "ENTITY_TYPE_ASSOCIATION_LHS";

  public void todoTestSelect() throws Exception {
    assertSelect(ENTITY_TYPE, "ID");
    assertSelect(ENTITY_TYPE_ASSOCIATION_LHS, "ENTITY_TYPE_ID,ASSOCIATE_TYPE_ID");
  }

  private void assertSelect(final String tableName, final String keys) throws SQLException {
    final String select = "SELECT * FROM " + tableName;
    final Table table = Table.newTable(sourceDataSource, tableName);
    assertEquals(select + (" WHERE " + keys.replaceAll(",", "=? AND ") + "=?"), table.selectRow());
    assertEquals(select + (" ORDER BY " + keys), table.select());
  }

  public void todoTestInsert() throws Exception {
    assertInsert(ENTITY_TYPE,
                 "ID,NAME,DESCRIPTION,TABLE_NAME,INSTANCE_ID,IMPORT_ID,SOURCE_ALLOWS_ANY,TARGET_ALLOWS_ANY,UPDATED_BY,CREATED_BY,CREATED_TIME,UPDATED_TIME,ACTIVE_TIME,INACTIVE_TIME");
    assertInsert(ENTITY_TYPE_ASSOCIATION_LHS,
                 "ENTITY_TYPE_ID,ASSOCIATE_TYPE_ID,UPDATED_BY,CREATED_BY,CREATED_TIME,UPDATED_TIME,ACTIVE_TIME,INACTIVE_TIME");
  }

  private void assertInsert(final String tableName, final String columns) throws SQLException {
    final String parameters = columns.replaceAll("[^,]+", "?");
    final String expect = "INSERT INTO " + tableName + " (" + columns + ") VALUES(" + parameters + ')';
    final Table table = Table.newTable(sourceDataSource, tableName);
    final String actual = table.insert();
    assertEquals(expect, actual);
  }

  public void todoTestUpdate() throws Exception {
    assertUpdate(ENTITY_TYPE,
                 "NAME,DESCRIPTION,TABLE_NAME,INSTANCE_ID,IMPORT_ID,SOURCE_ALLOWS_ANY,TARGET_ALLOWS_ANY,UPDATED_BY,CREATED_BY,CREATED_TIME,UPDATED_TIME,ACTIVE_TIME,INACTIVE_TIME",
                 "ID");
    assertUpdate(ENTITY_TYPE_ASSOCIATION_LHS,
                 "UPDATED_BY,CREATED_BY,CREATED_TIME,UPDATED_TIME,ACTIVE_TIME,INACTIVE_TIME",
                 "ENTITY_TYPE_ID,ASSOCIATE_TYPE_ID");
  }

  private void assertUpdate(final String tableName, final String columns, final String keys) throws SQLException {
    final String setClause = columns == null ? "" : " SET " + columns.replaceAll(",", "=?,") + "=?";
    final String whereClause = keys == null ? "" : " WHERE " + keys.replaceAll(",", "=? AND ") + "=?";
    final String expect = "UPDATE " + tableName + setClause + whereClause;
    final Table table = Table.newTable(sourceDataSource, tableName);
    final String actual = table.update();
    assertEquals(expect, actual);
  }

  public void todoTestWrite() throws Exception {
    final Table sourceTable = Table.newTable(sourceDataSource, ENTITY_TYPE);
    final String sourceXml = sourceTable.toString();
    final Table xmlTable = Table.newTable(sourceXml);
    xmlTable.write(targetDataSource); // this should be an insert
    xmlTable.write(targetDataSource); // this should be an update
    final Table targetTable = Table.newTable(targetDataSource, ENTITY_TYPE);
    final String targetXml = xStream.toXML(targetTable);
    assertEquals(sourceXml, targetXml);
  }

  public void todoTestKeys() throws Exception {
    assertExpectedKeys(ENTITY_TYPE, "[ID]");
    assertExpectedKeys(ENTITY_TYPE_ASSOCIATION_LHS, "[ENTITY_TYPE_ID, ASSOCIATE_TYPE_ID]");
  }

  private void assertExpectedKeys(final String tableName, final String expect) throws SQLException {
    Connection connection = null;
    try {
      connection = sourceDataSource.getConnection();
      final String[] keys = Table.newKeyNames(connection, tableName);
      final String actual = Arrays.asList(keys).toString();
      assertEquals(expect, actual);
    } finally {
      if (connection != null) connection.close();
    }
  }
}