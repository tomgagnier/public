package mangotiger.nio;

import java.nio.ByteBuffer;

import mangotiger.lang.Bytes;
import mangotiger.lang.Integers;

/**
 * A collection of static helpers for @see java.nbs.ByteBuffer.
 * @author tom_gagnier@yahoo.com
 */
public final class ByteBuffers {
  private static final Transform TO_HEX = new HexAscii() {
    public void appendByteRepresentation(final StringBuffer buf, final int b) {
      Bytes.appendHex(buf, b);
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
   * Find the index of a byte value.
   * @param buffer the ByteBuffer to search.
   * @param value  the value to search for.
   * @param begin  the starting index.
   * @param end    the ending index.
   * @return the index if found, else return -1.
   */
  public static int find(final ByteBuffer buffer, final byte value, int begin, int end) {
    if (begin < 0) {
      begin = 0;
    }
    if (end > buffer.limit()) {
      end = buffer.limit();
    }
    for (int i = begin; i < end; ++i) {
      if (buffer.get(i) == value) {
        return i;
      }
    }
    return -1;
  }

  private static char toAscii(final int b) {
    return b >= ' ' && b <= '~' ? (char)b : ' ';
  }

  /**
   * Convert a buffer to a hexadecimal string representation.
   * @param buffer the buffer to convert.
   * @return a hexadecimal string representation.
   */
  public static String toHex(final ByteBuffer buffer) {
    return toHex(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the ByteBuffer as a printable ASCII string. <p/> Prints a printable character if it can,
   * or the hex value of the byte.
   * @param buffer the ByteBuffer to print.
   * @return an ascii representation of the ByteBuffer.
   */
  public static String ascii(final ByteBuffer buffer) {
    return ascii(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string. <p/> Prints a printable character if it can,
   * or the hex value of the byte.
   * @param buffer the byte buffer to print.
   * @return a string representation.
   */
  public static String toString(final ByteBuffer buffer) {
    return toString(buffer, buffer.position(), buffer.limit());
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string. <p/> Prints a printable character if it can,
   * or the hex value of the byte.
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   * @return a string representation.
   */
  public static String toString(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_STRING, begin, end);
  }

  private static String transform(final ByteBuffer buffer, final Transform t, int begin, int end) {
    if (buffer == null || begin >= end) {
      return "";
    }
    if (begin < 0) {
      begin = 0;
    }
    if (end > buffer.limit()) {
      end = buffer.limit();
    }
    final int last = end - 1;
    final StringBuffer sbuf = new StringBuffer(t.getRequiredStringBufferSize(end - begin));
    for (int i = begin; i < end; ++i) {
      final int b = Integers.toUnsigned(buffer.get(i));
      t.appendByteRepresentation(sbuf, b);
      if (i != last) {
        t.appendSpace(sbuf);
      }
    }
    return sbuf.toString();
  }

  /**
   * Copy from postion() to limit() bytes from the original buffer into a new buffer. <p/> After copying, the original
   * buffer is compacted.
   * @param original the original byte buffer.
   * @return a copy of the original.
   */
  public static ByteBuffer copy(final ByteBuffer original) {
    final ByteBuffer copy = ByteBuffer.allocate(original.limit() - original.position());
    return copy(original, copy);
  }

  /**
   * Copy from postion() to limit() bytes from the original buffer into a new buffer. <p/> After copying, the original
   * buffer is compacted.
   * @param original the original byte buffer.
   * @param copy     a copy of the original.
   * @return a copy of the original.
   */
  private static ByteBuffer copy(final ByteBuffer original, final ByteBuffer copy) {
    while (original.remaining() > 0) {
      copy.put(original.get());
    }
    copy.flip();
    original.flip();
    return copy;
  }

  /**
   * Determine if a ByteBuffer slice matches a byte array.
   * @param buffer the buffer.
   * @param offset the starting offset of the buffer.
   * @param array  the byte array.
   * @return true if matched.
   */
  public static boolean matches(final ByteBuffer buffer, final int offset, final byte[] array) {
    if (offset + array.length > buffer.limit()) {
      return false;
    }
    for (int i = 0; i < array.length; ++i) {
      if (buffer.get(i + offset) != array[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get bytes from a ByteBuffer and put into a byte array.
   * @param source the source ByteBuffer.
   * @param offset the starting offset.
   * @param target the target byte array.
   * @return the target byte array.
   */
  public static byte[] get(final ByteBuffer source, final int offset, final byte[] target) {
    for (int i = 0; i < target.length; ++i) {
      target[i] = source.get(i + offset);
    }
    return target;
  }

  /**
   * Put bytes into a buffer from an array.
   * @param target the target ByteBuffer.
   * @param index  the ByteBuffer index.
   * @param source the source byte array.
   */
  public static void put(final ByteBuffer target, final int index, final byte[] source) {
    target.mark();
    target.position(index);
    target.put(source);
    target.reset();
  }

  /**
   * Move bytes from the source buffer to a copy. <p/> The source buffer's moved bytes are compressed in the source
   * buffer. The copy buffer is flipped, and made ready for reading.
   * @param source the byte source
   * @param copy   destination of the moved bytes
   * @param count  the number of bytes to move, starting at index 0
   * @return the copy.
   */
  public static ByteBuffer move(final ByteBuffer source, final ByteBuffer copy, final int count) {
    source.flip();
    for (int i = 0; i < count; ++i) {
      copy.put(source.get());
    }
    copy.flip();
    source.compact();
    return copy;
  }

  /**
   * A description of a ByteBuffer suitable for logging. <p/> This includes position, limit, capacity and both hex and
   * ascii representations.
   * @param buffer the byte buffer to describe.
   * @return a description of the byte buffer.
   */
  public static String describe(final ByteBuffer buffer) {
    return describe(buffer, "");
  }

  /**
   * A description of a ByteBuffer suitable for logging. <p/> This includes position, limit, capacity and both hex and
   * ascii representations.
   * @param buffer the byte buffer to describe.
   * @param prefix a prefix for the description.
   * @return a description of the byte buffer.
   */
  public static String describe(final ByteBuffer buffer, final Object prefix) {
    return describe(buffer, prefix == null ? "null" : prefix.toString());
  }

  /**
   * A description of a ByteBuffer suitable for logging. <p/> This includes position, limit, capacity and both hex and
   * ascii representations.
   * @param buffer the byte buffer to describe.
   * @param prefix a prefix for the description.
   * @return a description of the byte buffer.
   */
  public static String describe(final ByteBuffer buffer, final String prefix) {
    final int size = buffer.position() == 0 ? buffer.limit() : buffer.position();
    final int bufferSize = 2 * TO_ASCII.getRequiredStringBufferSize(size) + 50 + prefix.length();
    final StringBuffer stringBuffer = new StringBuffer(bufferSize);
    stringBuffer.append(prefix);
    if (stringBuffer.length() != 0) {
      stringBuffer.append(": ");
    }
    final String ruler = ruler(size);
    final String hex = toHex(buffer, 0, size);
    final String ascii = ascii(buffer, 0, size);
    stringBuffer.append(buffer.toString());
    final String leader = "\n        (";
    final char trailer = ')';
    stringBuffer.append(leader).append(ruler).append(trailer);
    stringBuffer.append(leader).append(hex).append(trailer);
    stringBuffer.append(leader).append(ascii).append(trailer);
    return stringBuffer.toString();
  }

  /**
   * A byte position ruler for use with ascii() or toHex().
   * @param size the number of positions on the ruler.
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
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   * @return the contents of the byte buffer as a string of hex numbers.
   */
  public static String toHex(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_HEX, begin, end);
  }

  /**
   * Represent the contents of the byte buffer as a printable ASCII string. <p/> Prints a printable character if it can,
   * or the hex value of the byte.
   * @param buffer the byte buffer to print.
   * @param begin  the first byte position.
   * @param end    the byte after the last byte.
   * @return an ascii representation of the ByteBuffer.
   */
  public static String ascii(final ByteBuffer buffer, final int begin, final int end) {
    return transform(buffer, TO_ASCII, begin, end);
  }

  /**
   * A byte position ruler for use with ascii() or toHex().
   * @param buffer the buffer to create a ruler for.
   * @return A byte position ruler for use with ascii() or toHex().
   */
  public static String ruler(final ByteBuffer buffer) {
    return ruler(buffer.position() == 0 ? buffer.limit() : buffer.position());
  }

  private ByteBuffers() {}

  interface Transform {
    void appendByteRepresentation(StringBuffer buf, int b);

    void appendSpace(StringBuffer buf);

    int getRequiredStringBufferSize(int byteBufferSize);
  }

  static abstract class HexAscii implements Transform {
// Interface Transform ...

    public final void appendSpace(final StringBuffer buf) {
      buf.append(' ');
    }

    public final int getRequiredStringBufferSize(final int size) {
      return 3 * size - 1;
    }
  }
}
