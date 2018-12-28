import java.util.*;

class Board {
    private int[][] matrix = new int[4][4];

    boolean makeMove(int x, int y) {
        if ((y != 3) && (matrix[x][y + 1] == 0)) {
            matrix[x][y + 1] = matrix[x][y];
            matrix[x][y] = 0;
            return true;
        } else if ((y != 0) && (matrix[x][y - 1] == 0)) {
            matrix[x][y - 1] = matrix[x][y];
            matrix[x][y] = 0;
            return true;
        } else if ((x != 3) && (matrix[x + 1][y] == 0)) {
            matrix[x + 1][y] = matrix[x][y];
            matrix[x][y] = 0;
            return true;
        } else if ((x != 0) && (matrix[x - 1][y] == 0)) {
            matrix[x - 1][y] = matrix[x][y];
            matrix[x][y] = 0;
            return true;
        } else return false;
    }

    void makeMove(int value) {
        int x = -1;
        int y = -1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] == value){
                    x = i;
                    y = j;
                }
            }
        }
        if ((y != 3) && (matrix[x][y + 1] == 0)) {
            matrix[x][y + 1] = matrix[x][y];
            matrix[x][y] = 0;
        } else if ((y != 0) && (matrix[x][y - 1] == 0)) {
            matrix[x][y - 1] = matrix[x][y];
            matrix[x][y] = 0;
        } else if ((x != 3) && (matrix[x + 1][y] == 0)) {
            matrix[x + 1][y] = matrix[x][y];
            matrix[x][y] = 0;
        } else if ((x != 0) && (matrix[x - 1][y] == 0)) {
            matrix[x - 1][y] = matrix[x][y];
            matrix[x][y] = 0;
        }

    }

    int[][] getMatrix() {
        return matrix;
    }

    boolean checkWin() {
        int count = 1;
        int error = 0;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                if (matrix[i][j] != count) error++;
                count++;
            }
        return error == 1;
    }

    private boolean canBeSolved(int[] invariants) {
        int sum = 0;
        for (int i = 0; i < 16; i++) {
            if (invariants[i] == 0) {
                sum += i / 4;
                continue;
            }

            for (int j = i + 1; j < 16; j++) {
                if (invariants[j] < invariants[i])
                    sum++;
            }
        }
        System.out.println(sum % 2 == 0);
        return sum % 2 == 0;
    }


    void randomizeMatrix() {
        Random generator = new Random();
        int[] invariants = new int[16];

        do {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    matrix[i][j] = 0;
                    invariants[i * 4 + j] = 0;
                }
            }

            for (int i = 1; i < 16; i++) {
                int k, l;
                do {
                    k = generator.nextInt(4);
                    l = generator.nextInt(4);
                }
                while (matrix[k][l] != 0);
                matrix[k][l] = i;
                invariants[k * 4 + l] = i;
            }
        }
        while (!canBeSolved(invariants));
    }



}
