package mangotiger.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import mangotiger.lang.Integers;

/**
 * A collection of InetSocketAddress helpers.
 * @author tom_gagnier@yahoo.com
 */
public final class InetSocketAddresses {
  private InetSocketAddresses() {}

  /**
   * Convert a string to an InetSocketAddress.
   * @param inetSocketAddress the InetSocketAddress to convert.
   * @return a new InetSocketAddress.
   * @throws UnknownHostException if an unknown host is contained
   */
  public static InetSocketAddress parseString(final String inetSocketAddress) throws UnknownHostException {
    final String[] parts = inetSocketAddress.split(":");
    if (parts.length != 2) {
      throw new IllegalArgumentException("bad format: " + inetSocketAddress);
    }
    final InetAddress address = InetAddress.getByName(parts[0]);
    final int port = Integer.parseInt(parts[1]);
    return new InetSocketAddress(address, port);
  }

  /**
   * Extract an InetSocketAddress from a ByteBuffer.
   * @param buffer the buffer to extract the address from.
   * @return a new InetSocketAddress.
   * @throws UnknownHostException if an unknown host is contained
   */
  public static InetSocketAddress parseBuffer(final ByteBuffer buffer) throws UnknownHostException {
    final InetAddress address = InetAddresses.parseBuffer(buffer);
    final int port = Integers.toUnsigned(buffer.getShort());
    return new InetSocketAddress(address, port);
  }
}
