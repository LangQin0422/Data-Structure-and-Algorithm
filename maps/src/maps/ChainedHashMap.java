package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.8;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 8;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 3;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;

    private int numPairs;
    private double threshold;
    private int chainCapacity;

    /**
     * Constructs a new ChainedHashMap with default resizing load factor threshold,
     * default initial chain count, and default initial chain capacity.
     */
    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    /**
     * Constructs a new ChainedHashMap with the given parameters.
     *
     * @param resizingLoadFactorThreshold the load factor threshold for resizing. When the load factor
     *                                    exceeds this value, the hash table resizes. Must be > 0.
     * @param initialChainCount the initial number of chains for your hash table. Must be > 0.
     * @param chainInitialCapacity the initial capacity of each ArrayMap chain created by the map.
     *                             Must be > 0.
     */
    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.chains = createArrayOfChains(initialChainCount);
        this.threshold = resizingLoadFactorThreshold;
        this.chainCapacity = chainInitialCapacity;
        this.numPairs = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    /**
     * Returns the adapted hashcode of the key.
     * <p>
     * This method will convert the hashcode by Java to the one adapted to this class.
     */
    private int getHashCode(Object key) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % chains.length;
        }
    }

    @Override
    public V get(Object key) {
        int index = getHashCode(key);
        if (chains[index] != null) {
            return chains[getHashCode(key)].get(key);
        } else {
            return null;
        }
    }

    @Override
    public V put(K key, V value) {
        if (!this.containsKey(key)) {
            numPairs++;
        }
        // resize the hashtable by twice the current size if hit the threshold
        if ((double) (numPairs / chains.length) > threshold) {
            AbstractIterableMap<K, V>[] temp = createArrayOfChains(chains.length * 2);
            for (Entry<K, V> curr : this) {
                int i = Math.abs(curr.getKey().hashCode()) % temp.length;
                if (temp[i] == null) {
                    temp[i] = createChain(chainCapacity);
                }
                temp[i].put(curr.getKey(), curr.getValue());
            }
            this.chains = temp;
        }
        int index = getHashCode(key);
        if (chains[index] == null) {
            chains[index] = createChain(chainCapacity);
        }
        return chains[index].put(key, value);
    }

    @Override
    public V remove(Object key) {
        if (this.containsKey(key)) {
            numPairs--;
            return chains[getHashCode(key)].remove(key);
        } else {
            return null;
        }
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        numPairs = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = getHashCode(key);
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return numPairs;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains, this.numPairs);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private AbstractIterableMap<K, V>[] chains;
        private Iterator<Entry<K, V>> itr;
        private int curr;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains, int numPairs) {
            this.chains = chains;
            if (numPairs > 0) {
                this.itr = null;
                curr = -1;
                do {
                    curr++;
                    if (chains[curr] != null) {
                        this.itr = chains[curr].iterator();
                    }
                } while (chains[curr] == null || !this.itr.hasNext());
            } else {
                curr = -1;
            }
        }

        @Override
        public boolean hasNext() {
            if (curr > -1 && !itr.hasNext()) {
                do {
                    curr++;
                } while (curr < chains.length && chains[curr] == null);
                if (curr < chains.length && chains[curr] != null) {
                    itr = chains[curr].iterator();
                }
            }
            return (curr > -1) && (curr < chains.length) && itr.hasNext();
        }

        @Override
        public Map.Entry<K, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return itr.next();
        }
    }
}
