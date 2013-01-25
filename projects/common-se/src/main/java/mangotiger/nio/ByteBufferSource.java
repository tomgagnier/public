package mangotiger.nio;

import java.nio.ByteBuffer;

/**
 * A byte buffer source.
 * @author tom_gagnier@yahoo.com
 */
public interface ByteBufferSource {
  /**
   * Borrow a message buffer from the message pool. <p/> NOTE: All borrowed buffers must be returned.  The use of the
   * following idiom is encouraged!
   * <pre>
   *      void foo() {
   *          ByteBuffer buf = null;
   *          try {
   *              buf = Message.borrowBuffer()
   *              // ...
   *          } finally {
   *              if (buf != null) {
   *              Message.returnBuffer(buf);
   *          }
   *      }
   * </pre>
   * @return a borrowed buffer that MUST be returned.
   */
  ByteBuffer borrowBuffer();

  /**
   * Return a borrowed byte buffer back to the pool.
   * @param borrowedByteBuffer the buffer being returned to the pool.
   */
  void returnBuffer(ByteBuffer borrowedByteBuffer);

  /**
   * The capacity of the byte buffers in this pool.
   * @return The capacity of the byte buffers in this pool.
   */
  int getByteBufferCapacity();
}
