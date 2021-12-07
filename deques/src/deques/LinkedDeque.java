package deques;

/**
 * @see Deque
 */
public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back = new Node<>(null);
        front.prev = null;
        back.next = null;
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> temp = new Node<>(item);
        temp.prev = front;
        temp.next = front.next;
        front.next.prev = temp;
        front.next = temp;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> temp = new Node<>(item);
        temp.next = back;
        temp.prev = back.prev;
        back.prev.next = temp;
        back.prev = temp;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> temp = front.next;
        front.next = temp.next;
        temp.next.prev = front;
        return temp.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Node<T> temp = back.prev;
        back.prev = temp.prev;
        temp.prev.next = back;
        return temp.value;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> temp = front;
        int i = -1;
        if (index < size / 2) {
            while (temp.next != null && i < index) {
                temp = temp.next;
                i++;
            }
        } else {
            i = size;
            temp = back;
            while (temp.prev != null && i > index) {
                temp = temp.prev;
                i--;
            }
        }
        return temp.value;
    }

    public int size() {
        return size;
    }
}
