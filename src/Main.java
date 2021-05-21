import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static java.lang.System.arraycopy;
import static java.lang.System.nanoTime;
import static java.util.Arrays.sort;

public class Main {
    static int SIZE = 10_000_000;

    public static void main(String[] args) throws InterruptedException, IOException {
        int[] arr = new int[SIZE];
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/sample.txt")));
        for (int i = 0; i < SIZE; ++i) {
            arr[i] = parseInt(reader.readLine());
        }

        int[] arrCopy = new int[SIZE];
        arraycopy(arr, 0, arrCopy, 0, SIZE);
        sort(arrCopy);

        SortThread t = new SortThread(arr, arr.length, 1);
        var startTime = nanoTime();
        t.start();
        t.join();

        System.out.println((nanoTime() - startTime) / 1e9);

//        for (int i : arrCopy) {
//            System.out.print(i + " ");
//        }
//        System.out.println();
//
//        for (int i : arr) {
//            System.out.print(i + " ");
//        }
//        System.out.println();

        boolean ok = true;
        for (int i = 1; i < SIZE; ++i) {
            if (arrCopy[i] != arr[i]) {
                ok = false;
                break;
            }
        }
        System.out.println("\n" + (ok ? "CORRECT" : "INCORRECT"));
    }
}
