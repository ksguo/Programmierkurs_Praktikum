package testing;

import logic.Field;

public class Test2 {


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Field[][] initBoard = new Field[4][4];
        int[][] farben = {
                {4, 5, 6, 3,},
                {1, 2, 3, 4,},
                {3, 5, 6, 1,},
                {1, 3, 4, 6,}};

        int[][] farben_minMoves = {
                {4, 5, 3, 3,},
                {1, 2, 5, 4,},
                {3, 5, 2, 1,},
                {1, 3, 5, 6,}};



        int[][] farben2 = {
                {3, 4, 3, 3,},
                {2, 1, 3, 2,}};

        int[][] farben3 = {
        {2,6,3,5,1,1,},
        {1,3,2,4,2,1,},
        {3,2,1,2,4,1,},
        {2,3,6,3,5,4,},
        {3,2,4,5,2,3,},
        {5,1,3,2,1,2,}};


        int[][] farben4 = {
                {2,6,3,5,1,1,},
                {1,3,2,4,2,1,},
                {3,2,1,2,4,1,},
                {2,3,6,3,5,4,}};

        int[][] farben5 = {
                {2,6,3,5,6,2,},
                {1,3,1,4,3,4,},
                {3,2,1,2,4,1,},
                {4,3,6,3,5,4,}};


        for (int i = 0; i < initBoard.length; i++) {
            for (int j = 0; j < initBoard[0].length; j++) {

                initBoard[i][j] = new Field(i,j,farben[i][j]);
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
        System.out.println("board is startklar:" + start);
        System.out.println("board is end:" + end);


        int minMoves = test.minMoves(0, 3);
        System.out.println("Minimum moves required to control the piece : " + minMoves);
           System.out.println("test fetch function: ");

    }

    }
