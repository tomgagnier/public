package mangotiger.nio;

import java.nio.ByteBuffer;

import mangotiger.test.MTestCase;

/**
 * Test @see ByteBuffers.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber"})
public final class ByteBuffersTest extends MTestCase {
  private ByteBuffer buffer;
  private static final int MAX_BUFFER_SIZE = 128;
  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

  @Override protected void setUp() throws Exception {
    super.setUp();
    buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
  }

  /** Test ByteBuffers.find(). */
  public void testFind() {
    buffer.put("abcdefghijklmnopqrstuvwxyz78901234567890".getBytes()).flip();
    assertEquals(26, ByteBuffers.find(buffer, (byte)'7', 0, 27));
    assertEquals(-1, ByteBuffers.find(buffer, (byte)'8', 0, 27));
    assertEquals(27, ByteBuffers.find(buffer, (byte)'8', 27, 28));
    assertEquals(26, ByteBuffers.find(buffer, (byte)'7', -1, 2 * MAX_BUFFER_SIZE));
  }

  /** Test ByteBuffers string operations. */
  public void testStringOperations1() {
    buffer.putInt(-1).put("hello, world".getBytes()).flip();
    final String ascii = ByteBuffers.ascii(buffer);
    final String asHex = ByteBuffers.toHex(buffer);
    assertEquals(ascii.length(), asHex.length());
    assertEquals("ff ff ff ff 68 65 6c 6c 6f 2c 20 77 6f 72 6c 64", asHex);
    assertEquals("             h  e  l  l  o  ,     w  o  r  l  d", ascii);
    assertEquals(ascii, ByteBuffers.ascii(buffer, -1, 2 * MAX_BUFFER_SIZE));
  }

  /** Test ByteBuffers string operations. */
  public void testStringOperations2() {
    buffer.putInt(0).put("hello, world".getBytes()).flip();
    assertEquals(buffer.toString(), "00 00 00 00 68 65 6c 6c 6f 2c 20 77 6f 72 6c 64", ByteBuffers.toHex(buffer));
    assertEquals(buffer.toString(), "             h  e  l  l  o  ,     w  o  r  l  d", ByteBuffers.ascii(buffer));
    assertEquals(buffer.toString(),
                 "    hello, world",
                 ByteBuffers.toString(buffer, buffer.position(), buffer.limit()));
    buffer.getInt();
    assertEquals(buffer.toString(),
                 "68 65 6c 6c 6f 2c 20 77 6f 72 6c 64",
                 ByteBuffers.toHex(buffer, buffer.position(), buffer.limit()));
    assertEquals(buffer.toString(),
                 " h  e  l  l  o  ,     w  o  r  l  d",
                 ByteBuffers.ascii(buffer, buffer.position(), buffer.limit()));
    assertEquals(buffer.toString(), "hello, world", ByteBuffers.toString(buffer, buffer.position(), buffer.limit()));
  }

  /** Test ByteBuffers string operations. */
  public void testStringOperations3() {
    buffer.putInt(0);
    buffer.put("var:".getBytes());
    buffer.put(new byte[]{(byte)192, (byte)168, 1, 101});
    final int port = 0x088e;
    buffer.putShort((short)port);
    buffer.put((byte)':');
    buffer.flip();
    assertEquals("    var:   e  :", ByteBuffers.toString(buffer));
    assertEquals("             v  a  r  :           e        :", ByteBuffers.ascii(buffer));
    assertEquals("00 00 00 00 76 61 72 3a c0 a8 01 65 08 8e 3a", ByteBuffers.toHex(buffer));
  }

  /** Test ByteBuffers.copy(). */
  public void testCopy() {
    buffer.put("01234".getBytes()).flip();
    assertEquals("java.nio.HeapByteBuffer[pos=0 lim=5 cap=128]", buffer.toString());
    final ByteBuffer copy = ByteBuffers.copy(buffer);
    assertEquals("30 31 32 33 34", copy);
    assertEquals("30 31 32 33 34", buffer);
    assertEquals("java.nio.HeapByteBuffer[pos=0 lim=5 cap=5]", copy.toString());
    assertEquals("", ByteBuffers.toHex(buffer, 0, buffer.position()));
    assertEquals("java.nio.HeapByteBuffer[pos=0 lim=5 cap=128]", buffer.toString());
    while (copy.remaining() != 0) {
      assertEquals(copy.get(), buffer.get());
    }
  }

  /** Test ByteBuffers.matches(). */
  public void testMatches() {
    buffer.put(ALPHABET.getBytes()).flip();
    assertTrue(ByteBuffers.matches(buffer, 0, "abc".getBytes()));
    assertFalse(ByteBuffers.matches(buffer, 2, "efg".getBytes()));
    assertTrue(ByteBuffers.matches(buffer, 4, "efg".getBytes()));
    assertFalse(ByteBuffers.matches(buffer, 25, "efg".getBytes()));
  }

  /** Test ByteBuffers.move(). */
  public void testMove() {
    for (int bytesToMove = 0; bytesToMove <= ALPHABET.length(); ++bytesToMove) {
      assertMove(bytesToMove);
      buffer.clear();
    }
  }

  private void assertMove(final int bytesToMove) {
    final ByteBuffer copy = ByteBuffer.allocate(buffer.capacity());
    buffer.put(ALPHABET.getBytes());
    assertTrue(ByteBuffers.move(buffer, copy, bytesToMove) == copy);
    assertEquals(copy.toString(), ALPHABET.substring(0, bytesToMove), ByteBuffers.toString(copy));
    assertEquals(buffer.toString(),
                 ALPHABET.substring(bytesToMove),
                 ByteBuffers.toString(buffer, 0, buffer.position()));
  }

  /** Test get(). */
  public void testGet() {
    buffer.put(ALPHABET.getBytes());
    final byte[] target = new byte[3];
    ByteBuffers.get(buffer, 12, target);
    assertEquals("mno".getBytes(), target);
  }

  /** Test put() of byte array. */
  public void testPutByteArray() {
    final byte[] bytes = new byte[]{1, 2, 3, 4, 5};
    buffer.put(bytes);
    buffer.flip();
    ByteBuffers.put(buffer, 2, new byte[]{0, 3, 4});
    assertEquals("01 02 00 03 04", buffer);
  }
}
