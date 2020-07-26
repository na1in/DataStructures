package datastructures.disjointsets;


import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;

/**
 * @see IDisjointSets for more details.
 */
public class ArrayDisjointSets<T> implements IDisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    int[] pointers;

    int numPointers = 0;

    IDictionary<T, Integer> arrayRepresent;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public ArrayDisjointSets() {
        pointers = new int[10];
        arrayRepresent = new ChainedHashDictionary<>();

    }

    @Override
    public void makeSet(T item) {

        handleArgumentException(item);

        if (numPointers == pointers.length) {
            int[] newPointers = new int[pointers.length * 2];
            System.arraycopy(pointers, 0, newPointers, 0, pointers.length);
            pointers = newPointers;
        }

        arrayRepresent.put(item, numPointers);

        pointers[numPointers] = -1;

        numPointers++;
    }

    @Override
    public int findSet(T item) {
        handleArgumentException2(item);

        return helperFindSet(arrayRepresent.get(item));
    }

    private int helperFindSet(int a) {
        if (pointers[a] < 0) {
            return a;
        } else {
            pointers[a] = helperFindSet(pointers[a]);
            return pointers[a];
        }
    }

    @Override
    public boolean union(T item1, T item2) {
        handleArgumentException2(item1);
        handleArgumentException2(item2);

        int set1 = findSet(item1);
        int set2 = findSet(item2);

        if (set1 == set2) {
            return false;
        }

        if (pointers[set1] > pointers[set2]) {
            pointers[set1] = set2;
        } else {
            pointers[set2] = set1;
        }

        return true;
    }


    private void handleArgumentException(T item) {
        if (arrayRepresent.containsKey(item)) {
            throw new IllegalArgumentException();
        }
    }

    private void handleArgumentException2(T item) {
        if (!arrayRepresent.containsKey(item)) {
            throw new IllegalArgumentException();
        }
    }


}
