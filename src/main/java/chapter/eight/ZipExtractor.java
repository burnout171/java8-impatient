package chapter.eight;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipFile;

class ZipExtractor implements AutoCloseable {

    private static final int BUFFER_SIZE = 2048;

    private static final class CleanFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(final Path file,
                                         final BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir,
                                                  final IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    }

    private final File file;
    private final File outputDirectory;

    ZipExtractor(final String file, final String outputDirectory) {
        this.file = new File(file);
        this.outputDirectory = createOutputDirectory(outputDirectory);
    }

    void extract() {
        byte[] buffer = new byte[BUFFER_SIZE];

        try (ZipFile zipFile = new ZipFile(file)) {
            zipFile.stream().forEach(entry -> {
                String name = entry.getName();
                File destinationFile = new File(outputDirectory, name);
                destinationFile.getParentFile().mkdirs();
                if (!entry.isDirectory()) {
                    try (BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry))) {
                        FileOutputStream fos = new FileOutputStream(destinationFile);
                        try (BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE)) {
                            int currentByte;
                            while ((currentByte = bis.read(buffer, 0 , BUFFER_SIZE)) != -1) {
                                bos.write(buffer, 0, currentByte);
                            }
                            bos.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("%s was extracted to %s\n", file, outputDirectory);
    }

    @Override
    public void close() {
        Path directory = Paths.get(outputDirectory.toURI());
        try {
            Files.walkFileTree(directory, new CleanFileVisitor());
        } catch (IOException e) {
            System.out.printf("Not possible to remove directory %s\n", outputDirectory);
            e.printStackTrace();
        }
        System.out.printf("%s was removed\n", outputDirectory);
    }

    private File createOutputDirectory(final String name) {
        File directory = new File(name);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.printf("%s directory was successfully created\n", name);
            } else {
                System.out.printf("Not possible to create directory %s\n", name);
            }

        }
        return directory;
    }
}
