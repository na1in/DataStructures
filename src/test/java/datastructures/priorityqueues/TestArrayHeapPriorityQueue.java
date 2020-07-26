package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestArrayHeapPriorityQueue extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeapPriorityQueue<>();
    }

    /**
     * A helper method for accessing the private array inside an `ArrayHeapPriorityQueue`.
     */
    protected static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return ((ArrayHeapPriorityQueue<T>) heap).heap;
    }

    @Test
    void testAddEmptyInternalArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertThat(array[0], is(3));
    }
    @Test
    void testAddEmpty(){
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(3));
        });
    }

    @Test
    void testAddBasic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(4);
            heap.add(5);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(3));
            assertThat(array[1], is(4));
            assertThat(array[2], is(5));
        });
    }
    @Test
    void testAddBasicPercolate() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(4);
            heap.add(5);
            heap.add(2);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(2));
            assertThat(array[1], is(4));
            assertThat(array[2], is(5));
            assertThat(array[3], is(3));
        });
    }

    @Test
    void testAddBasic3() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(8);
            heap.add(4);
            heap.add(7);
            heap.add(2);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(2));
            assertThat(array[1], is(8));
            assertThat(array[2], is(7));
            assertThat(array[3], is(4));
        });
    }

    @Test
    void testAddBasicPercolateSwapsTwiceCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            for(int i=3; i<=12;i++){
                heap.add(i);
            }
            heap.add(2);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(2));
            assertThat(array[1], is(4));
            assertThat(array[2], is(3));
            assertThat(array[3], is(6));
            assertThat(array[4], is(7));
            assertThat(array[5], is(8));
            assertThat(array[6], is(9));
            assertThat(array[7], is(10));
            assertThat(array[8], is(11));
            assertThat(array[9], is(12));
            assertThat(array[10], is(5));
        });
    }

    @Test
    void testAddBasicPercolateDownsSwapsTwiceCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            for(int i=3; i<=11;i++){
                heap.add(i);
            }
            heap.removeMin();
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(4));
            assertThat(array[1], is(8));
            assertThat(array[2], is(5));
            assertThat(array[3], is(6));
            assertThat(array[4], is(7));
            assertThat(array[5], is(11));
            assertThat(array[6], is(9));
            assertThat(array[7], is(10));
        });
    }



    @Test
    void testAddExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(4);
            assertThrows(IllegalArgumentException.class, () -> heap.add(null));
            assertThrows(InvalidElementException.class, () -> heap.add(4));
        });
    }

    @Test
    void testRemoveMinCheckExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            assertThrows(EmptyContainerException.class, () -> heap.removeMin());
        });
    }

    @Test
    void testRemoveMinBasic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            assertThat(heap.removeMin(), is(3));

        });
    }

    @Test
    void testRemoveMinBasic2() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(4);
            heap.add(0);
            int remove = heap.removeMin();
            int remove2 = heap.removeMin();
            int remove3 = heap.removeMin();

            assertThat(remove, is(0));
            assertThat(remove2, is(3));
            assertThat(remove3, is(4));

        });
    }
    @Test
    void testRemoveMinBasic3() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            int remove = heap.removeMin();
            heap.add(4);
            int remove2 = heap.removeMin();
            heap.add(0);
            int remove3 = heap.removeMin();

            assertThat(remove, is(3));
            assertThat(remove2, is(4));
            assertThat(remove3, is(0));

        });
    }

    @Test
    void testAddRemoveMinArray() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(8);
            heap.add(4);
            heap.add(7);
            heap.add(2);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(2));
            assertThat(array[1], is(8));
            assertThat(array[2], is(7));
            assertThat(array[3], is(4));
            assertThat(heap.removeMin(), is(2));
            assertThat(heap.removeMin(), is(4));
            assertThat(heap.removeMin(), is(7));
        });
    }


    @Test
    void testMinPeekCheckExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            assertThrows(EmptyContainerException.class, () -> heap.peekMin());
        });
    }

    @Test
    void testPeekMinBasic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            int peek = heap.peekMin();
            assertThat(peek, is(3));
        });
    }

    @Test
    void testPeekMinBasic2() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(4);
            heap.add(0);
            assertThat(heap.peekMin(), is(0));
            heap.removeMin();
            assertThat(heap.peekMin(), is(3));
            heap.add(2);
            assertThat(heap.peekMin(), is(2));
        });
    }

    @Test
    void testPeekMinBasic3() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(-1);
            for (int i =0; i<10000; i++){
                heap.add(i);
            }
            heap.remove(-1);
            assertThat(heap.removeMin(), is(0));
        });
    }


    @Test
    void testRemoveBasic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(4);
            Comparable<Integer>[] array = getArray(heap);
            assertThat(array[0], is(3));
            assertThat(array[1], is(4));
            heap.remove(3);
            assertThat(array[0], is(4));

        });
    }
    @Test
    void testRemoveCheckExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            assertThrows(IllegalArgumentException.class, () -> heap.remove(null));
            assertThrows(InvalidElementException.class, () -> heap.remove(4));
        });
    }
    @Test
    void testRemoveBasic2() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            Comparable<Integer>[] array = getArray(heap);
            for (int i=0; i< 100; i++){
                heap.add(i);
            }
            for (int i=0; i<99; i++){
                heap.remove(i);
            }
            assertThat(heap.isEmpty(), is(false));
            assertThat(heap.contains(99), is(true));
            assertThat(heap.contains(54), is(false));
            assertThat(heap.contains(0), is(false));
            assertThat(heap.size(), is(1));

        });
    }
    @Test
    void testRemoveMinStressCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            Comparable<Integer>[] array = getArray(heap);
            for (int i=0; i< 10000; i++){
                heap.add(i);
            }
            for (int i=0; i<10000; i++){
                int peek = heap.peekMin();
                assertThat(peek,is(i));
                int remove = heap.removeMin();
                assertThat(remove,is(i));
            }
        });
    }

    @Test
    void testRemoveAtEnd() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            for (int i=0; i< 100; i++){
                heap.add(i);
            }
            heap.remove(99);
            assertThat(heap.contains(99), is(false));

        });
    }


    @Test
    void testContains() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(4);
            assertThrows(IllegalArgumentException.class, () -> heap.contains(null));
            assertThat(heap.contains(4), is(true));
            heap.removeMin();
            assertThat(heap.contains(5), is(false));
            heap.add(5);
            assertThat(heap.contains(4), is(false));
            assertThat(heap.contains(5), is(true));
        });
    }
    @Test
    void testSize() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            assertThat(heap.size(), is(0));
            heap.add(4);
            assertThat(heap.size(), is(1));
            heap.removeMin();
            assertThat(heap.size(), is(0));

        });
    }

    @Test
    void testReplaceCheckExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IPriorityQueue<Integer> heap = this.makeInstance();
            heap.add(3);
            heap.add(2);
            assertThrows(IllegalArgumentException.class, () -> heap.replace(3, null));
            assertThrows(IllegalArgumentException.class, () -> heap.replace(null, 5));
            assertThrows(IllegalArgumentException.class, () -> heap.replace(null, null));
            assertThrows(InvalidElementException.class, () -> heap.replace(3, 2));
            assertThrows(InvalidElementException.class, () -> heap.replace(4, 2));
        });
    }

    @Test
    void testReplaceBasicSingle() {
        IntPair[] values = IntPair.createArray(new int[][]{{0, 0}});
        IPriorityQueue<IntPair> heap = this.makeInstance();

        for (IntPair value : values) {
            heap.add(value);
        }
        IntPair newValue = new IntPair(5, 5);
        heap.replace(values[0], newValue);
        assertThat(heap.removeMin(), is(newValue));
    }

    @Test
    void testUpdateDecrease() {
        IPriorityQueue<IntPair> heap = this.makeInstance();
        for (int i = 1; i <= 5; i++) {
            heap.add(new IntPair(i, i));
        }

         heap.replace(new IntPair(2, 2), new IntPair(0, 0));

        assertThat(heap.removeMin(), is(new IntPair(0, 0)));
        assertThat(heap.removeMin(), is(new IntPair(1, 1)));
    }

    @Test
    void testUpdateIncrease() {
        IntPair[] values = IntPair.createArray(new int[][]{{0, 0}, {2, 2}, {4, 4}, {6, 6}, {8, 8}});
        IPriorityQueue<IntPair> heap = this.makeInstance();

        for (IntPair value : values) {
            heap.add(value);
        }

        IntPair newValue = new IntPair(5, 5);
        heap.replace(values[0], newValue);

        assertThat(heap.removeMin(), is(values[1]));
        assertThat(heap.removeMin(), is(values[2]));
        assertThat(heap.removeMin(), is(newValue));
    }

}
