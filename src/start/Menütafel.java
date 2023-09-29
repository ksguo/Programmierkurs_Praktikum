package start;

import javax.swing.*;

import java.awt.event.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Menütafel extends JPanel {


    private Fenster fenster;
    private JPanel oberesMenu;
    private JPanel unteresMenu;
    //for oberes Panel
    private JButton startButton;
    private JButton playButton;
    private boolean isStarted = false;
    private Timer timer;


    private JLabel timerLabel;
    //for unteres Panel

    private JLabel beginLabel;
    private JRadioButton S1;
    private JRadioButton S2;


    //游戏条件设置
    private List<JSpinner> spinners = new ArrayList<>();
    private JComboBox<String> comboBox;

    private JButton introButton ;
    private boolean isPlayable = false;  // 添加此行来表示游戏是否可以开始

    private boolean isPlayerTurn = false;

    private JPanel SpielstandPanel;
    private  JLabel  spielstandLabel;
    private  JLabel s1ScoreLabel;
    private  JLabel s2ScoreLabel;
    private JLabel s1TurnLabel;


    public Menütafel(Fenster fenster) {

        this.fenster = fenster;


        this.setPreferredSize(new Dimension((int)(this.getWidth()*0.4), this.getHeight()));
        this.setLayout(new GridLayout(2,1));


        // Oberes Panel erstellen
        oberesMenu = new JPanel();

        oberesMenu.setLayout(new GridLayout(3, 1));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1,2));// 创建顶部面板，自动默认为FlowLayout，只有一个组件，所以看起来就像是一列
        timerLabel = new JLabel("Timer 00:00:00");
      //  timerLabel.setPreferredSize(new Dimension(125,60));
        Font font = new Font(timerLabel.getFont().getName(),Font.BOLD,15);
        timerLabel.setFont(font);
       topPanel.add(timerLabel);
        topPanel.setBackground(Color.decode("#F0F8FF"));

       oberesMenu.add(topPanel);

        JPanel bottomPanel = new JPanel(); // 创建底部面板
        bottomPanel.setLayout(new GridLayout(1, 2)); // 设置底部面板为一行，两列
        bottomPanel.setBackground(Color.decode("#F0F8FF"));
        // Start- und Stop-Buttons erstellen
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(110,60));

        // 在Menütafel构造函数中，添加以下代码
        startButton.addActionListener(e -> {
            if (!isStarted) {

                // 设置为已开始状态，修改按钮文本
                isStarted = true;
                isPlayable = false;  // 游戏初始化后，还不能立即开始游戏
                startButton.setText("Stop");

                // Read the values from the spinners
                int farben = (int) spinners.get(0).getValue();
                int zeilen = (int) spinners.get(1).getValue();
                int spalten = (int) spinners.get(2).getValue();


                fenster.getAnzeigetafel().getSpielBrettPanel().createSpielBrett(zeilen, spalten, farben);
                // 可以在这里更新FarbenPanel的颜色数量

                fenster.getAnzeigetafel().getFarbenPanel().setNumColors(farben);




            } else {

                // 设置为未开始状态，修改按钮文本
                isStarted = false;
                isPlayable = false;  // 游戏已经停止，不能开始游戏
                startButton.setText("Start");
                // 将策略重置为“Stagnation”
                comboBox.setSelectedItem("Stagnation");
                fenster.getAnzeigetafel().getSpielBrettPanel().clearSpielBrett();
                fenster.getAnzeigetafel().getFarbenPanel().clearFarben();

                resetGame(); // 停止游戏并重置所有设置
            }
        });




        playButton = new JButton("Play");
        playButton.setPreferredSize(new Dimension(110,60));

        bottomPanel.add(startButton);
        bottomPanel.add(playButton);
        // Buttons zum oberesMenu hinzufügen
        oberesMenu.add(bottomPanel);



        timer = new Timer(1000, new ActionListener() {
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                if(seconds == 60) {
                    minutes++;
                    seconds = 0;
                }
                if(minutes == 60) {
                    hours++;
                    minutes = 0;
                }
                // Update the timer label
                timerLabel.setText(String.format("Timer %02d:%02d:%02d", hours, minutes, seconds));
            }
        });


        // Nested JPanel with GridLayout in the CENTER
        JPanel beginPanel = new JPanel(new GridLayout(3, 1));
        //  beginPanel.setPreferredSize(new Dimension(200, 80));
        //beginPanel.setLayout(new GridLayout(3,1));
        beginPanel.setBackground(Color.decode("#F0F8FF"));
        //谁开始游戏
        beginLabel = new JLabel("Who begins the game");
        beginPanel.add(beginLabel);

        // Create the radio buttons S1 and S2
        S1 = new JRadioButton("S1", true);									// Radio Buttons um startenden Spieler auszuwählen
        S2 = new JRadioButton("S2", false);


        ButtonGroup group = new ButtonGroup();							//Button Group erstellen damit immer nur ein RadioButton ausgewählt ist
        group.add(S1);
        group.add(S2);

        ActionListener radioActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                if (radioButton.isSelected()) {
                    isPlayerTurn = true;
                    //System.out.print(s1Clicked);
                }
            }
        };
        ActionListener radioActionListener2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JRadioButton radioButton = (JRadioButton) e.getSource();
                if (radioButton.isSelected()) {
                    isPlayerTurn = false;
                    //System.out.print(s1Clicked);
                }
            }
        };

        S1.addActionListener(radioActionListener);
        S2.addActionListener(radioActionListener2);

        // Add the radio buttons to the panel
        beginPanel.add(S1);
        beginPanel.add(S2);

        oberesMenu.add(beginPanel);



        this.add(oberesMenu);




        // Unteres Panel erstellen
        unteresMenu = new JPanel();

       // unteresMenu.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.4), (int)(fenster.getHeight()*0.7)));
        unteresMenu.setLayout(new GridLayout(4, 1));


        //条件选择设置
        JPanel conditionPanel = new JPanel(new GridLayout(3, 1));
       // conditionPanel.setPreferredSize(new Dimension(220, 300));

        conditionPanel.setBackground(Color.decode("#F0F8FF"));

        String[] conditions = { "Farben (4-9):", "Zeilen (3-10):", "Spalten (3-10):" };
        int[] conditionDefaults = { 5, 6, 6 };
        int[][] conditionRanges = { {4, 9}, {3, 10}, {3, 10} };

        for (int i = 0; i < conditions.length; i++) {

            conditionPanel.add(new JLabel(conditions[i]));

            JSpinner spinner = new JSpinner(new SpinnerNumberModel(conditionDefaults[i], conditionRanges[i][0], conditionRanges[i][1], 1));
            spinners.add(spinner);
            conditionPanel.add(spinner);


        }

        unteresMenu.add(conditionPanel);

        JPanel strategyPanel = new JPanel(new GridLayout(1,1));

        strategyPanel.add(new JLabel("Strategie PC:"));
        strategyPanel.setBackground(Color.decode("#F0F8FF"));
        String[] strategies = { "Stagnation", "Greedy", "Blocking" };
        comboBox = new JComboBox<>(strategies);
        comboBox.setSelectedItem(strategies[0]);  // Set default strategy to "Strategie01"


        strategyPanel.add(comboBox);

        unteresMenu.add(strategyPanel);


        SpielstandPanel = new JPanel(new GridLayout(1,3));

        SpielstandPanel.setBackground(Color.decode("#F0F8FF"));

        Font font2 = new Font("Arial", Font.PLAIN, 10);

        spielstandLabel = new JLabel("Spielstand:");
        spielstandLabel.setFont(font2);

        s1ScoreLabel = new JLabel("S1: 1");
        s1ScoreLabel.setFont(font2);

        s2ScoreLabel = new JLabel("S2: 1");
        s2ScoreLabel.setFont(font2);

        s1TurnLabel = new JLabel("S1 ist dran");
        s1TurnLabel.setFont(font2);


        SpielstandPanel.add(spielstandLabel);
        SpielstandPanel.add(s1ScoreLabel);
        SpielstandPanel.add(s2ScoreLabel);
        SpielstandPanel.add(s1TurnLabel);
        unteresMenu.add(SpielstandPanel);


        //confirm条件
        JPanel introPanel = new JPanel(new GridLayout(1,1));  // Makes button centered
        introPanel.setBackground(Color.decode("#F0F8FF"));
       // introPanel.setPreferredSize(new  Dimension(100, 20));
         introButton = new JButton("Bedienungsanleitung");
      // introButton.setPreferredSize(new Dimension(40, 20));


        JLabel b1 = new JLabel("Bedienungsanleitunge:");
        JLabel b2 = new JLabel("-The aim of the game is to have a larger colored area than your opponent");
        JLabel b3 = new JLabel("-You can choose a color in two different ways");
        JLabel b4 = new JLabel("1. By clicking on the desired color");
        JLabel b5 = new JLabel("2. By clicking the corresponding number on the keyboard");
        introButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame bed = new JFrame("Introduction");
                bed.setSize(500,300);
                bed.setLocationRelativeTo(null);
                bed.setLayout(new GridLayout(5,1));
                bed.add(b1);
                bed.add(b2);
                bed.add(b3);
                bed.add(b4);
                bed.add(b5);
                bed.setVisible(true);

            }
        });
        introPanel.add(introButton);

        unteresMenu.add(introPanel);





        //play按钮变化设置
        playButton.addActionListener(e -> {

            // 在开始游戏时，检查是否已经点击过 startButton
            if (!isStarted) {
                JOptionPane.showMessageDialog(null, "请先点击start按钮开始游戏", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!timer.isRunning()) {
                timer.start();
                playButton.setText("Pause");

                isPlayable = true;  // 开始游戏后，才允许玩家操作

                // Disable settings elements
                for (JSpinner spinner : spinners) {
                    spinner.setEnabled(false);
                }
                S1.setEnabled(false);
                S2.setEnabled(false);
                comboBox.setEnabled(false);
                introButton.setEnabled(false);

                if(S2.isSelected()){
                    fenster.getAnzeigetafel().getSpielBrettPanel().executePCTurn();
                }
               // fenster.getAnzeigetafel().getSpielBrettPanel().executePCTurn();

                // Get the selected strategy and set the selected strategy of SpielBrettPanel
                String strategy = (String) comboBox.getSelectedItem();
                fenster.getAnzeigetafel().getSpielBrettPanel().setSelectedStrategy(strategy);



            } else {

                timer.stop();
                // Reset timer by re-initializing it
                timer = new Timer(1000, new ActionListener() {
                    int hours = 0;
                    int minutes = 0;
                    int seconds = 0;
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        seconds++;
                        if(seconds == 60) {
                            minutes++;
                            seconds = 0;
                        }
                        if(minutes == 60) {
                            hours++;
                            minutes = 0;
                        }
                        // Update the timer label
                        timerLabel.setText(String.format("Timer %02d:%02d:%02d", hours, minutes, seconds));
                    }
                });
                //  timerLabel.setText("Timer 00:00:00");
                playButton.setText("Play");

                // Enable settings elements
                for (JSpinner spinner : spinners) {
                    spinner.setEnabled(true);
                }
                S1.setEnabled(true);
                S2.setEnabled(true);
                comboBox.setEnabled(true);
                introButton.setEnabled(true);

                // Reset radio buttons
                S1.setSelected(true);

                // Reset spinner and comboBox to defaults
                int[] spinnerDefaults = {5, 6, 6};
                for (int i = 0; i < spinners.size(); i++) {
                    JSpinner spinner = spinners.get(i);
                    spinner.setValue(spinnerDefaults[i]);
                }

              // comboBox.setSelectedItem("Stagnation");
                isPlayable = false;  // 暂停游戏后，不允许玩家操作

            }
        });






        this.add(unteresMenu);
        this.setVisible(true);



    }
