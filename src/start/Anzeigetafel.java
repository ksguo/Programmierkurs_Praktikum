package start;

import javax.swing.*;



import java.awt.*;




public class Anzeigetafel extends JPanel {



    private SpielBrettPanel spielBrettPanel;

    private FarbenPanel farbenPanel;

    private Fenster fenster;

    public Anzeigetafel(Fenster fenster) {

        this.fenster = fenster;

       // this.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.6), fenster.getHeight()));

      //  this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



/*

        spielBrettPanel = new SpielBrettPanel(fenster);



        farbenPanel = new FarbenPanel(fenster);

        // 创建 GridBagLayout 和 GridBagConstraints
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;  // 让组件充满其显示区域
        c.weightx = 1.0; // 让组件在水平方向上填充额外的空间


        spielBrettPanel = new SpielBrettPanel(fenster);
        c.gridy = 0;     // 第二行
        c.weighty = 0.75;  // 高度占比为50%
        this.add(spielBrettPanel, c);

        farbenPanel = new FarbenPanel(fenster);
        c.gridy = 1;     // 第三行
        c.weighty = 0.25; // 高度占比为25%
        this.add(farbenPanel, c);
*/

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));



        spielBrettPanel = new SpielBrettPanel(fenster);



        farbenPanel = new FarbenPanel(fenster);

        spielBrettPanel.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.6), (int)(fenster.getHeight()*0.75)));
        farbenPanel.setPreferredSize(new Dimension((int)(fenster.getWidth()*0.6), (int)(fenster.getHeight()*0.25)));

        this.add(spielBrettPanel);
        this.add(farbenPanel);
        this.setVisible(true);





    }

    public void handleColorSelection() {
        int selectedColor = farbenPanel.getSelectedColor();
        spielBrettPanel.changeColorByFarbenPanel(selectedColor);

        getFarbenPanel().repaint();  // 在这里添加
    }






    public SpielBrettPanel getSpielBrettPanel() {

        return spielBrettPanel;

    }

    public void setSpielBrettPanel(SpielBrettPanel spielBrettPanel) {

        this.spielBrettPanel = spielBrettPanel;

    }

    public FarbenPanel getFarbenPanel() {

        return farbenPanel;

    }

    public void setFarbenPanel(FarbenPanel farbenPanel) {

        this.farbenPanel = farbenPanel;

    }





}








