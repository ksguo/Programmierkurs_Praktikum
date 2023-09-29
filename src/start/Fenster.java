package start;

import javax.swing.*;

import logic.Field;

import java.awt.event.*;

import java.awt.*;


public class Fenster extends JFrame {

    private Menütafel menütafel;
    private Anzeigetafel anzeigetafel;




    public Fenster() {
        this.setSize(new Dimension(600,600));

        this.setMinimumSize(new Dimension(600,600));

        this.setLocationRelativeTo(null);//窗口居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);



        menütafel = new Menütafel(this);
        anzeigetafel = new Anzeigetafel(this);



        this.setLayout(new BorderLayout());//窗口布局


        menütafel = new Menütafel(this);
        anzeigetafel = new Anzeigetafel(this);
        menütafel.setPreferredSize(new Dimension((int)(this.getWidth()*0.4), this.getHeight()));
        anzeigetafel.setPreferredSize(new Dimension((int)(this.getWidth()*0.6), this.getHeight()));
        this.add(menütafel,BorderLayout.EAST);
        this.add(anzeigetafel,BorderLayout.CENTER);

// 添加ComponentListener以侦听组件大小的更改
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
               super.componentResized(e);
                menütafel.setPreferredSize(new Dimension((int)(getWidth()*0.4), getHeight()));
                anzeigetafel.setPreferredSize(new Dimension((int)(getWidth()*0.6), getHeight()));
          validate();  // 重新计算布局
            }
        });







        this.setVisible(true); // 将窗口设置为可见状态



    }




    public Menütafel getMenütafel() {
        return menütafel;
    }



    public void setMenütafel(Menütafel menütafel) {
        this.menütafel = menütafel;
    }



    public Anzeigetafel getAnzeigetafel() {
        return anzeigetafel;
    }



    public void setAnzeigetafel(Anzeigetafel anzeigetafel) {
        this.anzeigetafel = anzeigetafel;
    }




}
