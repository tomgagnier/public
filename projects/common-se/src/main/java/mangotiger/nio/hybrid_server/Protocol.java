package mangotiger.nio.hybrid_server;

/**
 * Enumeration of protocols supported by PNE.
 * @author tom_gagnier@yahoo.com
 */
public interface Protocol {
  /** TCP. */
  Protocol Tcp = new Protocol() {
    @Override public String toString() {return "TCP";}
  };
  /** UDP. */
  Protocol Udp = new Protocol() {
    @Override public String toString() {return "UDP";}
  };
}
