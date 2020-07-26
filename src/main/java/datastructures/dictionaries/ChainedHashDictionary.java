package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You'll need to define reasonable default values for each of the following three fields
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 10;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 5;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 5;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    IDictionary<K, V>[] chains;

    double loadFactor;

    int chainCapacity;

    int dictSize;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashDictionary(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        dictSize = 0;

        loadFactor = resizingLoadFactorThreshold;

        chainCapacity = chainInitialCapacity;

        chains = makeArrayOfChains(initialChainCount);


    }

    private int handleHashCode(K key, int length) {
        if (key == null) {
            return 0;
        } else {
            return Math.abs(key.hashCode()) % length;
        }
    }

    private IDictionary<K, V>[] resizeDictionary() {

        int newSize = chains.length * 2;
        IDictionary<K, V>[] pairs = makeArrayOfChains(newSize);

        for (int i = 0; i < chains.length; i++) {
            if (chains[i] == null) {
                continue;
            }

            Iterator<KVPair<K, V>> iter = chains[i].iterator();

            while (iter.hasNext()) {
                KVPair<K, V> pair = iter.next();

                int position = handleHashCode(pair.getKey(), newSize);

                if (pairs[position] == null) {
                    pairs[position] = new ArrayDictionary<>(chainCapacity);
                }

                pairs[position].put(pair.getKey(), pair.getValue());
            }
        }
        return pairs;
    }
    /**
     * This method will return a new, empty array of the given size that can contain
     * `IDictionary<K, V>` objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        /*
        Note: You do not need to modify this method. See `ArrayDictionary`'s `makeArrayOfPairs`
        method for more background on why we need this method.
        */
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V get(K key) {
        int pos = handleHashCode(key, chains.length);
        if (chains[pos] != null && chains[pos].containsKey(key)) {
            return chains[pos].get(key);
        }
        throw new NoSuchKeyException();
    }

    @Override
    public V put(K key, V value) {
        V returnValue;

        int position = handleHashCode(key, chains.length);

        if (chains[position] == null) {
            chains[position] = new ArrayDictionary<>(chainCapacity);
        }

        if (!chains[position].containsKey(key)) {
            dictSize++;
        }

        returnValue = chains[position].put(key, value);

        double resize = (double) dictSize / chains.length;

        if (resize >= loadFactor) {
            chains = resizeDictionary();
        }

        return returnValue;
    }

    @Override
    public V remove(K key) {
        int pos = handleHashCode(key, chains.length);

        if (chains[pos] == null) {
            return null;
        }

        if (chains[pos].containsKey(key)) {
            dictSize--;
        }

        return chains[pos].remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        int pos = handleHashCode(key, chains.length);

        if (chains[pos] != null) {
            return chains[pos].containsKey(key);
        }

        return false;
    }

    @Override
    public int size() {
        return dictSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    @Override
    public String toString() {
        // return super.toString();

        //
        //After you've implemented the iterator, comment out the line above and uncomment the line
        //below to get a better string representation for objects in assertion errors and in the
        //debugger.
        //

        return IDictionary.toString(this);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int bucket;
        private Iterator<KVPair<K, V>> chainItr;

        // You may add more fields and constructor parameters

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.bucket = 0;
            this.chainItr = null;

            for (int i = 0; i < this.chains.length; i++) {
                if (chains[i] != null) {
                    bucket = i;
                    chainItr = chains[bucket].iterator();
                    nextChain();
                    break;
                }
            }

        }

        private void nextChain() {
            while (!chainItr.hasNext() && bucket < chains.length - 1){
                bucket++;

                if (chains[bucket] != null) {
                    chainItr = chains[bucket].iterator();
                }
            }
        }

        @Override
        public boolean hasNext() {
             return this.chainItr != null && this.chainItr.hasNext();
        }

        @Override
        public KVPair<K, V> next() {
            if ((chainItr != null) && (chainItr.hasNext())){
                KVPair<K, V> element = chainItr.next();

                nextChain();

                return element;
            }
            throw new NoSuchElementException();
        }
    }
}
