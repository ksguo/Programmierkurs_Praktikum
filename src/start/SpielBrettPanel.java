package start;


import javax.swing.*;




import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.*;


import logic.Field;



public class SpielBrettPanel extends JPanel {

    private Fenster fenster;
    private Field[][] spielBrett;  // 二维数组来存储游戏板的所有格子
    private List<Color> colorList = Arrays.asList( Color.decode("#E6E6FA"),
            Color.decode("#BEBEBE"),
            Color.decode("#6A5ACD"),
            Color.decode("#00BFFF"),
            Color.decode("#00CED1"),
            Color.decode("#FFD700"),
            Color.decode("#FF6A6A"),
            Color.decode("#9400D3"),
            Color.decode("#FFFACD"));
    // 添加一个新的字段来存储当前选中的颜色
    private int selectedColor = -1;




    private String selectedStrategy = "Stagnation" ;  // 默认策略为 "Stagnation"

    private List<Integer> previousSizes = new ArrayList<>();



    public SpielBrettPanel(Fenster fenster) {



        this.fenster = fenster;


        this.setBackground(Color.decode("#E1FFFF"));
       // this.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.6), (int)(fenster.getHeight()*0.75)));
      //  this.setLayout(new GridLayout(1,1));
        this.setVisible(true);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);


