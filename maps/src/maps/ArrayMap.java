package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 8;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!
    private int size;
    private int hasNull;

    /**
     * Constructs a new ArrayMap with default initial capacity.
     */
    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructs a new ArrayMap with the given initial capacity (i.e., the initial
     * size of the internal array).
     *
     * @param initialCapacity the initial capacity of the ArrayMap. Must be > 0.
     */
    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        this.size = 0;
        this.hasNull = -1;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    /**
     * @param key key whose index in entries to be found.
     * @return the index of the key in entries, or -1 if there was no such key.
     */
    private int findKey(Object key) {
        if (size < 1) {
            return -1;
        }
        // edge case: key = null
        if (key == null) {
            return hasNull;
        }
        // normal cases
        for (int i = 0; i < size; i++) {
            if (i != hasNull && entries[i].getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public V get(Object key) {
        int index = findKey(key);
        if (index == -1) {
            return null;
        } else {
            return entries[index].getValue();
        }
    }

    @Override
    public V put(K key, V value) {
        int index = findKey(key);
        if (index == -1) {
            // if the key is not present
            size++;
            if (size > entries.length) {
                // expand the entries by doubling its length
                SimpleEntry<K, V>[] temp = createArrayOfEntries(entries.length * 2);
                System.arraycopy(entries, 0, temp, 0, size - 1);
                entries = temp;
            }
            // put new entry
            if (key == null) {
                hasNull = size - 1;
                entries[hasNull] = new SimpleEntry<>(null, value);
            } else {
                entries[size - 1] = new SimpleEntry<>(key, value);
            }
            return null;
        } else {
            V prev = entries[index].getValue();
            entries[index].setValue(value);
            return prev;
        }
    }

    @Override
    public V remove(Object key) {
        int index = findKey(key);
        if (index == -1) {
            return null;
        } else {
            size--;
            V result = entries[index].getValue();
            entries[index] = entries[size];
            entries[size] = null;
            return result;
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return (findKey(key) != -1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: You may or may not need to change this method, depending on whether you
        // add any parameters to the ArrayMapIterator constructor.
        return new ArrayMapIterator<>(entries, size);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;
        private int curr;
        private int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            if (size < 1) {
                curr = entries.length;
            } else {
                curr = -1;
                this.size = size;
            }
        }

        @Override
        public boolean hasNext() {
            return curr < size - 1;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            curr++;
            return entries[curr];
        }
    }
}
