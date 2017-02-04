package chapter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public final class Utils {

    public static final String ALICE = "alice.txt";
    public static final String WAR_AND_PEACE = "war_and_peace.txt";

    public static void printChapter(final String chapter) {
        System.out.printf("** %s started **\n", chapter);
    }

    public static void printExercise(final int number) {
        System.out.printf("-> Exercise%d.\n", number);
    }

    public static List<String> getWordsFromFile(final String name) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        Path file = Paths.get(classLoader.getResource(name).getFile());
        String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        return Arrays.asList(contents.split("[\\P{L}]+"));
    }
}
