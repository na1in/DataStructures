package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;

import java.util.Iterator;

public class Sorter {
    /**
     * This method takes the input list and returns the greatest `k` elements in sorted order, from
     * least to greatest.
     *
     * If the input list contains fewer than `k` elements, return a list containing all
     * `input.length` elements in sorted order.
     *
     * Precondition: `input` does not contain `null`s or duplicate values (according to `equals`)
     * Postcondition: the input list has not been mutated
     *
     * @throws IllegalArgumentException  if k < 0
     * @throws IllegalArgumentException  if input is null
     */
    public static <T extends Comparable<T>> IList<T> topKSort(int k, IList<T> input) {
        /*
        Implementation notes:

        - This static method is a *generic method*. A generic method is similar to a generic class,
          except that the generic parameter is used only within this method.

          You can implement a generic method in basically the same way you implement generic
          classes: just use the `T` generic type as if it were a regular type.

        - You should implement this method by using your `ArrayHeapPriorityQueue` in order to
          achieve O(n log k) runtime.
        */

        if (k < 0) {
            throw new IllegalArgumentException();
        }

        if (input == null) {
            throw new IllegalArgumentException();
        }

        IPriorityQueue<T> heap = new ArrayHeapPriorityQueue<>();

        IList<T> result = new DoubleLinkedList<>();


        Iterator<T> iter = input.iterator();

        for (int i = 0; i < k; i++) {
            if (iter.hasNext()) {
                heap.add(iter.next());
            } else {
                break;
            }
        }

        if (iter.hasNext() && k > 0) {
            for (int i = 0; i < input.size() - k; i++) {
                if (iter.hasNext()) {
                    T nextElement = iter.next();
                    if (heap.peekMin().compareTo(nextElement) < 0) {
                        heap.replace(heap.peekMin(), nextElement);
                    }
                } else {
                    break;
                }
            }
        }
        int size = heap.size();

        for (int i = 0; i < size; i++) {
            T item = heap.removeMin();
            result.add(item);
        }
        return result;


    }
}
