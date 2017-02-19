package chapter.eight;

import chapter.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.PrimitiveIterator;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ScannerStream {

    private final Scanner scanner;

    private class LinesIterator implements Iterator<String> {

        @Override
        public boolean hasNext() {
            return scanner.hasNextLine();
        }

        @Override
        public String next() {
            return scanner.nextLine();
        }
    }

    private class IntegerIterator implements PrimitiveIterator.OfInt {

        @Override
        public boolean hasNext() {
            while (!scanner.hasNextInt() && scanner.hasNext()) scanner.next();
            return scanner.hasNextInt();
        }

        @Override
        public int nextInt() {
            return scanner.nextInt();
        }
    }

    private class DoubleIterator implements PrimitiveIterator.OfDouble {

        @Override
        public double nextDouble() {
            return scanner.nextDouble();
        }

        @Override
        public boolean hasNext() {
            while (!scanner.hasNextDouble() && scanner.hasNext()) scanner.next();
            return scanner.hasNextDouble();
        }
    }

    ScannerStream(final String fileName) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        Path file = Paths.get(classLoader.getResource(fileName).getFile());
        scanner = new Scanner(file);
        scanner.useDelimiter("[\\s.,]+");
    }

    Stream<String> words() {
        Spliterator<String> spliterator = createSpliterator(scanner);
        return getStream(spliterator);
    }

    Stream<String> lines() {
        Spliterator<String> spliterator = createSpliterator(new LinesIterator());
        return getStream(spliterator);
    }

    IntStream integers() {
        Spliterator.OfInt spliterator = Spliterators.spliteratorUnknownSize(new IntegerIterator(),
                Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.intStream(spliterator, false)
                .onClose(scanner::close);
    }

    DoubleStream doubles() {
        Spliterator.OfDouble spliterator = Spliterators.spliteratorUnknownSize(new DoubleIterator(),
                Spliterator.ORDERED | Spliterator.NONNULL);
        return StreamSupport.doubleStream(spliterator, false)
                .onClose(scanner::close);
    }

    private <T> Stream<T> getStream(final Spliterator<T> spliterator) {
        return StreamSupport.stream(spliterator, false)
                .onClose(scanner::close);
    }

    private <T> Spliterator<T> createSpliterator(final Iterator<T> iterator) {
        return Spliterators.spliteratorUnknownSize(iterator,
                Spliterator.ORDERED | Spliterator.NONNULL);
    }
}
