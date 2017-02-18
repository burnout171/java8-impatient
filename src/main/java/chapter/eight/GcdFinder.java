package chapter.eight;

class GcdFinder {

    int gcdMod(final int a, final int b) {
        return b == 0 ? a :
                gcdMod(b, ((a % b + Math.abs(b)) % Math.abs(b)));
    }

    int gcdFloorMod(final int a, final int b) {
        return b == 0 ? a :
                gcdFloorMod(b, (Math.floorMod(a, b) + Math.abs(b)) % Math.abs(b));
    }

    int gcbCustomRem(final int a, final int b) {
        return b == 0 ? a :
                gcbCustomRem(b, rem(a, b));
    }

    private int rem(final int a, final int b) {
        int aCopy = Math.abs(a);
        int bCopy = Math.abs(b);

        while (aCopy >= bCopy) aCopy -= bCopy;

        return aCopy;
    }

}
