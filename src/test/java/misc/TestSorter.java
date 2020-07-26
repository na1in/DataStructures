package misc;


import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Duration;

import static datastructures.lists.IListMatcher.listContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestSorter extends BaseTest {
    @Test
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(15, 16, 17, 18, 19)));
        assertThat(list,is(list));
    }

    @Test
    void testSorterExceptions() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 20; i++) {
                list.add(i);
            }
            assertThrows(IllegalArgumentException.class, () -> Sorter.topKSort(-1, list));
            assertThrows(IllegalArgumentException.class, () -> Sorter.topKSort(5, null));
            assertThat(list,is(list));
        });
    }

    @Test
    void testSorterFewerThanK() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 3; i++) {
                list.add(5 * i);
            }
            IList<Integer> top = Sorter.topKSort(5, list);
            assertThat(top, is(listContaining(0, 5, 10)));
            assertThat(list,is(list));

        });
    }

    @Test
    void testSorterBasic() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 5; i++) {
                list.add(10 - i);
            }
            IList<Integer> top = Sorter.topKSort(5, list);
            assertThat(top, is(listContaining(6, 7, 8, 9, 10)));
            assertThat(list,is(list));

        });
    }

    @Test
    void testSorterKZero() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 3; i++) {
                list.add(5 * i);
            }
            IList<Integer> top = Sorter.topKSort(0, list);
            assertThat(top.isEmpty(), is(true));
            assertThat(list,is(list));
        });
    }

    @Test
    void testAlreadySortedCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 10; i++) {
                list.add(i);
            }
            IList<Integer> top = Sorter.topKSort(10, list);
            assertThat(top,is(listContaining(0,1,2,3,4,5,6,7,8,9)));
            assertThat(list,is(list));
        });
    }

    @Test
    void testKequalsNCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 10; i++) {
                list.add(i * i);
            }
             IList<Integer> top = Sorter.topKSort(10, list);
            assertThat(top,is(listContaining(0,1,4,9,16,25,36,49,64,81)));
            assertThat(list,is(list));
         });
    }

    @Test
    void testKIsGreaterThanNCopy() {
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            IList<Integer> list = new DoubleLinkedList<>();
            for (int i = 0; i < 10; i++) {
                list.add(i * i);
            }
            IList<Integer> top = Sorter.topKSort(15, list);
            assertThat(top,is(listContaining(0,1,4,9,16,25,36,49,64,81)));
            assertThat(list,is(list));
        });
    }





}

