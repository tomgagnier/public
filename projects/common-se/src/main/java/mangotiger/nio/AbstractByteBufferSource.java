package mangotiger.nio;

import java.nio.ByteBuffer;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * Holds the byte buffer factory for all implementations of ByteBufferSource.
 * @author tom_gagnier@yahoo.com
 */
public abstract class AbstractByteBufferSource implements ByteBufferSource {
  final int byteBufferCapacity;
  final BasePoolableObjectFactory byteBufferFactory = new BasePoolableObjectFactory() {
    @Override public Object makeObject() {
      return ByteBuffer.allocate(byteBufferCapacity);
    }

    @Override public void passivateObject(final Object obj) {
      ((ByteBuffer)obj).clear();
    }
  };

  /**
   * Construct a new AbstractByteBufferSource using the capacity.
   * @param byteBufferCapacity the capacity of the byte buffers produced by this source.
   */
  AbstractByteBufferSource(final int byteBufferCapacity) {
    this.byteBufferCapacity = byteBufferCapacity;
  }

  public final int getByteBufferCapacity() {
    return byteBufferCapacity;
  }
}