/*
getter setter
 */
    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(boolean playable) {
        isPlayable = playable;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        isPlayerTurn = playerTurn;
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public void setComboBox(JComboBox<String> comboBox) {
        this.comboBox = comboBox;
    }

    public JLabel getS1TurnLabel() {
        return s1TurnLabel;
    }

    public void setS1TurnLabel(JLabel s1TurnLabel) {
        this.s1TurnLabel = s1TurnLabel;
    }

    public JLabel getS1ScoreLabel() {
        return s1ScoreLabel;
    }


    public void setS1ScoreLabel(JLabel s1ScoreLabel) {
        this.s1ScoreLabel = s1ScoreLabel;
    }

    public JLabel getS2ScoreLabel() {
        return s2ScoreLabel;
    }

    public void setS2ScoreLabel(JLabel s2ScoreLabel) {
        this.s2ScoreLabel = s2ScoreLabel;
    }

    /*
                            getter setter
                             */
    public void resetGame() {
        // Stop the game
        isStarted = false;
        isPlayable = false;  // 游戏已经停止，不能开始游戏
        startButton.setText("Start");

        // Clear the game board and colors
        fenster.getAnzeigetafel().getSpielBrettPanel().clearSpielBrett();
        fenster.getAnzeigetafel().getFarbenPanel().clearFarben();


        // 重置玩家和电脑的得分
         getS1ScoreLabel().setText("S1: 1");
        getS2ScoreLabel().setText("S2: 1");
        getS1TurnLabel().setText("S1 ist dran");
        // Reset the settings
        comboBox.setSelectedItem("Stagnation");
        for (JSpinner spinner : spinners) {
            spinner.setEnabled(true);
        }
        S1.setEnabled(true);
        S2.setEnabled(true);
        comboBox.setEnabled(true);
        introButton.setEnabled(true);

        // Reset timer
        timer.stop();

        timer = new Timer(1000, new ActionListener() {
            int hours = 0;
            int minutes = 0;
            int seconds = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                if(seconds == 60) {
                    minutes++;
                    seconds = 0;
                }
                if(minutes == 60) {
                    hours++;
                    minutes = 0;
                }
                // Update the timer label
                timerLabel.setText(String.format("Timer %02d:%02d:%02d", hours, minutes, seconds));
            }
        });
        timerLabel.setText("Timer 00:00:00");
        playButton.setText("Play");

        // Reset radio buttons
        S1.setSelected(true);

        // Reset spinner and comboBox to defaults
        int[] spinnerDefaults = {5, 6, 6};
        for (int i = 0; i < spinners.size(); i++) {
            JSpinner spinner = spinners.get(i);
            spinner.setValue(spinnerDefaults[i]);

fenster.getAnzeigetafel().getSpielBrettPanel().clearPreviousSizes();
        }
    }


    // 定义一个暂停的方法
    public void pauseTimer() {
        if(timer != null && timer.isRunning()) {
            timer.stop();


        }
    }



}