                if (!fenster.getMenütafel().isStarted() || !fenster.getMenütafel().isPlayable()) {  // 在这里检查游戏是否已经开始并且是否可以玩
                    JOptionPane.showMessageDialog(null, "bitte zuerst klicken Playbutton", "Hinweisen", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }


                // 计算每个格子的大小
                int maxBoardWidth = (int)(getWidth() * 0.95);
                int maxBoardHeight = (int)(getHeight() * 0.95);
                int cellWidth = Math.min(maxBoardWidth / spielBrett[0].length, maxBoardHeight / spielBrett.length);
                int cellHeight = cellWidth;  // 保持格子是正方形

                // 计算游戏板应该开始绘制的位置，使其在面板中居中
                int offsetX = (getWidth() - cellWidth * spielBrett[0].length) / 2;
                int offsetY = (getHeight() - cellHeight * spielBrett.length) / 2;


                // 计算点击的格子的坐标
                int x = (e.getX() - offsetX) / cellWidth;
                int y = (e.getY() - offsetY) / cellHeight;
                // 检查坐标是否在合理的范围内
                if (x < 0 || x >= spielBrett[0].length || y < 0 || y >= spielBrett.length ||
                        e.getX() < offsetX || e.getX() >= offsetX + spielBrett[0].length * cellWidth ||
                        e.getY() < offsetY || e.getY() >= offsetY + spielBrett.length * cellHeight) {
                    return;
                }



                // 获取被点击的格子的颜色
                int clickedColor = spielBrett[y][x].getColor();


                // 获取电脑当前的颜色
                int pcColor = spielBrett[0][spielBrett[0].length - 1].getColor();
// 获取玩家当前的颜色
                int playerColor = spielBrett[spielBrett.length - 1][0].getColor();  // 假设玩家颜色与左下角的颜色一致

// 如果玩家点击的颜色是电脑当前的颜色或自己当前的颜色，那么就返回并不做任何操作
                if (clickedColor == pcColor || clickedColor == playerColor) {
                    return;
                }


                // 遍历所有的格子，如果一个格子与左下角相连，那么就将它的颜色设置为被点击的格子的颜色
                for (Field[] row : spielBrett) {
                    for (Field field : row) {
                        if (field.isConnected()) {
                            field.setColor(clickedColor);
                        }
                    }
                }


                updateScores2();

                // 更新哪些格子与左下角相连
                updateConnectedFields();


                // 强制重绘面板，显示新的游戏板
                repaint();

                fenster.getMenütafel().getS1TurnLabel().setText("PC ist dran"); // Add this line

                executePCTurn();


            }


        });


        this.setFocusable(true);





    }

    public void executePCTurn() {

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        //在这里执行你原来的代码

                        selectedStrategy = fenster.getMenütafel().getComboBox().getSelectedItem().toString();
                        pcTurn(selectedStrategy);
                        updateConnectedFields();
                        repaint();
                        updateScores();

                        fenster.getMenütafel().getS1TurnLabel().setText("S1 ist dran"); // Add this line
                        fenster.getAnzeigetafel().getFarbenPanel().repaint();  // 在这里添加


                        updateScores2();
                        int s1Size = calculateComponentSize(true);
                        int s2Size = calculateComponentSize(false);

                        // 在游戏结束的情况下重置游戏
                        if (previousSizes.size() == 4 && previousSizes.get(0).equals(previousSizes.get(1)) && previousSizes.get(1).equals(previousSizes.get(2)) && previousSizes.get(2).equals(previousSizes.get(3))) {
                            fenster.getMenütafel().pauseTimer();
                            JOptionPane.showMessageDialog(null, "The game is a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                            fenster.getMenütafel().resetGame();
                        } else if (isFinalConfiguration()) {
                            if (s1Size > s2Size) {
                                fenster.getMenütafel().pauseTimer();
                                JOptionPane.showMessageDialog(null, "S1 wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            } else if (s1Size < s2Size) {
                                fenster.getMenütafel().pauseTimer();
                                JOptionPane.showMessageDialog(null, "S2 wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                fenster.getMenütafel().pauseTimer();
                                JOptionPane.showMessageDialog(null, "The game is a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            }

                            fenster.getMenütafel().resetGame();
                        }

                    }
                });
            }
        });

        timer.setRepeats(false); //确保定时器只执行一次
        timer.start(); //启动定时器
    }




    public void createSpielBrett(int zeilen, int spalten, int farben) {
        Random rand = new Random();
        spielBrett = new Field[zeilen][spalten];

        // Create a list of all colors
        List<Integer> allColors = new ArrayList<>();
        for (int i = 0; i < farben; i++) {
            allColors.add(i);
        }
        // Shuffle the color list to ensure randomness
        Collections.shuffle(allColors);

        // Initially assign one of each color to the cells
        int colorIndex = 0;
        for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                if (colorIndex < farben) {
                    spielBrett[i][j] = new Field(i, j, allColors.get(colorIndex));
                    colorIndex++;
                }
            }
        }

        // Fill the remaining cells with random colors
        for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                if (spielBrett[i][j] == null) {
                    int randomColor;
                    do {
                        randomColor = rand.nextInt(farben);
                    } while ((i > 0 && spielBrett[i - 1][j].getColor() == randomColor)
                            || (j > 0 && spielBrett[i][j - 1].getColor() == randomColor));
                    spielBrett[i][j] = new Field(i, j, randomColor);
                }
            }
        }

        // Ensure left bottom cell and top right cell are different colors
        int topRightColor = spielBrett[0][spalten - 1].getColor();
        int bottomLeftColor = spielBrett[zeilen - 1][0].getColor();
        if (topRightColor == bottomLeftColor) {
            int newColor;
            do {
                newColor = rand.nextInt(farben);
            } while (newColor == bottomLeftColor || newColor == spielBrett[zeilen - 2][0].getColor() || (spalten > 1 && newColor == spielBrett[zeilen - 1][1].getColor()));
            spielBrett[zeilen - 1][0].setColor(newColor);
        }






        // 强制重绘面板，显示新的游戏板
        this.repaint();

        selectedColor = -1;


    updateConnectedFields();
  updateConnectedFieldsForPC();





    }


    // 游戏板清除方法
    public void clearSpielBrett() {
        spielBrett = null;
        // 请求重新绘制
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // 先绘制背景

        if (spielBrett != null) {
            // 计算每个格子的大小，保持游戏板尺寸不超过面板尺寸的80%
            int maxBoardWidth = (int)(getWidth() * 0.95);
            int maxBoardHeight = (int)(getHeight() * 0.95);
            int cellWidth = Math.min(maxBoardWidth / spielBrett[0].length, maxBoardHeight / spielBrett.length);
            int cellHeight = cellWidth;  // 保持格子是正方形

            // 计算游戏板应该开始绘制的位置，使其在面板中居中
            int offsetX = (getWidth() - cellWidth * spielBrett[0].length) / 2;
            int offsetY = (getHeight() - cellHeight * spielBrett.length) / 2;

            for (int i = 0; i < spielBrett.length; i++) {
                for (int j = 0; j < spielBrett[i].length; j++) {
                    // 选择颜色
                    g.setColor(colorList.get(spielBrett[i][j].getColor()));
                    // 绘制正方形
                    g.fillRect(j * cellWidth + offsetX, i * cellHeight + offsetY, cellWidth, cellHeight);

                    // 添加黑色边框
                    g.setColor(Color.BLACK);
                    g.drawRect(j * cellWidth + offsetX, i * cellHeight + offsetY, cellWidth, cellHeight);
                }
            }
        }

    }
    // 获取当前游戏板的颜色配置
    public int[][] getSpielBrettColors() {
        if (spielBrett == null) {
            return null;
        }
        int[][] colors = new int[spielBrett.length][spielBrett[0].length];
        for (int i = 0; i < spielBrett.length; i++) {
            for (int j = 0; j < spielBrett[i].length; j++) {
                colors[i][j] = spielBrett[i][j].getColor();
            }
        }
        return colors;
    }




    /*
     * get set部分
     */


    public List<Color> getColorList() {
        return colorList;
    }


    public void setColorList(List<Color> colorList) {
        this.colorList = colorList;
    }


    public int getSelectedColor() {
        return selectedColor;
    }




    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }



    public Field[][] getSpielBrett() {
        return spielBrett;
    }


    public void setSpielBrett(Field[][] spielBrett) {
        this.spielBrett = spielBrett;
    }

    public void setSelectedStrategy(String selectedStrategy) {
        this.selectedStrategy = selectedStrategy;
    }

    public String getSelectedStrategy() {
        return selectedStrategy;
    }

    public List<Integer> getPreviousSizes() {
        return previousSizes;
    }

    public void setPreviousSizes(List<Integer> previousSizes) {
        this.previousSizes = previousSizes;
    }
    /*
     *
     */

    public void updateConnectedFields() {
        // 先将所有格子的isConnected字段设置为false
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                field.setConnected(false);
            }
        }

        // 从左下角开始，使用深度优先搜索找到所有相连的格子
        dfs(spielBrett.length - 1, 0, spielBrett[spielBrett.length - 1][0].getColor());
    }

    public void dfs(int row, int col, int color) {
        if (row < 0 || row >= spielBrett.length || col < 0 || col >= spielBrett[0].length) {
            return;
        }

        Field field = spielBrett[row][col];
        if (field.isConnected() || field.getColor() != color) {
            return;
        }

        field.setConnected(true);
        dfs(row - 1, col, color);
        dfs(row + 1, col, color);
        dfs(row, col - 1, color);
        dfs(row, col + 1, color);
    }



    public void pcTurn(String strategy) {


        switch (strategy) {
            case "Greedy":
                pcTurnGreedy();
                break;
            case "Stagnation":
                pcTurnStagnation();
                break;
            case "Blocking":
                pcTurnBlocking();
                break;
            default:
                throw new IllegalArgumentException("Invalid strategy: " + strategy);
        }

    }



    public void pcTurnGreedy() {
        updateConnectedFields();


        int maxIncrease = Integer.MIN_VALUE;
        int bestColor = -1;
        int playerColor = spielBrett[0][spielBrett[0].length-1].getColor();
        int pcColor = spielBrett[spielBrett.length-1][0].getColor();



        for (int color = 0; color < colorList.size(); color++) {
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
        System.out.println("Best color selected: " + bestColor + ", Increase: " + maxIncrease);
        // 更新电脑的连通组件的颜色
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (field.isConnectedByPC()) {
                    field.setColor(bestColor);
                }
            }
        }

        // 更新电脑的连通组件
        updateConnectedFieldsForPC();
    }

    public void pcTurnStagnation() {

        updateConnectedFields();
        // 获取玩家和电脑当前的颜色
        int playerColor = spielBrett[spielBrett.length - 1][0].getColor();
        int pcColor = spielBrett[0][spielBrett[0].length - 1].getColor();

        // 实现电脑的停滞策略
        int minIncrease = Integer.MAX_VALUE;
        int bestColor = -1;
        for (int color = 0; color < colorList.size(); color++) {
            // 跳过玩家当前的颜色和电脑自身的颜色
            if (color == playerColor || color == pcColor) {
                continue;
            }

            int increase = calculateIncrease(color, false);
            if (increase < minIncrease || (increase == minIncrease && color < bestColor)) {
                minIncrease = increase;
                bestColor = color;
            }
        }

        // 更新电脑的连通组件的颜色
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (field.isConnectedByPC()) {
                    field.setColor(bestColor);
                }
            }
        }

        // 更新电脑的连通组件
        updateConnectedFieldsForPC();
    }


    public void pcTurnBlocking() {

        updateConnectedFields();

        // Get the current color of the player
        int playerColor = spielBrett[spielBrett.length - 1][0].getColor();
        int pcColor = spielBrett[0][spielBrett[0].length - 1].getColor();

        // Implement the PC's blocking strategy
        int maxIncrease = Integer.MIN_VALUE;
        int bestColor = -1;
        for (int color = 0; color < colorList.size(); color++) {
            // Skip the player's current color and PC's current color
            if (color == playerColor || color == pcColor) {
                continue;
            }


            int areaIncrease = calculateIncrease(color, true);
            if (areaIncrease > maxIncrease) {
                maxIncrease = areaIncrease;
                bestColor = color;
            } else if (areaIncrease == maxIncrease && color < bestColor) {
                // In case of a tie, choose the smallest color based on RGB value
                bestColor = color;
            }


        }

        // Update the color of the PC's connected component
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (field.isConnectedByPC()) {
                    field.setColor(bestColor);
                }
            }
        }

        // Update the PC's connected component
        updateConnectedFieldsForPC();

    }



    public void updateConnectedFieldsForPC() {
        // 先将所有格子的isConnectedByPC字段设置为false
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                field.setConnectedByPC(false);
            }
        }

        // 从右上角开始，使用深度优先搜索找到所有相连的格子
        dfs(0, spielBrett[0].length - 1, spielBrett[0][spielBrett[0].length - 1].getColor(), false);
    }

    public void dfs(int row, int col, int color, boolean isPlayer) {
        if (row < 0 || row >= spielBrett.length || col < 0 || col >= spielBrett[0].length) {
            return;
        }

        Field field = spielBrett[row][col];
        if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC()) || field.getColor() != color) {
            return;
        }

        if (isPlayer) {
            field.setConnected(true);
        } else {
            field.setConnectedByPC(true);
        }
        dfs(row - 1, col, color, isPlayer);
        dfs(row + 1, col, color, isPlayer);
        dfs(row, col - 1, color, isPlayer);
        dfs(row, col + 1, color, isPlayer);
    }


    public int calculateIncrease(int color, boolean isPlayer) {
        // 计算如果选择这种颜色，连通组件的大小会增加多少
        int increase = 0;
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC())) {
                    for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                        int newRow = field.getRow() + dir[0];
                        int newCol = field.getCol() + dir[1];
                        if (newRow >= 0 && newRow < spielBrett.length && newCol >= 0 && newCol < spielBrett[0].length) {
                            Field newField = spielBrett[newRow][newCol];
                            if (newField.getColor() == color && !newField.isConnected() && !newField.isConnectedByPC()) {
                                increase++;
                            }
                        }
                    }
                }
            }
        }
        return increase;
    }

    public void changeColorByFarbenPanel(int color) {

        if (!fenster.getMenütafel().isStarted() || !fenster.getMenütafel().isPlayable()) {  // 在这里检查游戏是否已经开始并且是否可以玩
            JOptionPane.showMessageDialog(null, "bitte zuerst klicken Playbutton", "Hinweisen", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 遍历所有的格子，如果一个格子与左下角相连，那么就将它的颜色设置为被点击的格子的颜色
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (field.isConnected()) {
                    field.setColor(color);
                }
            }
        }



        updateScores2();

        // 更新哪些格子与左下角相连
        updateConnectedFields();

        // 强制重绘面板，显示新的游戏板
        repaint();

        // 根据选中的策略执行相应的方法
        pcTurn(selectedStrategy);


// 更新分数
        updateScores();

        // 更新当前的回合
        if ( fenster.getMenütafel().getS1TurnLabel().getText().equals("S1 ist dran")) {
            fenster.getMenütafel().getS1TurnLabel().setText("PC ist dran");
        } else {
            fenster.getMenütafel().getS1TurnLabel().setText("S1 ist dran");
        }

        updateScores2();
        int s1Size = calculateComponentSize(true);
        int s2Size = calculateComponentSize(false);

        // 在游戏结束的情况下重置游戏
        if (previousSizes.size() == 4 && previousSizes.get(0).equals(previousSizes.get(1)) && previousSizes.get(1).equals(previousSizes.get(2)) && previousSizes.get(2).equals(previousSizes.get(3))) {
            fenster.getMenütafel().pauseTimer();
            JOptionPane.showMessageDialog(null, "The game is a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

            fenster.getMenütafel().resetGame();
        } else if (isFinalConfiguration()) {
            if (s1Size > s2Size) {
                fenster.getMenütafel().pauseTimer();
                JOptionPane.showMessageDialog(null, "S1 wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else if (s1Size < s2Size) {
                fenster.getMenütafel().pauseTimer();
                JOptionPane.showMessageDialog(null, "S2 wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            } else {
                fenster.getMenütafel().pauseTimer();
                JOptionPane.showMessageDialog(null, "The game is a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }

            fenster.getMenütafel().resetGame();
        }

    }


    public void updateScores() {
        int playerScore = 0;
        int pcScore = 0;
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (field.isConnected()) {
                    playerScore++;
                } else if (field.isConnectedByPC()) {
                    pcScore++;
                }
            }
        }

        // 更新分数标签
        fenster.getMenütafel().getS1ScoreLabel().setText("S1: " + playerScore);
        fenster.getMenütafel().getS2ScoreLabel().setText("S2: " + pcScore);
    }

    public void updateScores2() {
        int s1Size = calculateComponentSize(true);
        int s2Size = calculateComponentSize(false);
        previousSizes.add(s1Size + s2Size);
        if (previousSizes.size() > 4) {
            previousSizes.remove(0);
        }
        if (previousSizes.size() == 4 && previousSizes.get(0).equals(previousSizes.get(1)) && previousSizes.get(1).equals(previousSizes.get(2)) && previousSizes.get(2).equals(previousSizes.get(3))) {

        }
    }

    public int calculateComponentSize(boolean isPlayer) {
        int size = 0;
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if ((isPlayer && field.isConnected()) || (!isPlayer && field.isConnectedByPC())) {
                    size++;
                }
            }
        }
        return size;
    }

    public boolean isFinalConfiguration() {
        for (Field[] row : spielBrett) {
            for (Field field : row) {
                if (!field.isConnected() && !field.isConnectedByPC()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void clearPreviousSizes() {
        previousSizes.clear();
    }



}
