package testing;

import logic.Field;

public class test_toBoard {



    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Field[][] initBoard = new Field[4][4];
        int[][] initColors = {
                {4, 5, 3, 3},
                {2, 2, 5, 4},
                {2, 2, 2, 2},
                {2, 2, 2, 2}
        };

        for (int i = 0; i < initBoard.length; i++) {
            for (int j = 0; j < initBoard[0].length; j++) {
                initBoard[i][j] = new Field(i,j,initColors[i][j]);
            }
        }

        Testing test = new Testing(initBoard);

        Field[][] anotherBoard = new Field[4][4];
        int[][] targetColors = {
                {4, 5, 3, 3},
                {2, 2, 5, 4},
                {2, 2, 2, 2},
                {2, 2, 2, 2}
        };

        for (int i = 0; i < anotherBoard.length; i++) {
            for (int j = 0; j < anotherBoard[0].length; j++) {
                anotherBoard[i][j] = new Field(i,j,targetColors[i][j]);
            }
        }

        boolean result = test.toBoard(anotherBoard, 0); // replace 16 with the actual moves you want to test
        System.out.println("The result is: " + result);
    }
}
