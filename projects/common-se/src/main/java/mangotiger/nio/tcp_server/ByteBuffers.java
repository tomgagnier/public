package mangotiger.nio.tcp_server;

import java.nio.ByteBuffer;

/**
 * A collection of helper methods for ByteBuffer operations.
 *
 * @author tom_gagnier@yahoo.com
 * @see java.nio.ByteBuffer
 */
public final class ByteBuffers {
  private static final int BITS_PER_HEXDIGIT = 4;
  private static final int SIZEOF_HEXDIGIT = 16;
  private static final int SIZEOF_FLUFF = 50;

  static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private static final Transform TO_HEX = new HexAscii() {
    public void appendByteRepresentation(final StringBuffer buf, final int b) {
      buf.append(HEX[(b >> BITS_PER_HEXDIGIT) % SIZEOF_HEXDIGIT]).append(HEX[b % SIZEOF_HEXDIGIT]);
    }
  };

  private static final Transform TO_ASCII = new HexAscii() {
    public void appendByteRepresentation(final StringBuffer buf, final int b) {
      buf.append(' ').append(toAscii(b));
    }
  };

  private static final Transform TO_STRING = new Transform() {
    public void appendByteRepresentation(final StringBuffer buf, final int b) {
      buf.append(toAscii(b));
    }

    public void appendSpace(final StringBuffer buf) {
      // Intentionally empty
    }

    public int getRequiredStringBufferSize(final int size) {
      return size;
    }
  };

  /**
   * Convert a buffer to a hexadecimal string representation.
   *
   * @param buffer the buffer to convert.
   *
   * @return a hexadecimal string representation.
   */
  public static String toHex(final ByteBuffer buffer) {
    return toHex(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the ByteBuffer as a printable ASCII string.
   * <p/>
   * Prints a printable character if it can, or the hex value of the byte.
   *
   * @param buffer the ByteBuffer to print.
   *
   * @return an ascii representation of the ByteBuffer.
   */
  public static String ascii(final ByteBuffer buffer) {
    return ascii(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string.
   * <p/>
   * Prints a printable character if it can, or the hex value of the byte.
   *
   * @param buffer the byte buffer to print.
   *
   * @return a string representation.
   */
  public static String toString(final ByteBuffer buffer) {
    return toString(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string.
   * <p/>
   * Prints a printable character if it can, or the hex value of the byte.
   *
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   *
   * @return a string representation.
   */
  private static String toString(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_STRING, begin, end);
  }

  private static String transform(final ByteBuffer buffer, final Transform t, final int begin, final int end) {
    if (buffer == null || begin >= end) {
      return "";
    }
    if (begin < 0) {
      throw new IllegalStateException("begin < 0");
    }
    if (end > buffer.limit()) {
      throw new IllegalStateException("end > buffer.limit()");
    }
    final int last = end - 1;
    final StringBuffer sbuf = new StringBuffer(t.getRequiredStringBufferSize(end - begin));
    for (int i = begin; i < end; ++i) {
      final int b = buffer.get(i);
      t.appendByteRepresentation(sbuf, b);
      if (i != last) {
        t.appendSpace(sbuf);
      }
    }
    return sbuf.toString();
  }

  /**
   * A description of a ByteBuffer suitable for logging.
   * <p/>
   * This includes position, limit, capacity and both hex and ascii representations.
   *
   * @param buffer the byte buffer to describe.
   *
   * @return a description of the byte buffer.
   */
  public static String describe(final ByteBuffer buffer) {
    return describe(buffer, "");
  }

  /**
   * A description of a ByteBuffer suitable for logging.
   * <p/>
   * This includes position, limit, capacity and both hex and ascii representations.
   *
   * @param buffer the byte buffer to describe.
   * @param prefix a prefix for the description.
   *
   * @return a description of the byte buffer.
   */
  public static String describe(final ByteBuffer buffer, final Object prefix) {
    return describe(buffer, prefix == null ? "null" : prefix.toString());
  }

  /**
   * A description of a ByteBuffer suitable for logging.
   * <p/>
   * This includes position, limit, capacity and both hex and ascii representations.
   *
   * @param buffer the byte buffer to describe.
   * @param prefix a prefix for the description.
   *
   * @return a description of the byte buffer.
   */
  private static String describe(final ByteBuffer buffer, final String prefix) {
    final int size = buffer.position() == 0 ? buffer.limit() : buffer.position();
    final int bufferSize = 3 * TO_ASCII.getRequiredStringBufferSize(size) + SIZEOF_FLUFF + prefix.length();
    final StringBuffer stringBuffer = new StringBuffer(bufferSize);
    stringBuffer.append(prefix);
    if (stringBuffer.length() != 0) {
      stringBuffer.append(": ");
    }
    final String ruler = ruler(size);
    final String hex = toHex(buffer, 0, size);
    final String ascii = ascii(buffer, 0, size);
    final String leader = "\n        (";
    final char trailer = ')';
    stringBuffer.append(buffer.toString());
    stringBuffer.append(leader).append(ruler).append(trailer);
    stringBuffer.append(leader).append(hex).append(trailer);
    stringBuffer.append(leader).append(ascii).append(trailer);
    return stringBuffer.toString();
  }

  /**
   * A byte position ruler for use with ascii() or toHex().
   *
   * @param size the number of positions on the ruler.
   *
   * @return A byte position ruler for use with ascii() or toHex().
   */
  private static String ruler(final int size) {
    final StringBuffer stringBuffer = new StringBuffer(size < 1 ? 0 : size * 3 - 1);
    for (int i = 0; i < size; ++i) {
      final int mark = i % 10;
      stringBuffer.append(i == 0 ? " " : "  ");
      if (mark == 9) {
        stringBuffer.append(' ');
      } else {
        stringBuffer.append(mark);
      }
    }
    return stringBuffer.toString();
  }

  /**
   * Represent the contents of the byte buffer as a string of hex numbers.
   *
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   *
   * @return the contents of the byte buffer as a string of hex numbers.
   */
  private static String toHex(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_HEX, begin, end);
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string.
   * <p/>
   * Prints a printable character if it can, or the hex value of the byte.
   *
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   *
   * @return an ascii representation of the ByteBuffer.
   */
  private static String ascii(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_ASCII, begin, end);
  }

  /**
   * A byte position ruler for use with ascii() or toHex().
   *
   * @param buffer the buffer to create a ruler for.
   *
   * @return A byte position ruler for use with ascii() or toHex().
   */
  public static String ruler(final ByteBuffer buffer) {
    return ruler(buffer.position() == 0 ? buffer.limit() : buffer.position());
  }

  static char toAscii(final int b) {
    return b >= ' ' && b <= '~' ? (char) b : ' ';
  }

  private ByteBuffers() {
    // prevent instantiation
  }

  private interface Transform {
    void appendByteRepresentation(StringBuffer buf, int b);

    void appendSpace(StringBuffer buf);

    int getRequiredStringBufferSize(int byteBufferSize);
  }

  private static abstract class HexAscii implements Transform {
// Interface Transform ...

    public final void appendSpace(final StringBuffer buf) {
      buf.append(' ');
    }

    public final int getRequiredStringBufferSize(final int size) {
      return 3 * size - 1;
    }
  }
}
