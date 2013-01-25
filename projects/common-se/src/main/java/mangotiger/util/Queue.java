package mangotiger.util;

import java.util.Iterator;

/**
 * Implements a circular queue using an efficient linked list. A maximum size can be set, which will cause queue
 * elements to "drop off" the end of the queue as more elements are inserted.
 * @author tom_gagnier@yahoo.com
 */
public class Queue {
  //private static final Log logger = LogFactory.getLog(Queue.class);
  private Node front;
  private Node rear;
  private int size = 0;
  private int maximumSize = 0;
  private boolean allowDuplicates = true;
  private boolean duplicatesMoveToFront = false;

  /**
   * Return <code>true</code> if the queue is currently empty, other <code>false</code>.
   * @return An indication of whether or not the queue is empty.
   */
  public boolean isEmpty() {
    return (0 == size);
  }

  /**
   * Returns the current size of the queue (the number of elements currently in the queue).
   * @return An integer equal to or greater than zero.
   */
  public int size() {
    return size;
  }

  /**
   * Determines whether or not the queue will accept duplicate objects. If set to <code>true</code> (the default)
   * duplicate objects are allowed. If set to <code>false</code> duplicates will be silently ignored, rather than added
   * to the queue, when invoking {@link #insert(Object)}.
   *
   * <p>Note that duplicate checking is done with the <code>equals()</code> method.
   * @param allowed <code>false</code> if the receiver should not allow duplicate objects in the queue.
   */
  public void setAllowDuplicates(boolean allowed) {
    this.allowDuplicates = allowed;
  }

  /**
   * If {@link #setAllowDuplicates(boolean)} has been disabled (that is, if duplicate elements are not allowed), this
   * option can change the behavior of potential duplicates when detected. When duplicates are not allowed, any attempt
   * to insert a duplicate element fails. However, when duplicates are detected the existing object can move to the
   * front of the queue, in effect "refreshing" it's position and assuming the position of a newly inserted element.
   *
   * <p>Set <code>shouldRevitalize</code> to <code>true</code> if you want this behavior. The default is
   * <code>false</code>.
   */
  public void setRevitalizeElements(boolean shouldRevitalize) {
    duplicatesMoveToFront = shouldRevitalize;
  }

  /**
   * Sets the maximum size, or the maximum element count, that the queue will hold. If there are currently more than
   * <code>maximumSize</code> elements in the queue, those elements will be dropped from the end of the queue.
   * @param maximum The maximum number of elements the queue will accept.
   */
  public void setMaximumSize(int maximum) {
    this.maximumSize = maximum;
    while (maximumSize != 0 && size > maximumSize) remove();
  }

  /**
   * Returns the current maximum allowed size of the queue. Zero is used to indicate an unlimited queue size.
   * @return An integer equal to or greater than zero, where zero indicates no size limit.
   */
  public int maximumSize() {
    return maximumSize;
  }

  /**
   * Inserts the new <code>object</code> into the head of the queue. If the size of the queue grows beyond its maximum
   * size (see {@link #setMaximumSize(int)}) the oldest queue element will be dropped. If it is important that elements
   * not be "lost" from the queue, call {@link #remove()} first.
   * @param object An instance of <code>Object</code> to insert into the head of the queue.
   * @see #remove()
   */
  public void insert(Object object) {
    // First do a duplicate check, if required; this iterates over the queue and, if the same
    // object is found (using an equals() comparison), we return without adding it to the queue.
    if (false == allowDuplicates) {
      // If it's already in at the head (extraction end) of the queue, we're done.
      if (null != rear && object.equals(rear.object)) return;
      Queue.Node iterator = front;

      // Otherwise, we need to iterate over the queue and see if the object is already present.
      while (null != iterator) {
        if (iterator.object.equals(object)) {
          // When we find a duplicate, if revitalization has been turned on we need to move the existing element
          // the front of the queue.
          if (duplicatesMoveToFront) {
            // If the iterator came from the front of the queue, change that.
            if (front == iterator) {
              front = iterator.next;
              if (null != front) front.previous = null;
            }

            // Remove the iterator (the element we are moving) out of the chain.
            if (null != iterator.previous) iterator.previous.next = iterator.next;
            if (null != iterator.next) iterator.next.previous = iterator.previous;

            // Now attach the stale element to the rear (first out) of the queue;
            iterator.next = null;
            iterator.previous = rear;
            if (null != rear) rear.next = iterator;
            rear = iterator;
          }

          // And now just return, we don't want to add another instance to the queue.
          return;
        }
        iterator = iterator.next;
      }
    }

    // Having passed the duplicate check, proceed to create a new node and put it into the head
    // of the queue.
    Queue.Node newNode = new Queue.Node();
    newNode.object = object;
    if (null == rear) {
      front = rear = newNode;
    } else {
      rear.next = newNode;
      newNode.previous = rear;
      rear = newNode;
    }
    size++;

    // Make sure we don't grow the queue too large, if a maximum size has been set.
    while (maximumSize != 0 && size > maximumSize) remove();
  }

