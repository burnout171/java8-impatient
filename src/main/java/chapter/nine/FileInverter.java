package chapter.nine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

class FileInverter implements AutoCloseable {

    private final Path from;
    private final Path to;

    FileInverter(final Path from, final Path to) {
        this.from = from;
        this.to = to;
    }

    void symbols() throws IOException {
        byte[] bytes = Files.readAllBytes(from);
        int size = bytes.length;

        byte[] inverse = new byte[size];
        for (int i = size - 1, j = 0; j < size; i--, j++) {
            inverse[j] = bytes[i];
        }
        Files.write(to, inverse);
    }

    void lines() throws IOException {
        List<String> lines = Files.readAllLines(from);

        Collections.reverse(lines);
        Files.write(to, lines);
    }

    @Override
    public void close() throws Exception {
        Files.delete(to);
    }
}
