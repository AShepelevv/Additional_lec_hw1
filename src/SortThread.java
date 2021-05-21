import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.arraycopy;
import static java.lang.System.out;

public class SortThread extends Thread {
    public int[] input;
    public int size;
    public int[] result;
    public int resultStartIndex;
    static AtomicInteger maxThreadCount;

    public SortThread(int[] input, int size, int[] result, int resultStartIndex) {
        this.input = input;
        this.size = size;
        this.result = result;
        this.resultStartIndex = resultStartIndex;
    }

    public SortThread(int[] input, int size, int threadCount) {
        this.input = input;
        this.size = size;
        maxThreadCount = new AtomicInteger(threadCount);
    }

    @Override
    public void run() {
        try {
            sort(input, size);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sort(int[] input, int size) throws InterruptedException {
        if (size <= 1) return;
        int pivot = input[new Random().nextInt(size)];
        int[] less = new int[size];
        int[] equals = new int[size];
        int[] greater = new int[size];
        int lessI = 0;
        int equalsI = 0;
        int greaterI = 0;
        for (int j = 0; j < size; ++j) {
            int i = input[j];
            if (i < pivot) {
                less[lessI++] = i;
            } else if (i == pivot) {
                equals[equalsI++] = i;
            } else {
                greater[greaterI++] = i;
            }
        }

        if (size > 100_000 && maxThreadCount.get() > 1 && maxThreadCount.decrementAndGet() > 0) {
            out.println(lessI + " " + equalsI + " " + greaterI);
            var t = new SortThread(less, lessI, input, 0);
            t.start();
            sort(greater, greaterI);
            // Многопоточный merge (less вставляет другой поток)
            for (int i = 0; i < equalsI; ++i) {
                input[lessI + i] = equals[i];
            }
            for (int i = 0; i < greaterI; ++i) {
                input[lessI + equalsI + i] = greater[i];
            }
            t.join();
            maxThreadCount.incrementAndGet();
        } else {
            sort(less, lessI);
            sort(greater, greaterI);
            // Однопоточный merge
            merge(less, lessI, equals, equalsI, greater, greaterI, input);
        }
        // merge в родительский поток
        if (result != null) {
            arraycopy(input, 0, result, resultStartIndex, size);
        }
    }

    private void merge(int[] less, int lessSize, int[] equals, int equalsSize, int[] greater, int greaterSize, int[] result) throws InterruptedException {
        if (lessSize >= 0) arraycopy(less, 0, result, 0, lessSize);
        if (equalsSize >= 0) arraycopy(equals, 0, result, lessSize, equalsSize);
        if (greaterSize >= 0) arraycopy(greater, 0, result, lessSize + equalsSize, greaterSize);
    }

    private void merge(int[] equals, int equalsSize, int[] greater, int greaterSize, int[] result, int start) throws InterruptedException {
        if (equalsSize >= 0) arraycopy(equals, 0, result, start, equalsSize);
        if (greaterSize >= 0) arraycopy(greater, 0, result, start + equalsSize, greaterSize);
    }
}
