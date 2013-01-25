package mangotiger.lang;

/** @author Tom Gagnier */
public class Exceptions {
  /** Return the ultimate root cause of an exception. */
  public static Throwable rootCause(final Throwable throwable) {
    Throwable cause = throwable;
    while (cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }

  private Exceptions() {
    // prevent construction
  }
}
