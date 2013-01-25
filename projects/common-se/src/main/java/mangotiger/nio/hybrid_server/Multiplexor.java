package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.lang.Haltable;
import mangotiger.nio.ByteBufferSource;
import mangotiger.nio.PooledByteBufferSource;

/**
 * A protocol independent channel multiplexor implementation.
 * @author tom_gagnier@yahoo.com
 */
public class Multiplexor implements Haltable {
  private static final Log LOG = LogFactory.getLog(Multiplexor.class);
  /** The byte buffer pool used for queued writes. */
  protected final ByteBufferSource byteBufferSource;
  private final List<PneChannel> channels = new ArrayList<PneChannel>();

  /**
   * Construct a Multiplexor.
   * @param byteBufferCapacity size of byte buffers.
   */
  protected Multiplexor(final int byteBufferCapacity) {
    byteBufferSource = new PooledByteBufferSource(byteBufferCapacity);
  }

  /** Start the multiplexor. */
  public final void run() {
    final String method = "run()";
    enter(method);
    for (boolean active = true; active;) {
      try {
        runLoop();
        for (final PneChannel pneChannel : channels) {
          pneChannel.select();
        }
      } catch (ClosedSelectorException e) {
        LOG.info(e);
        active = false;
      } catch (Throwable e) {
        LOG.error("", e);
      }
    }
    leave(method);
  }

  /** Hook to allow derived classes to insert logic into the run loop. */
  protected void runLoop() {
  }

  /** Halt the multiplexor. */
  public final void halt() {
    final String method = "close()";
    enter(method);
    for (Object channel : channels) {
      final PneChannel pneChannel = (PneChannel)channel;
      try {
        pneChannel.close();
      } catch (IOException e) {
        LOG.error("", e);
      }
    }
    leave(method);
  }

  @Override public final String toString() {
    final Set<String> set = new TreeSet<String>();
    for (Object channel : channels) {
      final PneChannel pneChannel = (PneChannel)channel;
      final InetSocketAddress localAddress = pneChannel.getLocalAddress();
      set.add(localAddress.toString());
    }
    return new StringBuffer().append(getClass().getName()).append(set)
        .toString();
  }

  /**
   * Add a PneChannel to this multiplexor.
   * @param pneChannel the PneChannel to add.
   */
  protected final void add(final PneChannel pneChannel) {
    channels.add(pneChannel);
  }

  final ByteBufferSource byteBufferSource() {return byteBufferSource;}

  private void leave(final String method) {
    LOG.debug("<--" + getClass().getName() + '.' + method);
  }

  private void enter(final String method) {
    LOG.debug("-->" + getClass().getName() + '.' + method);
  }

  /**
   * The write queue size.
   * @return The write queue size.
   */
  protected final int writeQueueSize() {
    int writeQueueSize = 0;
    for (final PneChannel pneChannel : channels) {
      writeQueueSize += pneChannel.writeQueueSize();
    }
    return writeQueueSize;
  }
}
