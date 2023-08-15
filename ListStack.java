package prog04;

import java.util.EmptyStackException;
import java.util.List;
import java.util.ArrayList;

/** Implementation of the interface StackInterface<E> using a List.
*   @author vjm
*/

public class ListStack<E> implements StackInterface<E> {
  // Data Fields
  /** Storage for stack. */
  List<E> list;

  /** Initialize list to an empty List. */
  public ListStack() {
    list = new ArrayList<E>();
  }

  /** Pushes an item onto the top of the stack and returns the item
      pushed.
      @param obj The object to be inserted.
      @return The object inserted.
   */
  public E push (E obj) {
    list.add(obj);
    return obj;
  }

  /**** EXERCISE ****/

  public E pop() {
    if (empty()) {
      throw new EmptyStackException();
    }
    E x = peek();
    list.remove(list.size() - 1);
    return x;
  }

  public E peek() {
    if (empty()) {
      throw new EmptyStackException();
    }
    return list.get(list.size() - 1);
  }

  public boolean empty() {
    return list.size() == 0;
  }
}
