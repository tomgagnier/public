package mangotiger.sql.metadata;

import mangotiger.sql.SimpleDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A simple data source based on Hibernate configuration files.
 * @author tom_gagnier@yahoo.com
 */
public final class HibernateDataSource extends SimpleDataSource {
  private static final Log logger = LogFactory.getLog(HibernateDataSource.class);

  public HibernateDataSource(final String hibernatePropertiesResource) {
    this(loadProperties(hibernatePropertiesResource));
  }

  private HibernateDataSource(final Properties hibernateProperties) {
    super(hibernateProperties.getProperty("hibernate.connection.driver_class"),
          hibernateProperties.getProperty("hibernate.connection.url"),
          hibernateProperties.getProperty("hibernate.connection.username"),
          hibernateProperties.getProperty("hibernate.connection.password"));
  }

  private static Properties loadProperties(final String hibernateProperties) {
    try {
      final InputStream is = HibernateDataSource.class.getResourceAsStream(hibernateProperties);
      if (is == null) {
        throw new IllegalStateException(hibernateProperties + " not found");
      }
      final Properties properties = new Properties();
      properties.load(is);
      return properties;
    } catch (IOException e) {
      final String message = hibernateProperties + " not loaded";
      logger.error(message, e);
      final IllegalStateException exception = new IllegalStateException(message);
      exception.setStackTrace(e.getStackTrace());
      throw exception;
    }
  }

  @Override public String toString() {
    return "HibernateDataSource{super=" + super.toString() + '}';
  }
}
