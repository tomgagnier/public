package mangotiger.nio.hybrid_server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.Iterator;

import mangotiger.lang.Characters;

/**
 * Iterate over a message payload to return either narrow or wide strings.
 * @author tom_gagnier@yahoo.com
 */
public abstract class PayloadIterator implements Iterator {
  private static final char DELIMITER = ':';
  final ByteBuffer buffer;
  int position;

  PayloadIterator(final ByteBuffer buffer) {
    this.buffer = buffer;
  }

  /**
   * A narrow (byte) string iterator.
   * @param buffer the buffer to iterator over.
   * @return an iterator.
   */
  public static PayloadIterator narrow(final ByteBuffer buffer) {
    return new Narrow(buffer);
  }

  /**
   * A wide (char) string iterator.
   * @param buffer the buffer to iterator over.
   * @return an iterator.
   */
  public static PayloadIterator wide(final ByteBuffer buffer) {
    return new Wide(buffer);
  }

  public final boolean hasNext() {
    return position < length();
  }

  public final Object next() {
    if (!hasNext()) {
      return "";
    }
    final int begin = position;
    final int end = findDelimeter();
    position = end + 1;
    final StringBuffer sb = new StringBuffer(end - begin);
    for (int i = begin; i < end; ++i) {
      append(sb, i);
    }
    return sb.toString();
  }

  public final void remove() {
    throw new UnsupportedOperationException();
  }

  /**
   * The current position in the underlying ByteBuffer.
   * @return The current position in the underlying ByteBuffer.
   */
  public abstract int position();

  private int findDelimeter() {
    for (int pos = position; pos < length(); ++pos) {
      if (isDelimiter(pos)) {
        return pos;
      }
    }
    return length();
  }

  abstract int length();

  abstract boolean isDelimiter(int index);

  abstract void append(StringBuffer stringBuffer, int index);

  private static final class Narrow extends PayloadIterator {
    Narrow(final ByteBuffer buffer) {
      super(buffer);
    }

    @Override public int position() {
      return buffer.position() + position;
    }

    @Override int length() {
      return buffer.limit() - buffer.position();
    }

    @Override boolean isDelimiter(final int offset) {
      return buffer.get(offset + buffer.position()) == (byte)DELIMITER;
    }

    @Override void append(final StringBuffer stringBuffer, final int index) {
      stringBuffer.append((char)buffer.get(index + buffer.position()));
    }

    @Override public String toString() {
      return "Narrow{super=" + super.toString() + '}';
    }
  }

  private static final class Wide extends PayloadIterator {
    private final char delimiter;
    private final CharBuffer charBuffer;
    private final ByteOrder order = ByteOrder.LITTLE_ENDIAN;

    Wide(final ByteBuffer buffer) {
      super(buffer);
      charBuffer = buffer.asCharBuffer();
      delimiter = isReverse(buffer) ? DELIMITER : Characters.reverseEndian(DELIMITER);
    }

    private boolean isReverse(final ByteBuffer aBuffer) {
      return order.equals(aBuffer.order());
    }

    @Override public int position() {
      return buffer.position() + 2 * position;
    }

    @Override int length() {
      return charBuffer.length();
    }

    @Override boolean isDelimiter(final int index) {
      return charBuffer.charAt(index) == delimiter;
    }

    @Override void append(final StringBuffer stringBuffer, final int index) {
      final char c = charBuffer.charAt(index);
      if (buffer.order().equals(order)) {
        stringBuffer.append(c);
      } else {
        stringBuffer.append(Characters.reverseEndian(c));
      }
    }

    @Override public String toString() {
      return "Wide{delimiter=" + delimiter + ",charBuffer=" + charBuffer + ",order=" + order + ",super=" +
             super.toString() + '}';
    }
  }

  @Override public String toString() {
    return "PayloadIterator{buffer=" + buffer + ",position=" + position + '}';
  }
}
