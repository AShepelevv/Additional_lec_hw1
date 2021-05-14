import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Integer.parseInt;
import static java.lang.System.nanoTime;

public class Main {
    static int SIZE = 10_000_000;

    public static void main(String[] args) throws InterruptedException, IOException {
        int[] arr = new int[SIZE];
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/sample.txt")));
        for (int i = 0; i < SIZE; ++i) {
            arr[i] = parseInt(reader.readLine());
        }

        SortThread t = new SortThread(arr, 4);
        var startTime = nanoTime();
        t.start();
        t.join();
        System.out.println((nanoTime() - startTime) / 1e9);
    }
}
