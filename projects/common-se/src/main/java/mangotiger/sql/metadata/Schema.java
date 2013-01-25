package mangotiger.sql.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.thoughtworks.xstream.XStream;

/**
 * The Harmony metadata schema.
 * @author tom_gagnier@yahoo.com
 */
public final class Schema {
  /** A list of tables holding the metdata infomration. */
  static final String[] TABLE_NAMES = new String[]{"ENTITY_DIRECTORY", "PERSON_NAME", "PERSON", "SECURITY_USER",
      "PICKLIST", "PICKLIST_VALUE", "STRATEGY", "FILTER_CATEGORY", "ENTITY_TYPE", "ENTITY_ATTRIBUTE",
      "ENTITY_TYPE_ASSOCIATION_LHS", "ENTITY_TYPE_ASSOCIATION_RHS", "ATTRIBUTE_FILTER",};
  private final Table[] tables = new Table[TABLE_NAMES.length];

  /**
   * A new schema based on a data source.
   * @param dataSource the data source to use for reading the metadata.
   * @return A new schema based on a data source.
   */
  public static Schema newSchema(final DataSource dataSource) throws SQLException {
    return new Schema(dataSource);
  }

  /**
   * A new schema based on an XML string.
   * @param xml an xml string produced by the toString() method of this class.
   * @return A new schema based on an XML string.
   */
  public static Schema newSchema(final String xml) {
    final XStream xStream = new XStream();
    return (Schema)xStream.fromXML(xml);
  }

  /**
   * A new schema based on an XML string.
   * @param xml an xml file produced by writing toString() method of this class to a file.
   * @return A new schema based on an XML string.
   */
  public static Schema newSchema(final File xml) throws FileNotFoundException {
    final FileReader fileReader = new FileReader(xml);
    final XStream xStream = new XStream();
    return (Schema)xStream.fromXML(fileReader);
  }

  private Schema(final DataSource dataSource) throws SQLException {
    for (int i = 0; i < TABLE_NAMES.length; ++i) {
      final String tableName = TABLE_NAMES[i];
      tables[i] = Table.newTable(dataSource, tableName);
    }
  }

  /** Write the schema to a datasource. */
  public void write(final DataSource dataSource) throws SQLException {
    for (int i = 0; i < tables.length; ++i) {
      tables[i].write(dataSource);
    }
  }

  /** Write a schema to an XML file. */
  public void write(final File xmlFile) throws IOException {
    new XStream().toXML(this, new FileWriter(xmlFile));
  }

  public String toString() {
    return new XStream().toXML(this);
  }
}
