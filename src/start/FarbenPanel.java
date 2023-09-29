package start;

import javax.swing.*;


import java.awt.event.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.awt.Color;


public class FarbenPanel extends JPanel {

 private Fenster fenster;

    private List<Color> colorList; // 存储颜色
    private int numColors; // 颜色的数量

    private int selectedColor = -1;

    private int forbiddenColor1 = -1;
    private int forbiddenColor2 = -1;


    public FarbenPanel(Fenster fenster) {

        this.fenster = fenster;
        // 设置面板的背景颜色为解码后的颜色
        this.setBackground(Color.decode("#F0F8FF"));
      // this.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.6), (int)(fenster.getHeight()*0.15)));
     //   this.setLayout(new GridLayout(1,1));
        // 初始化颜色列表
        colorList = Arrays.asList( Color.decode("#E6E6FA"),
                Color.decode("#BEBEBE"),
                Color.decode("#6A5ACD"),
                Color.decode("#00BFFF"),
                Color.decode("#00CED1"),
                Color.decode("#FFD700"),
                Color.decode("#FF6A6A"),
                Color.decode("#9400D3"),
                Color.decode("#FFFACD"));
        this.setVisible(true);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                if (!fenster.getMenütafel().isStarted() || !fenster.getMenütafel().isPlayable()) {  // 在这里检查游戏是否已经开始并且是否可以玩
                    JOptionPane.showMessageDialog(null, "bitte zuerst klicken Playbutton", "Hinweisen", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }

                if (e.getID() == KeyEvent.KEY_TYPED) {
                    char keyChar = e.getKeyChar();

                    // 检查按键是否是数字
                    if (Character.isDigit(keyChar)) {
                        int colorIndex = Character.getNumericValue(keyChar) - 1;  // 将字符转换为数字并减1，因为颜色索引是从0开始的

                        // 检查数字是否在颜色范围内
                        if (colorIndex >= 0 && colorIndex < numColors) {
                            // 获取电脑当前的颜色
                            int pcColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0][fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0].length - 1].getColor();
// 获取玩家当前的颜色
                            int playerColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett().length - 1][0].getColor();

                            // 如果玩家点击的颜色是电脑当前的颜色，那么就返回并不做任何操作
                            if (colorIndex == pcColor || colorIndex == playerColor) {
                                return false;
                            }

                            // 将选中的颜色存储起来
                            setSelectedColor(colorIndex);
                            // 调用Anzeigetafel的方法来处理颜色的选择
                            fenster.getAnzeigetafel().handleColorSelection();



                        }
                    }
                }

                return false;  // 允许事件继续向下传播
            }
        });




        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (!fenster.getMenütafel().isStarted() || !fenster.getMenütafel().isPlayable()) {  // 在这里检查游戏是否已经开始并且是否可以玩
                    JOptionPane.showMessageDialog(null, "bitte zuerst klicken Playbutton", "Hinweisen", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // 计算每种颜色的代表应该有多大，以及应该在何处开始绘制，以使它们在面板中居中
                int colorWidth = (int)(getWidth() * 0.9) / numColors;
                int colorHeight = (int)(getHeight() * 0.9);
                int squareSize = Math.min(colorWidth, colorHeight);  // 确保格子是正方形

                int offsetX = (getWidth() - squareSize * numColors) / 2;  // 计算偏移量，使得颜色代表在面板中居中
                int offsetY = (getHeight() - squareSize) / 2;

                // 计算点击的格子的坐标
                int x = (e.getX() - offsetX) / colorWidth;
                int y = (e.getY() - offsetY) / colorHeight;
                // 获取被点击的格子的颜色


                // 检查点击位置是否在颜色选项的区域内
                if (x < 0 || x >= numColors || y < 0 || y >= numColors) {
                    return;  // 如果不在，则直接返回，不执行后续的操作
                }

                int pcColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0][fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0].length - 1].getColor();
                int playerColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett().length - 1][0].getColor();

                int clickedColor = y * numColors + x;
                if (clickedColor == pcColor || clickedColor == playerColor) {
                    return;
                }

                setSelectedColor(clickedColor);



                // 将选中的颜色存储起来
                setSelectedColor(x);


                // 调用Anzeigetafel的方法来处理颜色的选择
                fenster.getAnzeigetafel().handleColorSelection();


            }
        });



        this.requestFocusInWindow();

    this.setFocusable(true);

    }
    public void setNumColors(int numColors) {
        this.numColors = numColors;
        repaint(); // 重绘面板以显示新的颜色数量
    }
    public void clearFarben() {
        numColors = 0;
        repaint();  // 请求重新绘制面板，这将调用 paintComponent() 方法
    }
    public void setForbiddenColors(int color1, int color2) {
        this.forbiddenColor1 = color1;
        this.forbiddenColor2 = color2;
        repaint();  // 需要重新绘制面板来显示新的禁止颜色
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (numColors > 0) {

            // 计算每种颜色的代表应该有多大，以及应该在何处开始绘制，以使它们在面板中居中
            int cellSize = Math.min(getWidth() / numColors, getHeight());
            int offsetX = (getWidth() - cellSize * numColors) / 2;
            int offsetY = (getHeight() - cellSize) / 2;

            for (int i = 0; i < numColors; i++) {
                g.setColor(colorList.get(i));
                g.fillRect(i * cellSize + offsetX, offsetY, cellSize, cellSize);

                // 添加黑色边框
                g.setColor(Color.BLACK);
                g.drawRect(i * cellSize + offsetX, offsetY, cellSize, cellSize);

                // 获取电脑当前的颜色和玩家当前的颜色
                int pcColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0][fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[0].length - 1].getColor();
                int playerColor = fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett()[fenster.getAnzeigetafel().getSpielBrettPanel().getSpielBrett().length - 1][0].getColor();

                // 如果当前绘制的颜色是电脑或玩家的颜色，那么在颜色代表上绘制一个红色的X
                if (i == pcColor || i == playerColor) {
                    g.setColor(Color.RED);
                    g.drawLine(i * cellSize + offsetX, offsetY, (i + 1) * cellSize + offsetX, offsetY + cellSize);
                    g.drawLine(i * cellSize + offsetX, offsetY + cellSize, (i + 1) * cellSize + offsetX, offsetY);
                }

                // 计算数字的位置
                int numberX = i * cellSize + offsetX + cellSize / 2;
                int numberY = offsetY + cellSize / 2;
                FontMetrics fontMetrics = g.getFontMetrics();
                int numberWidth = fontMetrics.stringWidth(Integer.toString(i));
                int numberHeight = fontMetrics.getHeight();
                int numberXOffset = numberWidth / 2;
                int numberYOffset = numberHeight / 2;

                // 设置数字居中
                numberX -= numberXOffset;
                numberY += numberYOffset;

                // 绘制数字
                g.setColor(Color.BLACK);
                g.drawString(Integer.toString(i+1), numberX, numberY);
            }
        }
    }



    public void setSelectedColor(int color) {
        this.selectedColor = color;

    }

    public int getSelectedColor() {
        return this.selectedColor;
    }





}

