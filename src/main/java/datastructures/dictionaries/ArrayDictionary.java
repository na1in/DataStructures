package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    /*
   Warning:
   You may not rename this field or change its type.
   We will be inspecting it in our secret tests.

   Note: The field below intentionally omits the "private" keyword. By leaving off a specific
   access modifier like "public" or "private" it becomes package-private, which means anything in
   the same package can access it. Since our tests are in the same package, they will be able
   to test this property directly.
    */

    Pair<K, V>[] pairs;
    int size;


    public static final int DEFAULT_INITIAL_CAPACITY = 10;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
       this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayDictionary(int initialCapacity) {
        size = 0;
        pairs = makeArrayOfPairs(initialCapacity);
    }

    /**
     * This method will return a new, empty array of the given size that can contain `Pair<K, V>`
     * objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
         arrays and generics interact. Do not modify this method in any way.
        */
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        if (index(key) != -1) {
            return pairs[index(key)].value;
        }
        throw new NoSuchKeyException();

    }

    @Override
    public V put(K key, V value) {
        if (containsKey(key)){
            V old = pairs[index(key)].value;
            pairs[index(key)].value = value;
            //pairs[index(key)] = new Pair<K, V>(key, value);
            return old;
        }else {
            if (size >= pairs.length){
                Pair<K, V>[] newPairs = makeArrayOfPairs(pairs.length * 2);
                System.arraycopy(pairs, 0, newPairs, 0, size);
                pairs = newPairs;
            }
            pairs[size] = new Pair<>(key, value);
            size++;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (containsKey(key)){
            V value = pairs[index(key)].value;
            pairs[index(key)] = pairs[size-1];
            pairs[size-1]=null;
            size--;
            return value;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return index(key)!=-1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<>(pairs, size());
    }

    @Override
    public String toString() {
        //return super.toString();



        return IDictionary.toString(this);
    }

    private int index(K key){
        for (int i=0; i<size; i++){
            if (key != null && key.equals(pairs[i].key)|| pairs[i].key== key){
                return i;
            }
        }
        return -1;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String toString() {
            return String.format("%s=%s", this.key, this.value);
        }
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {

        // You'll need to add some fields
        Pair<K, V>[] pairs;
        private int size;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs, int size) {
            this.size = size;
            this.pairs = pairs;
        }

        @Override
        public boolean hasNext() {
            return size >0;
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                KVPair<K, V> kv = new KVPair<>(pairs[size-1].key, pairs[size-1].value);
                size--;
                return kv;
            }
        }
    }
}
