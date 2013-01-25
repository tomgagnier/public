package mangotiger.net;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * EncodedDigester is a thin wrapper around Sun Java's MessageDigest with particular convenience for those who want MD5
 * digests Base64Encoder encoded through US-ASCII string representation. These are the defaults and for those who want that, it
 * can be as simple as calling:
 *
 * public static String getEncodedDigest(String toDigest)
 *
 * Since we use defaults, the caller will not even need to worry about exception handling using this method.
 *
 * It is typical, however, to digest a pair of strings -- one known only to us and one known only to the outside party.
 * The former string is called "the secret" and this technique makes it very difficult for anyone to spoof the digested
 * strings. This is highly recommended for systems that store user passwords as digests, for example.
 *
 * In this case, it is recommended that you use:
 *
 * public static String getEncodedDigest(String[] toDigest)
 *
 * passing the secret and the outside party string as a String[]
 *
 * You can additionally select the encoding algorithm (for example, if you want "SHA") or the string byte encoding
 * (though, this will always return a Base64Encoder string anyway), but if you use these methods, exception handling becomes
 * your responsibility.
 */
public class EncodedDigester {
  private static final Log logger = LogFactory.getLog(EncodedDigester.class);

  /** Disallow instantiation -- it's all static */
  private EncodedDigester() {
    // Disallow instantiation -- it's all static
  }

  // default to MD5
  private static String algorithm = "MD5";
  // for xlating chars to bytes
  private static final String stringToBytesEncoding = "US-ASCII";

  /**
   * Utility Method for obtaining a BASE64 encoded MessageDigest while specifying the desired digest algorithm. Note
   * that the algorithm must be understood by Sun's MessageDigest -- we are but a conduit. <p>
   * @param toDigest           - string to digest
   * @param digestingAlgorithm - string of algorithm to use to digest
   * @return BASE64 encoded String of a MessageDigest digested by the specified algorithm
   * @throws java.security.NoSuchAlgorithmException
   *          - when the algorithm is unknown
   * @see MessageDigest
   */
  public static String getEncodedDigest(String toDigest, String digestingAlgorithm) throws java.security.NoSuchAlgorithmException {
    String[] toDigestArray = {toDigest};
    String digest = null;
    try {
      digest = getEncodedDigest(toDigestArray, digestingAlgorithm, stringToBytesEncoding);
    } catch (UnsupportedEncodingException e) {
      // we're using the defaults in this class, if we get exceptions, we should fix 'em in development
      logger.fatal("the default encoding is not supported: " + stringToBytesEncoding);
      throw new RuntimeException(e);
    }
    return digest;
  }

  /**
   * Utility Method for obtaining a BASE64 encoded MessageDigest via default algorithm (MD5).
   * @param toDigest - string to digest
   * @return BASE64 encoded String of a MessageDigest digested by the specified algorithm
   * @see MessageDigest
   */
  public static String getEncodedDigest(String toDigest) {
    String[] toDigestArray = {toDigest};
    String digest = null;
    try {
      digest = getEncodedDigest(toDigestArray, algorithm, stringToBytesEncoding);
    } catch (Exception e) {
      // we're using the defaults in this class, if we get exceptions, we should fix 'em in development
      logger.fatal("if the defaults don't work, it's really darned broken");
      throw new RuntimeException(e);
    }
    return digest;
  }

  /**
   * Utility Method for obtaining a BASE64 encoded MessageDigest via default algorithm (MD5).
   * @param toDigest - array of strings to digest together
   * @return BASE64 encoded String of a MessageDigest digested by the "MD5" algorithm
   * @see MessageDigest
   */
  public static String getEncodedDigest(String[] toDigest) {
    String digest = null;
    try {
      digest = getEncodedDigest(toDigest, algorithm, stringToBytesEncoding);
    } catch (Exception e) {
      // we're using the defaults in this class, if we get exceptions, we should fix 'em in development
      logger.fatal("if the defaults don't work, it's really darned broken");
      throw new RuntimeException(e);
    }
    return digest;
  }

  /**
   * Utility Method for obtaining a BASE64 encoded MessageDigest via specified algorithm.
   *
   * Added a check for null here.
   * @param toDigest    - array of strings to digest
   * @param anAlgorithm - digesting algorithm to use to perform the digest
   * @return BASE64 encoded String of a MessageDigest digested by the specified algorithm
   * @throws java.security.NoSuchAlgorithmException
   *                                      - when the algorithm is unknown
   * @throws UnsupportedEncodingException - when the string-to-byte encoding is not supported
   * @see MessageDigest
   */
  public static String getEncodedDigest(String[] toDigest, String anAlgorithm, String stringEncoding) throws java.security.NoSuchAlgorithmException, java.io.UnsupportedEncodingException {
    String digest = null;
    if (toDigest != null && toDigest.length > 0) {
      try {
        MessageDigest messageDigest = MessageDigest.getInstance(anAlgorithm);
        for (int i = 0; i < toDigest.length && toDigest[i] != null; i++) {
          messageDigest.update(toDigest[i].getBytes(stringEncoding));
        }
        digest = encode(messageDigest.digest());
      } catch (java.security.NoSuchAlgorithmException e) {
        logger.error("SecurityManager.getLicenseKey(): can't get a digest instance", e);
        throw e;
      } catch (java.io.UnsupportedEncodingException e) {
        logger.error("SecurityManager.getLicenseKey(): can't get a digest instance", e);
        throw e;
      }
    }
    return digest;
  }

  //*********************************************************************
  //* Base64Encoder - a simple base64 encoder and decoder.
  //*
  //*     Copyright (c) 1999, Bob Withers - bwit@pobox.com
  //*
  //* This code may be freely used for any purpose, either personal
  //* or commercial, provided the authors copyright notice remains
  //* intact.
  //*********************************************************************

  /**
   * Encodes data via the BASE64 encoding scheme. This is taken from Base64Encoder by Bob Withers (freely used for any purpose)
   * <p> Copyright (c) 1999, Bob Withers - bwit@pobox.com
   * @param data - bytes to BASE64 encode
   * @return String representing BASE64 encoding of argument
   */
  private static String encode(byte[] data) {
    int fillchar = '=';
    String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz" + "0123456789+/";
    int c;
    int len = data.length;
    StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
    for (int i = 0; i < len; ++i) {
      c = (data[i] >> 2) & 0x3f;
      ret.append(cvt.charAt(c));
      c = (data[i] << 4) & 0x3f;
      if (++i < len) c |= (data[i] >> 4) & 0x0f;
      ret.append(cvt.charAt(c));
      if (i < len) {
        c = (data[i] << 2) & 0x3f;
        if (++i < len) c |= (data[i] >> 6) & 0x03;
        ret.append(cvt.charAt(c));
      } else {
        ++i;
        ret.append((char)fillchar);
      }
      if (i < len) {
        c = data[i] & 0x3f;
        ret.append(cvt.charAt(c));
      } else {
        ret.append((char)fillchar);
      }
    }
    return (ret.toString());
  }
}
