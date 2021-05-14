import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.random;
import static java.lang.String.valueOf;

public class ArraySampleGenerator {
    private static final int SIZE = 10_000_000;

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("resources/sample.txt", false));
        for (int i = 0; i < SIZE; ++i) writer.append(valueOf((int) (random() * 1e9))).append("\n");
        writer.close();
    }
}
