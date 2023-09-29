package testing;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.LinkedList;
import java.util.*;

/*
 * Siehe Hinweise auf dem Aufgabenblatt.
 */

import logic.Field;


public class Testing {

    private Field[][] board;



    public Testing(Field[][] initBoard) {
        this.board = initBoard;



    }



    public boolean isStartklar() {
        // Check condition 3
        if (board[0][board[0].length - 1].getColor() == board[board.length - 1][0].getColor()) {
            return false;
        }

        Set<Integer> colors = new HashSet<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int color = board[i][j].getColor();
                colors.add(color);

                // Check the color of neighbor cells
                if ((i > 0 && board[i - 1][j].getColor() == color)
                        || (j > 0 && board[i][j - 1].getColor() == color)
                        || (i < board.length - 1 && board[i + 1][j].getColor() == color)
                        || (j < board[i].length - 1 && board[i][j + 1].getColor() == color)) {
                    return false;  // condition 1 not met
                }
            }
        }

        // Check condition 2
        return colors.size() == 6;
    }



    public boolean isEndConfig() {
        int playerColor = board[board.length-1][0].getColor();
        int pcColor = board[0][board[0].length-1].getColor();

        int rows = board.length;
        int cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Field currentField = board[i][j];
                int fieldColor = currentField.getColor();
                if (!(fieldColor == playerColor) && !(fieldColor == pcColor)) {
                    return false;
                }
            }
        }

        return true;
    }


    public int testStrategy01() {
        updateConnectedFields();

        updateConnectedFieldsForPC();
        int minIncrease = Integer.MAX_VALUE;
        int bestColor = -1;
        int playerColor = board[board.length-1][0].getColor();
        int pcColor = board[0][board[0].length-1].getColor();
        for (int i = 1; i < 7; i++) {
            if (i == playerColor || i == pcColor) {
                continue;
            }
           // int color = i;


            int increase = calculateIncrease(i, false);
            if (increase < minIncrease) {
                minIncrease = increase;
                bestColor = i;
            } else if (increase ==  minIncrease && i < bestColor ) {
                // In case of a tie, choose the smallest color based on RGB value
                bestColor = i;
            }
        }

        if (bestColor == -1) {
            throw new IllegalStateException("error");
        }
    //    updateConnectedFieldsForPC();
        return bestColor;

    }

    public int testStrategy02() {

        updateConnectedFields();
        updateConnectedFieldsForPC();

        int maxIncrease = Integer.MIN_VALUE;
        int bestColor = -1;
        int playerColor = board[0][board[0].length-1].getColor();
        int pcColor = board[board.length-1][0].getColor();



        for (int color = 1; color < 7; color++) {
            // 跳过玩家当前的颜色和电脑自身的颜色
            if (color == playerColor || color == pcColor) {
                continue;
            }

            int increase = calculateIncrease(color, false);
            if (increase > maxIncrease || (increase == maxIncrease && color < bestColor)) {
                maxIncrease = increase;
                bestColor = color;
            }

        }

        return bestColor;
    }

    public int testStrategy03() {


        updateConnectedFields();
        updateConnectedFieldsForPC();

        int maxAreaIncrease = Integer.MIN_VALUE;
        int chosenColor = 0;
        int lc = board[0][board[0].length-1].getColor();
        int oc = board[board.length-1][0].getColor();

        for (int i = 1; i < 7; i++) {
            if (i == lc || i == oc) {
                continue;
            }
            int color = i;

            int areaIncrease = calculateIncrease(color, true);
            if (areaIncrease > maxAreaIncrease) {
                maxAreaIncrease = areaIncrease;
                chosenColor = color;
            } else if (areaIncrease == maxAreaIncrease && i < chosenColor) {
                // In case of a tie, choose the smallest color based on RGB value
                chosenColor = i;
            }
        }
      //  updateConnectedFieldsForPC();
        return chosenColor;
    }



    public boolean toBoard(Field[][] anotherBoard, int moves) {
        int count = 0;
        Field[][] copyBoard = copyBoard(board);

        if (anotherBoard.length != board.length || anotherBoard[0].length != board[0].length) {
            return false;
        }

        List<Field> currentComponent = findComponent(board[0][board[0].length-1]);
        List<Field> targetComponent = findComponent(anotherBoard[0][anotherBoard[0].length-1]);

        List<Field> versFelderPlayer = new ArrayList<>();
        for (Field targetField : targetComponent) {
            boolean found = false;
            for (Field currentField : currentComponent) {
                if (targetField.getRow() == currentField.getRow() && targetField.getCol() == currentField.getCol()) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                versFelderPlayer.add(board[targetField.getRow()][targetField.getCol()]);
            }
        }

        int targetColor = board[board.length - 1][0].getColor();

        while (versFelderPlayer.size() != 0 && count <= moves) {
            currentComponent = findComponent(board[board.length - 1][0]);
            List<Field> potNextField = findNextField(currentComponent, versFelderPlayer);

            if (versFelderPlayer.size() == 0) {
                targetColor = 0;
                count++;
            } else {
                Field nextField = searchBestField(potNextField, board, versFelderPlayer, targetColor);
                if (nextField != null) {
                    targetColor = nextField.getColor();
                    List<Field> newFields = updateComponent(currentComponent, nextField, targetColor);
                    versFelderPlayer.removeAll(newFields);
                    count++;
                } else {
                    count++;
                }
            }
        }

        if (count <= moves && isSameBoard(anotherBoard)) {
            board = copyBoard(copyBoard);
            return true;
        }

        board = copyBoard(copyBoard);
        return false;
    }
    private boolean isSameBoard(Field[][] anotherBoard) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j].getColor() != anotherBoard[i][j].getColor()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Field[][] copyBoard(Field[][] originalBoard) {
        Field[][] newBoard = new Field[originalBoard.length][];
        for (int i = 0; i < originalBoard.length; i++) {
            newBoard[i] = Arrays.copyOf(originalBoard[i], originalBoard[i].length);
        }
        return newBoard;
    }

    private List<Field> updateComponent(List<Field> currentComponent, Field nextField, int targetColor) {
        // Assuming `nextField` is the new position of a part of the currentComponent
        // Change the color of nextField to targetColor and update it in currentComponent
        nextField.setColor(targetColor);
        currentComponent.remove(nextField);
        currentComponent.add(nextField);
        return currentComponent;
    }

    private List<Field> findNextField(List<Field> currentComponent, List<Field> versFelderPlayer) {
        List<Field> NextField = new ArrayList<>();
        // Simple version: Check each field in the board. If it's not occupied by versFelderPlayer, consider it as a potential next field
        for (Field[] row : board) {
            for (Field field : row) {
                if (!versFelderPlayer.contains(field)) {
                    NextField.add(field);
                }
            }
        }
        return NextField;
    }

    private List<Field> findComponent(Field field) {
        List<Field> component = new ArrayList<>();
        // Simple version: Check each field in the board. If it's in the same color as the input field, consider it as a part of the component
        for (Field[] row : board) {
            for (Field f : row) {
                if (f.getColor() == field.getColor()) {
                    component.add(f);
                }
            }
        }
        return component;
    }

    private Field searchBestField(List<Field> potNextField, Field[][] board, List<Field> versFelderPlayer, int targetColor) {
        // Simple version: Check each potential next field. If it's not occupied by versFelderPlayer and its color is the target color, choose it as the best field
        for (Field field : potNextField) {
            if (!versFelderPlayer.contains(field) && field.getColor() == targetColor) {
                return field;
            }
        }
        // If there is no such field, return null
        return null;
    }

    public int minMoves(int row, int col) {
        // Check if the target Field object has already been taken
        if (board[row][col].isConnectedByPC()) {
            return 0;
        }

        int minMoves = Integer.MAX_VALUE;

        // Loop through all possible start colors
        for (int startColor = 1; startColor <= 6; startColor++) {

            // Copy the original game board
            Field[][] boardCopy = copyBoard();

            // Start BFS from the start position
            int moves = 0;
            int color = startColor;
            boolean done = false;
            while (!done) {

                updateForMinMoves();
                // Change the color of all connected fields
                for (Field[] rowFields : board) {
                    for (Field field : rowFields) {
                        if (field.isConnectedByPC()) {
                            field.setColor(color);
                        }
                    }
                }
printBoard();
                // Check if the target cell has been taken before incrementing moves
                if (board[row][col].isConnectedByPC()) {
                    minMoves = Math.min(minMoves, moves);
                    done = true;
                    break;
                }

                // Increase the steps
                moves++;

                // If the whole board has been colored, we stop
                if (isAllSameColor()) {
                    done = true;
                }

                color = getNextColor(color);
            }

            // Reset the game board for the next iteration
            board = boardCopy;
        }
        return minMoves;
    }
    public void updateForMinMoves() {
        // 先将所有格子的isConnectedByPC和isConnected字段设置为false
        for (Field[] row : board) {
            for (Field field : row) {
                field.setConnectedByPC(false);
                field.setConnected(false); // 添加此行代码
            }
        }

        dfsForMinMoves(board.length - 1, 0, board[board.length - 1][0].getColor(), false);
    }

    public void dfsForMinMoves(int row, int col, int color, boolean isPlayer) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        Field field = board[row][col];
        // If the cell is already connected to player or computer, or has a different color, return
        if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC()) || field.getColor() != color) {
            return;
        }

        if (isPlayer) {
            field.setConnected(true);
        } else {
            field.setConnectedByPC(true);
        }

        // Check if the target cell's neighbor is connected
        if (row == board.length - 1 && col == 0) {
            for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int neighborRow = row + dir[0];
                int neighborCol = col + dir[1];
                if (neighborRow >= 0 && neighborRow < board.length && neighborCol >= 0 && neighborCol < board[0].length) {
                    Field neighbor = board[neighborRow][neighborCol];
                    if (neighbor.isConnectedByPC()) {
                        return;  // Neighbor connected, exit DFS
                    }
                }
            }
        }

        // Perform DFS on the four neighboring cells
        for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
            dfsForMinFull(row + dir[0], col + dir[1], color, isPlayer);
        }
    }


    public int minMovesFull() {
        // Überprüfen Sie, ob das Spielbrett bereits einfarbig ist
        for (int startColor = 1; startColor <= 6; startColor++) {
            if (isAllSameColor()) {
                return 0;
            }
        }

        int minMoves = Integer.MAX_VALUE;
        // Schleife durch alle möglichen Startfarben
        for (int startColor = 1; startColor <= 6; startColor++) {

            // Kopiere das ursprüngliche Spielbrett
            Field[][] boardCopy = copyBoard();

            // Beginne BFS von der Startposition
            int moves = 0;
            int color = startColor;
            boolean done = false;
            while (!done) {
                // Erhöhen Sie zuerst die Schritte
                moves++;

                updateForMinFull();
                // Ändere die Farbe aller verbundenen Felder
                for (Field[] row : board) {
                    for (Field field : row) {
                        if (field.isConnectedByPC()) {
                            field.setColor(color);
                        }
                    }
                }



                if (isAllSameColor()) {
                    minMoves = Math.min(minMoves, moves);
                    break;
                }

                color = getNextColor(color);
            }

            // Setze das Spielbrett für die nächste Iteration zurück
            board = boardCopy;
        }
        return minMoves;
    }


    private int getNextColor(int color) {
        return (color % 6) + 1;
    }

    private Field[][] copyBoard() {
        Field[][] boardCopy = new Field[board.length][];
        for (int i = 0; i < board.length; i++) {
            boardCopy[i] = new Field[board[i].length];
            for (int j = 0; j < board[i].length; j++) {
                Field field = board[i][j];
                boardCopy[i][j] = new Field(field.getRow(), field.getCol(), field.getColor());
            }
        }
        return boardCopy;
    }

    public void printBoard() {
        for (Field[] row : board) {
            for (Field field : row) {
                System.out.print(field.getColor() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isAllSameColor() {
        int firstColor = board[0][0].getColor();
        for (Field[] row : board) {
            for (Field field : row) {
                if (field.getColor() != firstColor) {
                    return false;
                }
            }
        }
        return true;
    }





    /*
     * Getter und Setter
     */
    public Field[][] getBoard() {
        return board;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }




    //method




    public void updateForMinFull() {
        // 先将所有格子的isConnectedByPC字段设置为false
        for (Field[] row : board) {
            for (Field field : row) {
                field.setConnectedByPC(false);
            }
        }


        dfsForMinFull(board.length - 1, 0, board[board.length - 1][0].getColor(), false);
    }

    public void dfsForMinFull(int row, int col, int color, boolean isPlayer) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        Field field = board[row][col];
        // 如果该格子已经连接到玩家或者电脑，或者颜色不同，那么就直接返回
        if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC()) || field.getColor() != color) {
            return;
        }

        if (isPlayer) {
            field.setConnected(true);
        } else {
            field.setConnectedByPC(true);
        }

        // 对当前格子的上下左右四个邻居进行深度优先搜索
        for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
            dfsForMinFull(row + dir[0], col + dir[1], color, isPlayer);
        }
    }

    public int calculateIncrease(int color, boolean isPlayer) {
        int increase = 0;
        for (Field[] row : board) {
            for (Field field : row) {
                if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC())) {
                    for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                        int newRow = field.getRow() + dir[0];
                        int newCol = field.getCol() + dir[1];
                        if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                            Field newField = board[newRow][newCol];
                            if (newField.getColor() == color && !(newField.isConnected() || newField.isConnectedByPC())) {
                                increase++;
                            }
                        }
                    }
                }
            }
        }
        return increase;
    }

    public void updateConnectedFields() {
        // 先将所有格子的isConnected字段设置为false
        for (Field[] row : board) {
            for (Field field : row) {
                field.setConnected(false);
            }
        }

        // 从左下角开始，使用深度优先搜索找到所有相连的格子
        dfs(board.length - 1, 0, board[board.length - 1][0].getColor());
    }

    public void dfs(int row, int col, int color) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        Field field = board[row][col];
        if (field.isConnected() || field.getColor() != color) {
            return;
        }

        field.setConnected(true);
        dfs(row - 1, col, color);
        dfs(row + 1, col, color);
        dfs(row, col - 1, color);
        dfs(row, col + 1, color);
    }


    public void updateConnectedFieldsForPC() {
        // 先将所有格子的isConnectedByPC字段设置为false
        for (Field[] row : board) {
            for (Field field : row) {
                field.setConnectedByPC(false);
            }
        }

        // 从右上角开始，使用深度优先搜索找到所有相连的格子
        dfs2(0, board[0].length - 1, board[0][board[0].length - 1].getColor(), false);
    }

    public void dfs2(int row, int col, int color, boolean isPlayer) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }

        Field field = board[row][col];
        if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC()) || field.getColor() != color) {
            return;
        }

        if (isPlayer) {
            field.setConnected(true);
        } else {
            field.setConnectedByPC(true);
        }
        dfs2(row - 1, col, color, isPlayer);
        dfs2(row + 1, col, color, isPlayer);
        dfs2(row, col - 1, color, isPlayer);
        dfs2(row, col + 1, color, isPlayer);
    }



}

