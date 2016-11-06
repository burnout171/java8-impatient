import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class Chapter2 {

    void ex1() {
        try {
            List<String> words = getWordsFromFile();
            words.parallelStream()
                    .filter(w -> {
                        if (w.length() > 12) {
                            System.out.println("Filter call with: " + w);
                            return true;
                        }
                        return false;
                    })
                    .limit(5).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getWordsFromFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path file = Paths.get(classLoader.getResource("alice.txt").getFile());
        String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        return Arrays.asList(contents.split("[\\P{L}]+"));
    }

    public static void main(String[] args) {
        Chapter2 ch = new Chapter2();
        ch.ex1();
    }
}
