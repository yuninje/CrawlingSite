import javax.swing.*;

public class Window extends JFrame {
    public final static int WIDTH = 400;
    public final static int HEIGHT = 550;

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
