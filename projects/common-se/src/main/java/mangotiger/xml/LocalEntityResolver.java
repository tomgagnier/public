package mangotiger.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * This class, for use with the JDOM framework, is an implementation of EntityResolver that looks in a local directory
 * to resolve an entity whose public ID is known to the class. If it's not know, this class handles proxies and
 * authenticating to proxies when going out the the web to resolve an entity.<br><br> <p/> The file
 * local-entity-resolver.xml holds a mapping from public IDs to local files, making this class expandable without
 * recompiling.<br><br>
 */
public final class LocalEntityResolver implements EntityResolver {
  private static final Log LOG = LogFactory.getLog(LocalEntityResolver.class);
  private final Map<String, String> publicIdMappings = new HashMap<String, String>();
  private final Map<String, String> systemIdMappings = new HashMap<String, String>();

  /** Default Constructor. Loads property file "local-entity-resolver.xml". */
  private LocalEntityResolver() throws IOException, JDOMException {
    this(localEntityResolverMap());
  }

  private static InputStream localEntityResolverMap() {
    final String resource = "local-entity-resolver.xml";
    if (LOG.isDebugEnabled()) {
      final URL resURL = LocalEntityResolver.class.getResource(resource);
      LOG.debug("Configuration " + resource + " resolved to resource " + resURL);
    }
    final InputStream is = LocalEntityResolver.class.getResourceAsStream(resource);
    if (is == null) {
      throw new IllegalArgumentException("unable to load resource: " + resource);
    }
    return is;
  }

  private LocalEntityResolver(final InputStream is) throws IOException, JDOMException {
    final EntityResolver entityResolver = new EntityResolver() {
      public InputSource resolveEntity(final String publicId, final String systemId) {
        return new InputSource(getClass().getResourceAsStream("local-entity-resolver.dtd"));
      }

      ;
    };
    final boolean validate = false;
    final SAXBuilder builder = new SAXBuilder(validate);
    builder.setEntityResolver(entityResolver);
    final Document doc = builder.build(is);
    final Element root = doc.getRootElement();
    initMappings(root.getChildren("mapping"));
  }

  private void initMappings(final List mappings) {
    for (Object mapping1 : mappings) {
      final Element mapping = (Element)mapping1;
      addMappings(publicIdMappings, mapping, "public-id");
      addMappings(systemIdMappings, mapping, "system-id");
    }
  }

  private static void addMappings(final Map<String, String> map, final Element mapping, final String key) {
    final String resource = mapping.getChildTextTrim("resource");
    final List ids = mapping.getChildren(key);
    for (Object id1 : ids) {
      final Element idElement = (Element)id1;
      final String id = idElement.getTextTrim();
      map.put(id, resource);
      if (LOG.isDebugEnabled()) {
        LOG.debug(new StringBuilder().append(key).append("-map[").append(id).append("] = ").append(resource));
      }
    }
  }

  /**
   * Allows the application to resolve external entities.<br><br> <p/> The Parser will call this method before opening
   * any external entity except the top-level document entity (including the external DTD subset, external entities
   * referenced within the DTD, and external entities referenced within the document element): the application may
   * request that the parser resolve the entity itself, that it use an alternative URI, or that it use an entirely
   * different input source.<br><br> <p/> Application writers can use this method to redirect external system
   * identifiers to secure and/or local URIs, to look up public identifiers in a catalogue, or to read an entity from a
   * database or other input source (including, for example, a dialog box).<br><br> <p/> If the system identifier is a
   * URL, the SAX parser must resolve it fully before reporting it to the application.<br><br>
   * @param publicId The public identifier of the external entity being referenced, or null if none was supplied.
   * @param systemId The system identifier of the external entity being referenced.
   * @return the input source used by JDom.
   * @throws IOException A Java-specific IO exception, possibly the result of creating a new InputStream or Reader for
   *                     the InputSource
   */
  public InputSource resolveEntity(final String publicId, final String systemId) throws IOException {
    LOG.debug("resolveEntity(" + publicId + ", " + systemId + ')');
    String filename = publicIdMappings.get(publicId);
    if (filename == null) {
      filename = systemIdMappings.get(systemId);
    }
    if (filename != null) {
      LOG.debug("Resolving with local file: " + filename);
      final InputStream is = LocalEntityResolver.class.getResourceAsStream(filename);
      if (is != null) {
        return new InputSource(is);
      }
      LOG.debug("Unable to load local file!");
    }
    if (systemId.startsWith("http://")) {
      LOG.warn("Going out to the internet to resolve entity!");
      final URLConnection connection = new URL(systemId).openConnection();
      connection.setDoOutput(true);
      connection.connect();
      final InputStream stream = connection.getInputStream();
      return new InputSource(stream);
    }
    return null; // use default entity resolution algorithm
  }

  /**
   * Root element from a file validated using the local entity resolver.
   * @param filename the filename of the xml file.
   * @return Root element from a file validated using the local entity resolver.
   */
  public static Element getRootElement(final String filename) throws IOException, JDOMException {
    return getRootElement(new FileInputStream(filename));
  }

  /**
   * Root element from a file validated using the local entity resolver.
   * @param file the xml file to validate.
   * @return Root element from a file validated using the local entity resolver.
   */
  public static Element getRootElement(final File file) throws IOException, JDOMException {
    return getRootElement(new FileInputStream(file));
  }

  /**
   * Root element from a file validated using the local entity resolver.
   * @param is the input stream of the xml file to validate.
   * @return Root element from a file validated using the local entity resolver.
   */
  public static Element getRootElement(final InputStream is) throws IOException, JDOMException {
    final SAXBuilder saxBuilder = new SAXBuilder();
    saxBuilder.setEntityResolver(new LocalEntityResolver());
    final Document doc = saxBuilder.build(is);
    return doc.getRootElement();
  }

  @Override public String toString() {
    return "LocalEntityResolver{publicIdMappings=" + publicIdMappings + ",systemIdMappings=" + systemIdMappings + '}';
  }
}
