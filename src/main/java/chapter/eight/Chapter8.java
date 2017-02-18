package chapter.eight;

import chapter.Utils;

import java.util.Random;
import java.util.function.BiFunction;

class Chapter8 {

    Chapter8() {
        Utils.printChapter(Chapter8.class.getSimpleName());
    }

    void ex1() {
        Utils.printExercise(1);

        Integer first = Integer.MAX_VALUE;
        Integer second = Integer.MAX_VALUE;
        System.out.println(unsignedOperation(first, second, Integer::sum));
        System.out.println(unsignedOperation(first, 2, Integer::divideUnsigned));
    }

    void ex2() {
        Utils.printExercise(2);

        Integer value = Integer.MIN_VALUE;
        try {
            Math.negateExact(value);
        } catch (ArithmeticException ex) {
            System.out.printf("Exception '%s' was generated for the value %d\n", ex, value);
        }
    }

    void ex3() {
        Utils.printExercise(3);

        GcdFinder gcd = new GcdFinder();
        int a = 180;
        int b = -150;

        if (gcd.gcdMod(a, b) == gcd.gcdFloorMod(a, b) &&
                gcd.gcdFloorMod(a, b) == gcd.gcbCustomRem(a, b)) {
            System.out.printf("The great common divisor is equal for all functions = %d\n", gcd.gcdMod(a, b));
        } else {
            System.out.printf("Results are different %d, %d, %d",
                    gcd.gcdMod(a, b), gcd.gcdFloorMod(a, b), gcd.gcbCustomRem(a, b));
        }
    }

    void ex4() {
        Utils.printExercise(4);

        Random generator = new CustomSeedRandom().get();
        int index = 0;
        double value;
        do {
            index++;
            value = generator.nextDouble();
        } while (index != 376050 && value != 0);
        System.out.printf("Index %d, value %f\n", index, value);
    }

    private Long unsignedOperation(final Integer first,
                                   final Integer second,
                                   final BiFunction<Integer, Integer, Integer> operation) {
        Integer result = operation.apply(first, second);
        return Integer.toUnsignedLong(result);
    }

    public static void main(String[] args) {
        Chapter8 ch = new Chapter8();
//        ch.ex1();
//        ch.ex2();
//        ch.ex3();
        ch.ex4();
    }
}
