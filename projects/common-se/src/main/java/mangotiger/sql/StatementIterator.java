package mangotiger.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Iterate over a series of SQL statements.
 * @author tom_gagnier@yahoo.com
 */
public final class StatementIterator implements Iterator {
  private static final Log LOG = LogFactory.getLog(StatementIterator.class);
  private final String delimiter;
  private final BufferedReader in;
  private String nextStatement;

  /**
   * Construct a SQL statement iterator iterating over the statements inside an input stream.  The statement delimiter
   * is a semi-colon.
   * @param in the input stream containing the SQL statements.
   */
  public StatementIterator(final InputStream in) {
    this.in = new BufferedReader(new InputStreamReader(in));
    delimiter = ";";
    nextStatement = findNext();
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }

  public boolean hasNext() {
    return nextStatement != null;
  }

  public Object next() {
    final String next = nextStatement;
    nextStatement = findNext();
    return next;
  }

  private String findNext() {
    try {
      final StringBuffer sql = new StringBuffer();
      for (String line = in.readLine(); line != null; line = in.readLine()) {
        if (line.trim().length() == 0 || line.startsWith("--")) {
          continue;
        }
        if (sql.length() != 0) {
          sql.append('\n');
        }
        sql.append(line);
        if (sql.toString().endsWith(delimiter)) {
          final int end = sql.length() - delimiter.length();
          return sql.substring(0, end);
        }
      }
      if (sql.length() > 0) {
        return sql.toString();
      }
      in.close();
      return null;
    } catch (IOException e) {
      LOG.error("", e);
      return null;
    }
  }

  @Override public String toString() {
    return "StatementIterator{delimiter='" + delimiter + "',in=" + in + ",nextStatement='" + nextStatement + "'}";
  }
}
