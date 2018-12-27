import java.util.*;

class Board {
    private int[][] matrix = new int[4][4];
    private int zeroX;
    private int zeroY;
    private int h;

    private int lastX;
    private int lastY;

    private int dimension() { return matrix.length; }
    private int getH() { return h; }

    Map getNeighbors(){
        int x = zeroX;
        int y = zeroY;
        Map data = new HashMap<Integer, ArrayList<Integer>>();


        if (y == 3) {
            if (x == 0){
                data.put(value(x + 1, y), Arrays.asList(x + 1, y));
                data.put(value(x, y - 1), Arrays.asList(x, y - 1));
            }

            if (x == 3){
                data.put(value(x - 1, y), Arrays.asList(x - 1, y));
                data.put(value(x, y - 1), Arrays.asList(x, y - 1));
            }
            else
                data.put(value(x + 1, y), Arrays.asList(x + 1, y));
            data.put(value(x - 1, y), Arrays.asList(x - 1, y));
            data.put(value(x, y - 1), Arrays.asList(x, y - 1));
        }
        if (y == 0) {
            if (x == 0){
                data.put(value(x + 1, y), Arrays.asList(x + 1, y));
                data.put(value(x, y + 1), Arrays.asList(x, y + 1));
            }

            if (x == 3){
                data.put(value(x - 1, y), Arrays.asList(x - 1, y));
                data.put(value(x, y + 1), Arrays.asList(x, y + 1));
            }
            else
                data.put(value(x + 1, y), Arrays.asList(x + 1, y));
            data.put(value(x - 1, y), Arrays.asList(x - 1, y));
            data.put(value(x, y + 1), Arrays.asList(x, y + 1));
        }

        if (x == 3){
            data.put(value(x - 1, y), Arrays.asList(x - 1, y));
            data.put(value(x ,y - 1), Arrays.asList(x, y - 1));
            data.put(value(x, y + 1), Arrays.asList(x, y + 1));
        }

        if (x == 0){
            data.put(value(x + 1, y), Arrays.asList(x + 1, y));
            data.put(value(x ,y - 1), Arrays.asList(x, y - 1));
            data.put(value(x, y + 1), Arrays.asList(x, y + 1));
        }

        else
            data.put(value(x + 1, y), Arrays.asList(x + 1, y));
        data.put(value(x - 1, y), Arrays.asList(x - 1, y));
        data.put(value(x, y + 1), Arrays.asList(x, y + 1));
        data.put(value(x, y - 1), Arrays.asList(x, y - 1));

        return data;
    }

    // короче, нужно просто в зависимости от того, где стоит 0, добавить в карту всех соседей
    // а ещё там есть метод, который возвращает значение ячейки по её координатам (value())
    // так что вперёд собсно


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
            recountParameters();
        }
        while (!canBeSolved(invariants));
    }

    private void recountParameters() {
        h = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != (i * dimension() + j + 1) && matrix[i][j] != 0) {
                    h += 1;
                }
                if (matrix[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                }
            }
        }
    }

    private int value(int x, int y){
        return matrix[x][y];
    }



}
