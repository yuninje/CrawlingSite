package UI;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;

    //BasePanel basePanel;
    String[] siteList = {"10000img", "manpeace"};
    String[] nBList = {"인터넷이슈", "스포츠", "연예인", "인물"};

    Container c = this.getContentPane();
    MainPanel mainPanel;
    public Window() {
        setTitle("Website Crawling");
        setSize(WIDTH, HEIGHT);

        add(mainPanel = new MainPanel());
        setVisible(true);
        this.setLocationRelativeTo(null);   // 모니터 가운데 띄우
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     // 폼 종료시 프로그램 종료

    }
}
