package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeapPriorityQueue<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_ARRAY_SIZE = 10;

    /*
    You MUST use this field to store the contents of your heap.
    You may NOT rename this field or change its type: we will be inspecting it in our secret tests.
    */
    T[] heap;
    IDictionary<T, Integer> indices;
    int size;

    // Feel free to add more fields and constants.

    public ArrayHeapPriorityQueue() {
        heap = makeArrayOfT(DEFAULT_ARRAY_SIZE);
        indices = new ChainedHashDictionary<>();
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type `T`.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        /*
        This helper method is basically the same one we gave you in `ArrayDictionary` and
        `ChainedHashDictionary`.

        As before, you do not need to understand how this method works, and should not modify it in
         any way.
        */
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        handleEmptyContainer();

        T returnItem = heap[0];
        indices.remove(heap[0]);

        if (size < 2) {
            heap[0] = null;
            size--;
        } else {
            indices.put(heap[size - 1], 0);
            heap[0] = heap[size - 1];
            heap[size - 1] = null;
            size--;
            percolateDown(0);
        }

        return returnItem;
    }

    @Override
    public T peekMin() {
        handleEmptyContainer();
        return heap[0];
    }

    @Override
    public void add(T item) {
        handleIllegalArgument(item);
        handleInvalidElement(item);

        if (size == heap.length) {
            T[] newHeap = makeArrayOfT(size * 2);
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }

        heap[size] = item;

        indices.put(item, size);

        size++;

        percolate(size - 1);
    }



    @Override
    public boolean contains(T item) {
        handleIllegalArgument(item);
        return indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        handleIllegalArgument(item);
        if (!contains(item)){
            throw new InvalidElementException();
        }

        int index = indices.get(item);

        if (index == 0) {
            removeMin();
        } else if (index == size - 1) {
            indices.remove(item);
            heap[size - 1] = null;
            size--;
        } else {
            indices.remove(item);
            heap[index] = heap[size - 1];
            indices.put(heap[index], index);
            heap[size - 1] = null;
            size--;
            percolate(index);
        }

    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (newItem == null || oldItem == null) {
            throw new IllegalArgumentException();
        }

        if (indices.containsKey(newItem) || !indices.containsKey(oldItem)) {
            throw new InvalidElementException();
        }

        int index = indices.remove(oldItem);

        heap[index] = newItem;

        indices.put(newItem, index);

        percolate(index);
    }

    @Override
    public int size() {
        return size;
    }

    private void handleIllegalArgument(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void handleInvalidElement(T item) {
        if (indices.containsKey(item)) {
            throw new InvalidElementException();
        }
    }
    private void handleEmptyContainer() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
    }


    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        if (index != 0) {
            int parentIndex = (index - 1) / 4;

            if (heap[parentIndex].compareTo(heap[index]) > 0) {
                swap(parentIndex, index);
                percolateUp(parentIndex);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        if (4*index + 1 < size) {
            int smallestChild = findSmallestChild(4 * index + 1, 4 * index + 2, 4 * index + 3, 4 * index + 4);
            if (heap[index].compareTo(heap[smallestChild]) > 0) {
                swap(index, smallestChild);
                percolateDown(smallestChild);
            }
        }

    }

    private int findSmallestChild(int a, int b, int c, int d) {
        int compareABCD = a;

        if (b < size && heap[a].compareTo(heap[b]) > 0) {
            compareABCD = b;
        }

        if (c < size && heap[compareABCD].compareTo(heap[c]) > 0) {
            compareABCD = c;
        }

        if (d < size && heap[compareABCD].compareTo(heap[d]) > 0) {
            compareABCD = d;
        }

        return compareABCD;
    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (index == 0) {
            percolateDown(index);
        } else {
            int parentIndex = (index - 1) / 4;

            if (heap[index].compareTo(heap[parentIndex]) < 0) {
                percolateUp(index);
            } else {
                percolateDown(index);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the `heap` array.
     */
    private void swap(int a, int b) {
        indices.put(heap[a], b);
        indices.put(heap[b], a);

        T temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    @Override
    public String toString() {
        return IPriorityQueue.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayHeapIterator<>(this.heap, this.size());
    }

    private static class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T> {
        private final T[] heap;
        private final int size;
        private int index;

        ArrayHeapIterator(T[] heap, int size) {
            this.heap = heap;
            this.size = size;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T output = heap[this.index];
            this.index++;
            return output;
        }
    }
}
