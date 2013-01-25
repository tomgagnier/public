package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/** Common code shared by both TCP and UDP Channels. */
abstract class AbstractPneChannel implements PneChannel {
  private final InetAddress address;
  private final Selector selector;

  protected AbstractPneChannel() throws IOException {
    address = InetAddress.getLocalHost();
    selector = Selector.open();
  }

  /** Select the next group of ready keys. */
  public final void select() throws IOException {
    if (selector.select(TIMEOUT_MILLISECONDS) == 0) {
      return;
    }
    final Set set = selector.selectedKeys();
    for (Iterator i = set.iterator(); i.hasNext();) {
      final SelectionKey key = (SelectionKey)i.next();
      i.remove();
      try {
        processSelectionKey(key);
      } catch (IOException e) {
        key.cancel();
      }
    }
  }

  final Selector selector() {
    return selector;
  }

  public final void close() throws IOException {
    selector.close();
    closeChannel();
  }

  public final InetSocketAddress getLocalAddress() {
    return new InetSocketAddress(address, getPort());
  }

  @Override public String toString() {
    return getLocalAddress().toString();
  }

  abstract int getPort();

  abstract void closeChannel() throws IOException;

  abstract void processSelectionKey(SelectionKey key) throws IOException;
}
