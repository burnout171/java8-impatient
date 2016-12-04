package chapter.one;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

interface Collection2<T> extends Collection<T> {
    default void forEachIf(Consumer<T> action, Predicate<T> filter) {
        forEach(i -> {
            if (filter.test(i)) {
                action.accept(i);
            }
        });
    }
}
