package mangotiger.lang;

/**
 * Mechanism to stop runnable threads and release resources.
 * @author tom_gagnier@yahoo.com
 */
public interface Haltable extends Runnable {
  /** Halt the running thread and close all resources. */
  void halt();
}
