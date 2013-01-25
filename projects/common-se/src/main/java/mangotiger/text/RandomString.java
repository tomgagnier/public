package mangotiger.text;

import java.util.Random;

// This was posted to http://www.experts-exchange.com/Programming/Programming_Languages/Java/Q_20378428.html

/**
 * This class can be used to generate strings of random characters.
 * The length of the generated strings, as well as the charactes the strings
 * are generated with can be specified
 *
 * @see java.util.Random
 * @author Jan Louwerens
 */
public class RandomString
{
   private static final String DEFAULT_SOURCE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

   private Random random = null;
   private String m_SourceChars = null;

   /**
    * Creates a RandomString instance with the source characters including
    * lowecase letters a-z, uppercase letters A-Z, and numbers 0-9
    */
   public RandomString()
   {
      this(DEFAULT_SOURCE_CHARS);
   }

   /**
    * Creates a RandomString instance with the specified source characters.
    * The sourceChars string must not be null and must contain at least one
    * character
    *
    * @param sourceChars a string of characters from which a random string
    *                    can be derived
    * @throws IllegalArgumentException if sourceChars is null or does not
    *                                  contain at least one character
    */
   public RandomString(String sourceChars)
      throws IllegalArgumentException
   {
      if (sourceChars == null)
         throw new IllegalArgumentException("sourceChars must not be null");

      if (sourceChars.length() <= 0)
         throw new IllegalArgumentException("sourceChars length must not be > 0");

      random = new Random();
      m_SourceChars = sourceChars;
   }

   /**
    * Returns a character chosen at random from the pool of sourceChars
    *
    * @return a character chosen at random from the pool of sourceChars
    */
   public char nextChar()
   {
      int index = random.nextInt(m_SourceChars.length());
      return m_SourceChars.charAt(index);
   }

   /**
    * Returns a string of the specified length whose characters are chosen
    * at random from the pool of sourceChars
    *
    * @param length the length of the generated string. must be >= 0
    * @return a string of the specified length whose characters are chosen
    *         at random from the pool of sourceChars
    * @throws IllegalArgumentException if length < 0
    */
   public String nextString(int length)
      throws IllegalArgumentException
   {
      if (length < 0)
         throw new IllegalArgumentException("length must be >= 0");

      StringBuffer results = new StringBuffer(length);
      for (int index = 0; index < length; index++)
         results.append(nextChar());

      return results.toString();
   }

   /**
    * Returns a character chosen at random from the pool of sourceChars.
    * <p/>
    * NOTE: If you wish to generate multiple characters, it is suggested
    *       that you create a RandomString instance and use the nextChar()
    *       method
    *
    * @return a character chosen at random from the pool of sourceChars
    * @see #nextChar()
    */
   public static char getRandomChar()
   {
      return getRandomChar(DEFAULT_SOURCE_CHARS);
   }

   /**
    * Returns a character chosen at random from the pool of sourceChars<br>
    * NOTE: If you wish to generate multiple characters, it is suggested
    *       that you create a RandomString instance and use the nextChar(String)
    *       method
    *
    * @param sourceChars a string of characters from which a random character
    *                    can be chosen
    * @return a character chosen at random from the pool of sourceChars
    * @see #nextChar()
    */
   public static char getRandomChar(String sourceChars)
   {
      RandomString randomString = new RandomString(sourceChars);
      return randomString.nextChar();
   }

   /**
    * Returns a string of the specified length whose characters are chosen
    * at random from the pool of sourceChars<br>
    * NOTE: If you wish to generate multiple strings, it is suggested
    *       that you create a RandomString instance and use the nextString(int)
    *       method
    *
    * @param length the length of the generated string. must be >= 0
    * @return a string of the specified length whose characters are chosen
    *         at random from the pool of sourceChars
    * @throws IllegalArgumentException if length < 0
    * @see #nextString(int)
    */
   public static String getRandomString(int length)
      throws IllegalArgumentException
   {
      return getRandomString(DEFAULT_SOURCE_CHARS, length);
   }

   /**
    * Returns a string of the specified length whose characters are chosen
    * at random from the pool of sourceChars<br>
    * NOTE: If you wish to generate multiple strings, it is suggested
    *       that you create a RandomString instance and use the
    *       nextString(String, int) method
    *
    * @param sourceChars a string of characters from which a random string
    *                    can be derived
    * @param length the length of the generated string. must be >= 0
    * @return a string of the specified length whose characters are chosen
    *         at random from the pool of sourceChars
    * @throws IllegalArgumentException if sourceChars is null or does not
    *                                  contain at least one character, or
    *                                  if length < 0
    * @see #nextString(int)
    */
   public static String getRandomString(String sourceChars, int length)
      throws IllegalArgumentException
   {
      RandomString randomString = new RandomString(sourceChars);
      return randomString.nextString(length);
   }
}
