package mangotiger.poker.channel;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;
import mangotiger.poker.EventChannel;

/** @author tom.gagnier@acs-inc.com */

@SuppressWarnings({"ClassWithoutToString"})
public class EventChannelImplTest extends TestCase {

  EventChannel channel = new EventChannelImpl();

  public void test() throws Exception {
    final Subscriber subscriber = new Subscriber();
    channel.subscribe(subscriber);
    channel.publish("hello, world!");
    channel.publish("goodnight, sweet prince");
    assertEquals("[hello, world!, goodnight, sweet prince]", subscriber.events.toString());
    subscriber.events.clear();
    channel.cancel(subscriber);
    channel.publish("hello, world!");
    channel.publish("goodnight, sweet prince");
    assertEquals("[]", subscriber.events.toString());
  }

  public void testMethodMatchesEvent() throws Exception {
    assertTrue(EventChannelImpl.methodMatchesEvent(Subscriber.class.getMethod("on", String.class), String.class));
    assertFalse(EventChannelImpl.methodMatchesEvent(Subscriber.class.getMethod("off", String.class), String.class));
  }

  public void testMethodForEvent() throws Exception {
    assertEquals(Subscriber.class.getMethod("on", String.class),
                 EventChannelImpl.methodForEvent(Subscriber.class, String.class));
    assertNull(EventChannelImpl.methodForEvent(Subscriber.class, Double.class));
  }

  public static class Subscriber {

    public final Collection<String> events = new LinkedList<String>();

    public void on(String event) {
      events.add(event);
    }

    public void off(String event) {

    }
  }
}