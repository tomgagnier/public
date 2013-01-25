package mangotiger.nio;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

/**
 * ByteBufferPool test.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public final class ByteBufferPoolTest extends TestCase {
  private ByteBufferSource pool;
  private ByteBuffer[] buffers;
  private static final int BYTE_BUFFER_CAPACITY = 64;

  @Override protected void setUp() throws Exception {
    super.setUp();
    pool = new PooledByteBufferSource(BYTE_BUFFER_CAPACITY);
    buffers = new ByteBuffer[64];
  }

  /** ByteBufferPool test. */
  public void test() {
    for (int i = 0; i < buffers.length; ++i) {
      buffers[i] = pool.borrowBuffer();
      assertEquals(BYTE_BUFFER_CAPACITY, buffers[i].capacity());
    }
    for (ByteBuffer buffer : buffers) {
      pool.returnBuffer(buffer);
    }
  }
}

