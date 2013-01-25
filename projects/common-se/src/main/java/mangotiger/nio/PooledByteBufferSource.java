package mangotiger.nio;

import java.nio.ByteBuffer;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.SoftReferenceObjectPool;

/**
 * The message byte buffer pool.
 * @author tom_gagnier@yahoo.com
 */
public final class PooledByteBufferSource extends AbstractByteBufferSource {
  private final ObjectPool byteBufferPool;

  /**
   * Construct a byte buffer pool.
   * @param byteBufferCapacity the capacity of the byte buffers in the pool.
   */
  public PooledByteBufferSource(final int byteBufferCapacity) {
    super(byteBufferCapacity);
    byteBufferPool = new SoftReferenceObjectPool(byteBufferFactory);
  }

  public ByteBuffer borrowBuffer() {
    try {
      return (ByteBuffer)byteBufferPool.borrowObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void returnBuffer(final ByteBuffer borrowedByteBuffer) {
    try {
      byteBufferPool.returnObject(borrowedByteBuffer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getNumberActive() {
    return byteBufferPool.getNumActive();
  }

  private int getNumberIdle() {
    return byteBufferPool.getNumIdle();
  }

  @Override public String toString() {
    final StringBuffer buf = new StringBuffer();
    buf.append(getClass().getName());
    buf.append("{capacity=").append(byteBufferCapacity);
    buf.append(",active=").append(getNumberActive());
    buf.append(",idle=").append(getNumberIdle());
    buf.append(",total=").append(getNumberIdle() + getNumberActive());
    buf.append('}');
    return buf.toString();
  }
}