  public boolean remove(Object object) {
    boolean removed = false;
    Queue.Node current = front;
    while (null != current) {
      if (current.object.equals(object)) {
        // Remove the current object from the queue
        if (null != current.previous) current.previous.next = current.next;
        if (null != current.next) current.next.previous = current.previous;
        // null it's references so it'll be GCd
        current.next = null;
        current.previous = null;
        current.object = null;
        removed = true;
      }
      current = current.next;
    }
    return removed;
  }

  /**
   * Removes the oldest element in the queue and returns it. If there are no elements in the queue, <code>null</code> is
   * returned.
   * @return An instance of <code>Object</code> (or <code>null</code> if the queue is empty).
   */
  public Object remove() {
    if (size() != 0) {
      Object object = front.object;
      front = front.next;
      front.previous = null;
      if (null == front) rear = null;
      size--;
      return object;
    }
    return null;
  }

  /**
   * Returns an <code>Iterator</code> instance for the current queue. Iteration begins at the head of the queue (the
   * oldest element and therefore most ready to be removed) and progresses to the most recent tail (entry-point) of the
   * queue.
   * @return An instance of <code>Iterator</code>.
   */
  public Iterator iterator() {
    return new QueueIterator(front);
  }

  /**
   * An obscure variation of an iterator that will actually return an inverted iterator that traverses the queue in
   * reverse order. This returns the most recently added elements first, which is typically the opposite of desired
   * behavior with queues.
   *
   * <p>This method can be usefull for displaying the queue in its natural order.
   * @param inverted <code>true</code> if the iterator should be inverted, otherwise <code>false</code> for a typical
   *                 iterator.
   * @return An instance of <code>Iterator</code>.
   */
  public Iterator iterator(boolean inverted) {
    if (inverted) return new QueueIterator(rear, true);
    //else
    return new QueueIterator(front);
  }

  /**
   * An inner container that represents a position in the queue. The <code>Node</code> maintains an object reference and
   * a pointer to a subsequent node in the queue.
   */
  private class Node {
    Object object = null;
    Node next = null;
    Node previous = null;
  }

  /** An <code>Iterator</code> implementation specific to the <code>Queue</code>. */
  private class QueueIterator implements Iterator {
    Queue.Node nextNode;
    boolean reverseDirection = false;

    /**
     * Creates a new <code>Iterator</code> instance for the current queue. Iteration begins at the head of the queue
     * (the most recently added element) and progresses to the end of the queue.
     * @param firstNode The first <code>Queue.Node</code> instance.
     */
    public QueueIterator(Queue.Node firstNode) {
      nextNode = firstNode;
    }

    public QueueIterator(Queue.Node lastNode, boolean reverseDirection) {
      nextNode = lastNode;
      this.reverseDirection = reverseDirection;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return (null != nextNode);
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
      Queue.Node currentNode = nextNode;
      if (reverseDirection) {
        nextNode = nextNode.previous;
      } else {
        nextNode = nextNode.next;
      }
      return currentNode.object;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      throw new UnsupportedOperationException(
          "Cannot remove an element from a Queue instance (use Queue.remove() instead).");
    }
  }
}
