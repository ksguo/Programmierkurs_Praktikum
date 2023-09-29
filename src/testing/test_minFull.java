package testing;

import logic.Field;

public class test_minFull {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Field[][] initBoard = new Field[4][4];
        int[][] farben = {
                {4, 5, 3, 3,},
                {2, 2, 5, 4,},
                {2, 2, 2, 2,},
                {2, 2, 2, 2,}};

        int [][] farbenFull = {
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,}};

        int[][] farben3 = {
                {2,6,3,5,1,1,},
                {1,3,2,4,2,1,},
                {3,2,1,2,4,1,},
                {2,3,6,3,5,4,},
                {3,2,4,5,2,3,},
                {5,1,3,2,1,2,}};

        for (int i = 0; i < initBoard.length; i++) {
            for (int j = 0; j < initBoard[0].length; j++) {

                initBoard[i][j] = new Field(i,j,farben3[i][j]);
            }
        }


        Testing test = new Testing(initBoard);

        int minMovesFull = test.minMovesFull();
        System.out.println("minMovesFull: " + minMovesFull);
    }



}
