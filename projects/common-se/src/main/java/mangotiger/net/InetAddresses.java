package mangotiger.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import mangotiger.lang.Integers;

/** Collection of helpers for java.net.InetAddress */
public final class InetAddresses {
  private static final int MAX_UNSIGNED_BYTE = 255;

  private InetAddresses() {}

  /**
   * Convert a text IP address to a network order byte array. This class does absolulely no error checking!
   * @param inetAddress as a text (e.g., 192.168.0.1).
   * @return a network order byte array.
   */
  public static byte[] toBytes(final String inetAddress) {
    final byte[] bytes = new byte[4];
    int begin = 0;
    for (int i = 0; i < bytes.length && begin < inetAddress.length(); ++i) {
      int end = inetAddress.indexOf('.', begin);
      if (end < 0) {
        end = inetAddress.length();
      }
      final int integer = Integer.parseInt(inetAddress.substring(begin, end));
      if (integer > MAX_UNSIGNED_BYTE) {
        throw new IllegalArgumentException(inetAddress + ": ");
      }
      bytes[i] = (byte)integer;
      begin = end + 1;
    }
    return bytes;
  }

  /**
   * Convert a string to an InetAddress.
   * @param address a string representation of an InetAddress.
   * @return a new InetAddress.
   */
  public static InetAddress toInetAddress(final String address) {
    try {
      return InetAddress.getByAddress(address, toBytes(address));
    } catch (UnknownHostException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  /**
   * Convert a ByteBuffer to an InetAddress.
   * @param buffer the buffer to convert.
   * @return a new InetAddress.
   * @throws UnknownHostException if an unknown host is contained
   */
  public static InetAddress parseBuffer(final ByteBuffer buffer) throws UnknownHostException {
    final byte[] address = new byte[4];
    buffer.get(address);
    return InetAddress.getByAddress(address);
  }

  /**
   * Convert an InetAddress byte array to a string.
   * @param address an InetAddress byte array to a string.
   * @return a string representation of the InetAddress byte array.
   */
  public static String toString(final byte[] address) {
    if (address == null) {
      return "null";
    }
    final StringBuffer buf = new StringBuffer(address.length * 4);
    for (int i = 0; i < address.length; ++i) {
      if (i != 0) {
        buf.append('.');
      }
      buf.append(Integers.toUnsigned(address[i]));
    }
    return buf.toString();
  }
}