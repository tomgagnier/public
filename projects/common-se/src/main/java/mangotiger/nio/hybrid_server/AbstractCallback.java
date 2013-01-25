package mangotiger.nio.hybrid_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mangotiger.nio.ByteBufferSource;
import mangotiger.nio.ByteBuffers;

/**
 * The code shared by both TCP and UDP callbacks.
 * @author tom_gagnier@yahoo.com
 */
abstract class AbstractCallback implements Callback {
  private static final Log LOG = LogFactory.getLog(AbstractCallback.class);
  private final List writeQueue = Collections.synchronizedList(new LinkedList());
  private final ByteBuffer input;
  private final ByteBufferSource pool;
  private final Router router;
  private final Sender sender;

  AbstractCallback(final Sender sender, final ByteBufferSource pool, final Router router) {
    this.sender = sender;
    this.pool = pool;
    this.router = router;
    input = ByteBuffer.allocate(pool.getByteBufferCapacity());
  }

  public final void flushWriteQueue() throws IOException {
    while (writeQueue.size() > 0) {
      final Object queuedWrite = writeQueue.remove(0);
      final ByteBuffer borrowedBuffer = write(queuedWrite);
      pool.returnBuffer(borrowedBuffer);
    }
  }

  public final void read() throws IOException {
    final int bytesRead = readBuffer(input);
    if (bytesRead > 0) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(ByteBuffers.describe(input));
      }
      router.route(sender, input, getRemoteAddress());
    }
  }

  final void queueObject(final Object object) {
    writeQueue.add(object);
  }

  final ByteBuffer copy(final ByteBuffer message) {
    final ByteBuffer copy = pool.borrowBuffer();
    copy.put(message);
    copy.flip();
    message.rewind();
    return copy;
  }

  public final int writeQueueSize() {
    return writeQueue.size();
  }

  abstract ByteBuffer write(Object queuedObject) throws IOException;

  abstract int readBuffer(ByteBuffer buffer) throws IOException;

  abstract InetSocketAddress getRemoteAddress();
}
