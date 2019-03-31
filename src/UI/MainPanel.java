package UI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {


    public static final int BORDER_THICK = 20;


    JButton jBtnNow;
    JButton jBtnCrawling;
    JButton jBtnDownloading;
    JButton jBtnComplete;
    ImageIcon imgDownload = new ImageIcon("./image/download.png");//"./image/download.png"
    ImageIcon imgDownloading = new ImageIcon("./image/downloading.gif");
    ImageIcon imgComplete = new ImageIcon("./image/complete.png");

    String[] siteArray = {"manpeace", "aaa"};
    String[] genreArray_mampeace = {"스포츠", "연예인", "인물", "므흣", "유저짤"};

    int btnCondition = 1; // 1 : download, 2 : downloading,  3 : complete
    public MainPanel() {
        // component 선언
        JPanel jPSet = new JPanel();
        JPanel JPDate = new JPanel();

        jBtnCrawling = new JButton(imgDownload);
        jBtnDownloading = new JButton(imgDownloading);
        jBtnComplete = new JButton(imgComplete);

        jBtnNow = jBtnCrawling;

        JLabel jLStartDate = new JLabel("Start Date ( **** . ** . ** )");
        JLabel jLEndDate = new JLabel("End Date ( **** . ** . ** )");
        JTextField jTStartDate = new JTextField("2019.03.30");
        JTextField jTEndDate = new JTextField("2019.03.30");
        JComboBox jCBSite = new JComboBox<>(siteArray);
        JProgressBar jProgressBar = new JProgressBar();

        JPanel jPGenre_manpeace = new JPanel();
        JCheckBox jCB1_manpeace = new JCheckBox(genreArray_mampeace[0]);    // 스포츠 bo_table=ssam
        JCheckBox jCB2_manpeace = new JCheckBox(genreArray_mampeace[1]);    // 연예인 bo_table=celeb
        JCheckBox jCB3_manpeace = new JCheckBox(genreArray_mampeace[2]);    // 인물 bo_table=grateful
        JCheckBox jCB4_manpeace = new JCheckBox(genreArray_mampeace[3]);    // 꼴릿 bo_table=ggolit
        JCheckBox jCB5_manpeace = new JCheckBox(genreArray_mampeace[4]);    // 유저짤 bo_table=jap

        TitledBorder borderDate = new TitledBorder("");
        borderDate.setTitleColor(Color.BLACK);
        borderDate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderDate.setTitleJustification(TitledBorder.CENTER);

        TitledBorder borderGenre = new TitledBorder("게시판");
        borderGenre.setTitleFont(new Font("aa", Font.BOLD, 20));
        borderGenre.setTitleColor(Color.BLACK);
        borderGenre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderGenre.setTitleJustification(TitledBorder.CENTER);

        jProgressBar.setPreferredSize(new Dimension(200, 30));
        jProgressBar.setValue(50);

        // btn setPreferedSize
        jBtnCrawling.setPreferredSize(new Dimension(imgDownload.getIconWidth(), imgDownload.getIconHeight()));
        jBtnComplete.setPreferredSize(new Dimension(imgDownload.getIconWidth(), imgDownload.getIconHeight()));
        jBtnDownloading.setPreferredSize(new Dimension(imgDownload.getIconWidth(), imgDownload.getIconHeight()));

        jBtnCrawling.addActionListener(btnClickListener);

        // btn align
        jBtnCrawling.setHorizontalAlignment(SwingConstants.CENTER);
        jBtnDownloading.setHorizontalAlignment(SwingConstants.CENTER);
        jBtnComplete.setHorizontalAlignment(SwingConstants.CENTER);
        jBtnCrawling.setVerticalAlignment(SwingConstants.CENTER);
        jBtnDownloading.setVerticalAlignment(SwingConstants.CENTER);
        jBtnCrawling.setVerticalAlignment(SwingConstants.CENTER);

        // btn Border X
        jBtnCrawling.setBorderPainted(false);
        jBtnDownloading.setBorderPainted(false);
        jBtnComplete.setBorderPainted(false);

        // btn 내용영역 채우기 X
        jBtnCrawling.setContentAreaFilled(false);
        jBtnDownloading.setContentAreaFilled(false);
        jBtnComplete.setContentAreaFilled(false);

        // btn 클릭시 focus 테두리 X
        jBtnCrawling.setFocusPainted(false);
        jBtnDownloading.setFocusPainted(false);
        jBtnComplete.setFocusPainted(false);

        jLStartDate.setVerticalAlignment(SwingConstants.CENTER);
        jLStartDate.setHorizontalAlignment(SwingConstants.CENTER);
        jLEndDate.setVerticalAlignment(SwingConstants.CENTER);
        jLEndDate.setHorizontalAlignment(SwingConstants.CENTER);
        jTStartDate.setHorizontalAlignment(JTextField.CENTER);
        jTEndDate.setHorizontalAlignment(SwingConstants.CENTER);

        jLStartDate.setBorder(borderDate);
        jLEndDate.setBorder(borderDate);
        jTStartDate.setBorder(borderDate);
        jTEndDate.setBorder(borderDate);


        //JPDate
        JPDate.setLayout(new GridLayout(2, 2));
        JPDate.setPreferredSize(new Dimension(300, 80));
        JPDate.add(jLStartDate);
        JPDate.add(jLEndDate);
        JPDate.add(jTStartDate);
        JPDate.add(jTEndDate);
        JPDate.setBorder(borderDate);

        //jPGenre_manpeace
        jPGenre_manpeace.setLayout(new GridLayout(3, 3));
        jPGenre_manpeace.setPreferredSize(new Dimension(300, 80));
        jPGenre_manpeace.setBorder(borderGenre);
        jPGenre_manpeace.add(jCB1_manpeace);
        jPGenre_manpeace.add(jCB2_manpeace);
        jPGenre_manpeace.add(jCB3_manpeace);
        jPGenre_manpeace.add(jCB4_manpeace);
        jPGenre_manpeace.add(jCB5_manpeace);

        //jPSet
        jPSet.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        jPSet.add(JPDate, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPSet.add(jPGenre_manpeace, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPSet.add(jBtnCrawling, gbc);

        //MainPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_THICK, BORDER_THICK, BORDER_THICK, BORDER_THICK));
        add(jCBSite, BorderLayout.NORTH);
        add(jPSet, BorderLayout.CENTER);
        add(jProgressBar, BorderLayout.SOUTH);


    }

    ActionListener btnClickListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(jBtnCrawling)) {
                switch (btnCondition) {
                    case 1:
                        System.out.println("click download ");
                        jBtnCrawling.setIcon(imgDownloading);
                        // crawling
                        break;
                    case 2:
                        System.out.println("click downloading");
                        // no action
                        break;
                    case  3:
                        System.out.println("click complete");
                        jBtnCrawling.setIcon(imgDownload);
                        break;
                    default:

                        System.out.println();
                }
            }
        }
    };
}
