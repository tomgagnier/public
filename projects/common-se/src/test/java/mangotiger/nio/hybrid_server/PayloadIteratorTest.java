package mangotiger.nio.hybrid_server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;

import mangotiger.test.MTestCase;
import mangotiger.lang.Bytes;
import mangotiger.nio.hybrid_server.PayloadIterator;

/**
 * Test @see PayloadIterator.
 * @author tom_gagnier@yahoo.com
 */
@SuppressWarnings({"ClassWithoutToString", "MagicNumber", "UnusedCatchParameter"})
public final class PayloadIteratorTest extends MTestCase {
  private ByteBuffer buffer;

  @Override protected void setUp() throws Exception {
    super.setUp();
    buffer = ByteBuffer.allocate(100);
  }

  /** The PayloadIterator test. */
  public void test() {
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.putInt(1);
    final String wide = "Hello, world!";
    for (int i = 0; i < wide.length(); ++i) {
      buffer.putChar(wide.charAt(i));
    }
    buffer.putChar(':');
    final String narrow = "Goodnight sweet Prince.";
    buffer.put(narrow.getBytes());
    buffer.flip();
    final int sizeofInt = 4;
    buffer.position(sizeofInt);
    final PayloadIterator wi = PayloadIterator.wide(buffer);
    assertTrue(wi.hasNext());
    assertEquals(wide, (String)wi.next());
    buffer.position(wi.position());
    final PayloadIterator ni = PayloadIterator.narrow(buffer);
    assertEquals(narrow, ni.next());
  }

  /** Test a wide string (UTF-16). */
  public void testWide() {
    buffer.put(Bytes.reverseEndian(Bytes.toBytes("mangotiger:Password")));
    buffer.flip();
    final PayloadIterator wi = PayloadIterator.wide(buffer);
    assertEquals("mangotiger", wi.next());
    assertEquals("Password", wi.next());
  }

  /** Test a byte payload. */
  public void testBytePayload() {
    buffer.put("lir:tom:password:hello:world".getBytes()).flip();
    final Iterator payload = PayloadIterator.narrow(buffer);
    assertTrue(payload.hasNext());
    assertEquals("lir", payload.next());
    assertTrue(payload.hasNext());
    assertEquals("tom", payload.next());
    assertTrue(payload.hasNext());
    assertEquals("password", payload.next());
    assertTrue(payload.hasNext());
    assertEquals("hello", payload.next());
    assertTrue(payload.hasNext());
    assertEquals("world", payload.next());
    assertFalse(payload.hasNext());
  }

  /** Test a using simplified chinese unicode encoding. */
  public void testSimplifiedChinese() {
    final byte[] bytes = new byte[]{
        //0    1    2   3     4    5     6    7   8  9    10   11    12   13  14 15  16 17
        108, 105, 114, 58, -121, 101, -114, 127, 58, 0, -121, 101, -114, 127, 58, 0, 53, 0};
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    buf.position(4);
    final Iterator payload = PayloadIterator.wide(buf);
    assertTrue(payload.hasNext());
    assertEquals(new byte[]{101, -121, 127, -114}, Bytes.toBytes(payload.next().toString()));
    assertTrue(payload.hasNext());
    assertEquals(new byte[]{101, -121, 127, -114}, Bytes.toBytes((String)payload.next()));
    assertTrue(payload.hasNext());
    assertEquals("5", payload.next());
    assertFalse(payload.hasNext());
  }

  /** Test an actual use case. */
  public void testActual() {
    final byte[] bytes = new byte[]{115, 99, 105, 58, 8, -113, 8, -114, 8, -113, 8, -114, 58, 49, 57, 50, 46, 49, 54,
                                    56, 46, 48, 46, 49, 48, 50, 58, 104, 117, 109, 97, 110, 109, 97, 108, 101, 46, 109, 115, 104, 58, 49, 58, 72,
                                    77, 67, 72, 48, 48, 49, 95, 71, 111, 97, 116, 101, 101, 46, 116, 103, 97, 58, 40, 69, 41, 72, 77, 67, 69, 48,
                                    48, 49, 46, 116, 103, 97, 58, 72, 77, 67, 84, 95, 78, 66, 82, 95, 80, 84, 84, 115, 104, 105, 114, 116, 46, 116,
                                    103, 97, 58, 72, 77, 67, 76, 95, 76, 76, 66, 95, 71, 114, 101, 121, 83, 104, 111, 114, 116, 115, 46, 116, 103,
                                    97, 58, 72, 97, 105, 114, 66, 111, 110, 101, 78, 101, 119, 48, 49, 58, 72, 77, 95, 104, 97, 105, 114, 95, 99,
                                    104, 105, 110, 101, 115, 101, 79, 110, 101, 46, 109, 115, 104, 58, -52, -52, -52, -52, -52, -52, -52, -52, -52,
                                    -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52, -52,
                                    58, 69, 121, 101, 66, 111, 110, 101, 48, 49, 58, 72, 67, 95, 102, 97, 99, 101, 95, 115, 104, 97, 100, 101, 115,
                                    46, 109, 115, 104, 58, 58, 103, 97, 114, 100, 101, 110, 95, 77, 97, 122, 101, 58};
    final ByteBuffer buf = ByteBuffer.wrap(bytes);
    buf.position(13);
    final Iterator payload = PayloadIterator.narrow(buf);
    assertEquals("192.168.0.102", payload.next());
    assertEquals("humanmale.msh", payload.next());
    assertEquals("1", payload.next());
    assertEquals("HMCH001_Goatee.tga", payload.next());
    assertEquals("(E)HMCE001.tga", payload.next());
    assertEquals("HMCT_NBR_PTTshirt.tga", payload.next());
    assertEquals("HMCL_LLB_GreyShorts.tga", payload.next());
    assertEquals("HairBoneNew01", payload.next());
    assertEquals("HM_hair_chineseOne.msh", payload.next());
    payload.next(); // Unpritable, so skip it.
    assertEquals("EyeBone01", payload.next());
    assertEquals("HC_face_shades.msh", payload.next());
    assertEquals("", payload.next());
    assertEquals("garden_Maze", payload.next());
    assertFalse(payload.hasNext());
  }

  /** Test remove. */
  public void testRemove() {
    try {
      buffer.flip();
      final Iterator i = PayloadIterator.narrow(buffer);
      assertEquals("", i.next());
      i.remove();
      fail();
    } catch (UnsupportedOperationException e) {
      // expected
    }
  }
}
