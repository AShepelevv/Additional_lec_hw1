import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SortThread extends Thread {
    public int[] input;
    static AtomicInteger maxThreadCount;

    public SortThread(int[] input) {
        this.input = input;
    }

    public SortThread(int[] input, int threadCount) {
        this.input = input;
        maxThreadCount = new AtomicInteger(threadCount);
    }

    @Override
    public void run() {
        try {
            sort(input, input.length);
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

        if (maxThreadCount.decrementAndGet() > 0) {
            var t = new SortThread(less);
            t.start();
            sort(greater, greaterI);
            t.join();
        } else {
            sort(less, lessI);
            sort(greater, greaterI);
        }
        merge(less, lessI, equals, equalsI, greater, greaterI, input);
    }

    private void merge(int[] less, int lessSize, int[] equals, int equalsSize, int[] greater, int greaterSize, int[] result) throws InterruptedException {
        for (int i = 0; i < lessSize; ++i) {
            result[i] = less[i];
        }
        for (int i = 0; i < equalsSize; ++i) {
            result[lessSize + i] = equals[i];
        }
        for (int i = 0; i < greaterSize; ++i) {
            result[lessSize + equalsSize + i] = greater[i];
        }
    }
}
