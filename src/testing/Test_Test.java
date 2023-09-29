package testing;

import logic.Field;

public class Test_Test {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Field[][] initBoard = new Field [3][3];
        int [][] farben = {
                {1,2,3,4,5,2,},
                {6,1,5,1,3,5,},
                {4,3,6,2,5,1,},
                {5,1,3,6,2,5,},
                {3,4,5,2,6,3,},
                {1,2,6,1,3,2,} };

        int [][] farben5 = {
                {2,2,2,6,},
                {2,2,2,2,},
                {2,2,2,2,} };


        int [][] farbenEnd = {
                {1,1,1,1,1,1,},
                {1,1,2,1,1,1,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,},
                {2,2,2,2,2,2,}};


        int [][] fabenTest1 = {
                {1,1,2,},
                {1,1,1,},
                {1,1,1,}};

        int[][] farben3 = {
                {2,6,3,5,1,1,},
                {1,3,2,4,2,1,},
                {3,2,1,2,4,1,},
                {2,3,6,3,5,4,},
                {3,2,4,5,2,3,},
                {5,1,3,2,1,2,}};

        for (int i = 0; i < initBoard.length; i++) {
            for (int j = 0; j < initBoard[0].length; j++) {

                initBoard[i][j] = new Field(i,j,farben5[i][j]);
            }
        }


        Testing test = new Testing(initBoard);

        boolean start = test.isStartklar();
        boolean end = test.isEndConfig();

       int Stagnation = test.testStrategy01();
       System.out.println("pcTurnStagnation choose : " + Stagnation);

       int Greedy = test.testStrategy02();
        System.out.println("pcTurnGreedy choose : " + Greedy);

        int Blocking = test.testStrategy03();
       System.out.println("pcTurnBlocking choose : " + Blocking);

        System.out.println("board is end:" + end);
        System.out.println("board is startklar:" + start);

        int minMoves = test.minMoves(1, 1);
       System.out.println("Minimum moves required to control the piece : " + minMoves);


        int minMovesFull = test.minMovesFull();
        System.out.println("minMovesFull: " + minMovesFull);
    }

}
