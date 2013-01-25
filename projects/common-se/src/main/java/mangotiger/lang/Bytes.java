package mangotiger.lang;

import java.net.InetSocketAddress;

/**
 * A collection of methonds for manipulating byte arrays.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"MagicNumber"})
public final class Bytes {
  static final int[] SHIFTS = {0x00, 0x08, 0x10, 0x18};
  static final int[] MASKS = {0xff, 0xff00, 0xff0000, 0xff000000};
  private static final int MAX_INT_BYTES = 4;
  private static final byte[] ZERO_BYTES = new byte[0];

  private Bytes() {}

  /**
   * Convert a byte buffer to an integer (assumes big endian).
   * @param buffer a byte array containing a big endian integer.
   * @return an big endian integer representation of the byte array.
   */
  public static int toInt(final byte[] buffer) {
    return toInt(buffer, 0, MAX_INT_BYTES);
  }

  /**
   * Convert a byte buffer to an integer (assumes big endian).
   * @param buffer a byte array containing a big endian integer.
   * @param offset the offset of the integer's most significant digit.
   * @param length the length of the byte array segment.
   * @return an big endian integer representation of the byte array.
   */
  public static int toInt(final byte[] buffer, final int offset, final int length) {
    final int l = length > buffer.length - offset ? buffer.length - offset : length;
    long value = 0;
    for (int i = 0; i < l; ++i) {
      value <<= 8;
      value |= buffer[i + offset] & MASKS[0];
    }
    return (int)value;
  }

  /**
   * Return a big endian byte representation of an integer.
   * @param integer the integer to convert.
   * @return a big endian representation of integer.
   */
  public static byte[] toBytes(final int integer) {
    final byte[] bytes = new byte[MAX_INT_BYTES];
    for (int i = 0; i < bytes.length; ++i) {
      bytes[i] = (byte)(integer >> SHIFTS[MAX_INT_BYTES - i - 1]);
    }
    return bytes;
  }

  /**
   * Return a big endian byte representation of an integer.
   * @param l the long to convert.
   * @return a big endian representation of integer.
   */
  public static byte[] toBytes(long l) {
    final byte[] bytes = new byte[8];
    for (int i = 0; i < 8; ++i) {
      bytes[7 - i] = (byte)(l & 0xff);
      //noinspection AssignmentToMethodParameter
      l >>= 8;
    }
    return bytes;
  }

  /**
   * Return an array of bytes for a given text in big endian format.
   * @param string the text to convert.
   * @return the bytes of the text.
   */
  public static byte[] toBytes(final String string) {
    if (string == null) {
      return ZERO_BYTES;
    }
    final byte[] bytes = new byte[2 * string.length()];
    for (int i = 0; i < string.length(); ++i) {
      final int c = string.charAt(i);
      bytes[2 * i] = (byte)((c & MASKS[1]) >> SHIFTS[1]);
      bytes[2 * i + 1] = (byte)c;
    }
    return bytes;
  }

  /**
   * Convert an array of bytes to a hexadecimal string representation.
   * @param bytes the byte array to convert.
   * @return a hexadecimal string representation.
   */
  public static String toHex(final byte[] bytes) {
    return toHex(bytes, 0, bytes == null ? 0 : bytes.length);
  }

  /**
   * Convert an array of bytes to a hexadecimal string representation.
   * @param bytes the byte array to convert.
   * @param begin the starting index of the byte array.
   * @param end   then ending index of the byte array.
   * @return a hexadecimal string representation.
   */
  public static String toHex(final byte[] bytes, final int begin, final int end) {
    if (bytes == null) {
      return "null";
    }
    final int b = begin < 0 ? 0 : begin;
    final int e = end > bytes.length ? bytes.length : end;
    if (b >= e) {
      return "";
    }
    final StringBuffer buffer = new StringBuffer(3 * (e - b) - 1);
    for (int i = b; i < e; ++i) {
      if (i != b) {
        buffer.append(' ');
      }
      appendHex(buffer, bytes[i]);
    }
    return buffer.toString();
  }

  /**
   * Convert an Internet socket address to a six byte array.
   * @param address the Internet socket address to convert.
   * @return a six byte array representing the Internet socket address.
   */
  public static byte[] toBytes(final InetSocketAddress address) {
    final byte[] bytes = new byte[6];
    final byte[] source = address.getAddress().getAddress();
    System.arraycopy(source, 0, bytes, 0, source.length);
    bytes[4] = byteAt(address.getPort(), 1);
    bytes[5] = byteAt(address.getPort(), 0);
    return bytes;
  }

  /**
   * Reverse bytes to account for little endian byte ordering.
   * @param bytes The bytes to be reversed.
   * @return The reversed bytes.
   */
  public static byte[] reverseEndian(final byte[] bytes) {
    for (int i = 0; i < bytes.length; i += 2) {
      final byte b = bytes[i];
      bytes[i] = bytes[i + 1];
      bytes[i + 1] = b;
    }
    return bytes;
  }

  /**
   * A byte from the integer, where 0 is the least significate byte.
   * @param integer the integer from wich to extract the byte.
   * @param index   the index of the byte where 0 is the least significant byte.
   * @return The least significate byte.
   */
  public static byte byteAt(final int integer, final int index) {
    return (byte)((integer & MASKS[index]) >> SHIFTS[index]);
  }

  private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  private static void appendHex(final StringBuffer buf, final byte b) {
    appendHex(buf, Integers.toUnsigned(b));
  }

  /**
   * Append a hex representation of a byte to a string buffer.
   * @param buf the buffer to append to.
   * @param b   the byte to represent.
   */
  public static void appendHex(final StringBuffer buf, final int b) {
    buf.append(HEX[(b >> 4) % 16]).append(HEX[b % 16]);
  }
}
