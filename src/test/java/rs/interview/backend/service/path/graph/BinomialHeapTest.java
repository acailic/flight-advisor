package rs.interview.backend.service.path.graph;

import org.junit.jupiter.api.Test;
import rs.interview.backend.service.path.heap.BinomialHeap;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BinomialHeapTest {

    private static final long seed = System.currentTimeMillis();

    private final BinomialHeap<Integer> heap;

    public BinomialHeapTest() {
        this.heap = new BinomialHeap<>();
    }


    /**
     * Clears the heap before any test.
     */
   // @BeforeAll
    public void init() {
        heap.clear();
    }

    @Test
    public void testAddAndExtractMinimum() {
        final int sz = 100000;
        final Random rnd = new Random(seed);

        for (int i = 0; i != sz; ++i) {
            Integer ii = rnd.nextInt();
            heap.add(ii, ii);
        }

        Integer prev = null;

        while (!heap.isEmpty()) {
            Integer current = heap.extractMinimum();

            if (prev != null && prev > current) {
                fail("The sequence was not monotonically increasing. " +
                        "Previous: " + prev + ", current: " + current + ".");
            }

            prev = current;
        }
    }

    @Test
    public void testDecreasePriority() {
        for (int i = 10; i != 0; --i) {
            heap.add(i, i);
        }

        heap.decreasePriority(10, -1D);

        assertEquals((Integer) 10, heap.extractMinimum());

        int i = 1;
        while (!heap.isEmpty()) {
            assertEquals((Integer) i, heap.extractMinimum());
            i++;
        }
    }




    @Test
    public void testMin() {
        heap.add(3, 3); // (3, 3)

        assertEquals((Integer) 3, heap.min());

        heap.add(2, 2); // (2, 2) (3, 3)

        assertEquals((Integer) 2, heap.min());

        assertEquals(2, heap.size());

        heap.decreasePriority(3, 1D); // (3, 1) (2, 2)

        assertEquals((Integer) 3, heap.min());

        assertEquals(2, heap.size());

        assertEquals((Integer) 3, heap.extractMinimum()); // (2, 2)

        assertEquals(1, heap.size());

        assertEquals((Integer) 2, heap.min());

        assertEquals((Integer) 2, heap.extractMinimum());

        assertEquals(0, heap.size());

        try {
            heap.min();
            fail("BinomialHeap did not throw on being read while empty.");
        } catch (final NoSuchElementException nsee) {

        }
    }
}
