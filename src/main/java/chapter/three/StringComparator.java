package chapter.three;

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;

public class StringComparator {

    public enum Options {
        REVERSED,
        IGNORE_CASE,
        IGNORE_SPACES;

        public static EnumSet<Options> choose(final Options... op)  {
            return EnumSet.copyOf(Arrays.asList(op));
        }

        public static EnumSet<Options> all() {
            return choose(REVERSED, IGNORE_CASE, IGNORE_SPACES);
        }
    }

    public static Comparator<String> get(final EnumSet<Options> options) {
        return (first, second) -> {
            if (options.contains(Options.IGNORE_CASE)) {
                first = first.toLowerCase();
                second = second.toLowerCase();
            }
            if (options.contains(Options.IGNORE_SPACES)) {
                first = first.replaceAll("\\s", "");
                second = second.replaceAll("\\s", "");
            }
            return !options.contains(Options.REVERSED) ? first.compareTo(second) : second.compareTo(first);
        };
    }
}
