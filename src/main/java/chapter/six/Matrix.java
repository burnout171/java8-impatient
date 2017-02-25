package chapter.six;

class Matrix {

    private final int[][] array;

    Matrix(final int[][] array) {
        this.array = array;
    }

    Matrix multiply(final Matrix that) {
        int a11 = array[0][0] * that.array[0][0] + array[0][1] * that.array[1][0];
        int a12 = array[0][0] * that.array[0][1] + array[0][1] * that.array[1][1];
        int a21 = array[1][0] * that.array[0][0] + array[1][1] * that.array[1][0];
        int a22 = array[1][0] * that.array[0][1] + array[1][1] * that.array[1][1];
        return new Matrix(new int[][]{ {a11, a12}, {a21, a22} });
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Matrix{\n");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                sb.append(array[i][j]).append(" ");
            }
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}
