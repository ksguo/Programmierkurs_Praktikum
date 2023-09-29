package testing;

import logic.Field;

public class test_minMoves {
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Field[][] initBoard = new Field[3][3];
        int[][]  farben = {
                {4, 5, 3, 3},
                {1, 2, 5, 4},
                {3, 5, 2, 1},
                {1, 3, 5, 6}
        };

        int [][] farbenFull = {
                {1,1,1,1,1,1,},
                {1,1,1,1,1,1,},
                {1,1,1,1,1,1,},
                {1,1,1,1,1,1,},
                {2,2,2,2,1,1,},
                {2,2,2,2,2,2,}};

        int[][] farben3 = {
                {2,6,3,5,1,1,},
                {1,3,2,4,2,1,},
                {3,2,1,2,4,1,},
                {2,3,6,3,5,4,},
                {3,2,4,5,2,3,},
                {5,1,3,2,1,2,}};

        int [][] fabenTest1 = {
                {1,1,3,},
                {1,1,1,},
                {1,1,1,}};

        Field[][] fields = new Field[initBoard.length][initBoard[0].length];

        for (int row = 0; row < initBoard.length; row++) {
            for (int col = 0; col < initBoard[row].length; col++) {
                fields[row][col] = new Field(row, col, fabenTest1[row][col]);
            }
        }

        Testing testing = new Testing(fields);

        int row = 0;
        int col = 2;
        int minMoves = testing.minMoves(row, col);
        System.out.println("Min Moves to (" + row + ", " + col + "): " + minMoves);
    }
}
