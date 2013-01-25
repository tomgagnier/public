package mangotiger.net;

import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

/** @author tom_gagnier@yahoo.com */
public class EncodedDigesterTest extends TestCase {
  public void testEncodedDigestStringArray() {
    String expectedDigest = "HI7SUFXt6NJuEWGxIDbBmg==";
    String digested = EncodedDigester.getEncodedDigest(new String[]{"the secret", "the password"});
    assertEquals("digest returned is not the digest expected", expectedDigest, digested);
  }

  public void testEncodedDigestString() {
    String expectedDigest = "HI7SUFXt6NJuEWGxIDbBmg==";
    String digested = EncodedDigester.getEncodedDigest("the secretthe password");
    assertEquals("digest returned is not the digest expected", expectedDigest, digested);
  }

  public void testEncodedDigestStringAlgo() {
    try {
      String expectedDigest = "HI7SUFXt6NJuEWGxIDbBmg==";
      String digested = EncodedDigester.getEncodedDigest("the secretthe password", "MD5");
      assertEquals("digest returned is not the digest expected", expectedDigest, digested);
    } catch (NoSuchAlgorithmException e) {
      fail("unexpected exception");
    }
    try {
      String expectedDigest = "hi0ooL2P/hZJhT5pxg6pFY+Ywho=";
      String digested = EncodedDigester.getEncodedDigest("the secretthe password", "SHA");
      assertEquals("digest returned is not the digest expected", expectedDigest, digested);
    } catch (NoSuchAlgorithmException e) {
      fail("unexpected exception");
    }

//    try {
//      String digested = EncodedDigester.getEncodedDigest("the secretthe password", "DOESNOTEXIST");
//      fail("did not produce expected exception");
//    } catch (NoSuchAlgorithmException e) {
//      // ok. exception expected
//    }
  }
}
