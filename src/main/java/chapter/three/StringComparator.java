package chapter.three;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;

class StringComparator {

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

    public static Comparator<String> getOptionsComparator(final EnumSet<Options> options) {
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

    public static <T> Comparator<T> getLexicographicComparator(final String... fieldNames) {
        return (first, second) -> {
            for (String name : fieldNames) {
                try {
                    Field field = first.getClass().getDeclaredField(name);
                    field.setAccessible(true);

                    T firstValue = (T) field.get(first);
                    T secondValue = (T) field.get(second);

                    if (firstValue == null && secondValue == null) continue;
                    if (firstValue ==null || secondValue == null) return firstValue == null ? 1 : -1;

                    int result = firstValue.toString().compareTo(secondValue.toString());
                    if (result != 0) {
                        return result;
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        };
    }
}
