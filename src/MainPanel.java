import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

public class MainPanel extends JPanel {
    public static final int BORDER_THICK = 20;
    private String[] siteArray = {"manpeace", "aaa"};
    public static String[] genreArray_mampeace = {"스포츠", "연예인", "인물", "므흣", "유저짤"};
    private JPanel jPSet;
    private JPanel jPInput;
    private JPanel jPGenre;
    private JPanel jPLoading;
    private JPanel jPSaveLocation;

    private JLabel jLLoginID;
    private JLabel jLLoginPW;
    private JLabel jLEndDate;
    private JLabel jLStartDate;
    public JTextArea jTSaveLocation;
    public static JLabel jLPercent;

    public static JTextField jTLoginID;
    public static JPasswordField jTLoginPW;
    private JTextField jTStartDate;
    private JTextField jTEndDate;
    private JComboBox jCBSite;
    public static JProgressBar jProgressBar;
    private JCheckBox jCB1_manpeace;    // 스포츠 bo_table=ssam
    private JCheckBox jCB2_manpeace;    // 연예인 bo_table=celeb
    private JCheckBox jCB3_manpeace;    // 인물 bo_table=grateful
    private JCheckBox jCB4_manpeace;    // 꼴릿 bo_table=ggolit
    private JCheckBox jCB5_manpeace;    // 유저짤 bo_table=jap
    public static JButton jBtnCrawling;
    public static JButton jBtnSaveLocation;

    private JFileChooser jFileChooser;

    public static ImageIcon imgDownload = new ImageIcon("./image/download.png");//"./image/download.png"
    public static ImageIcon imgDownloading = new ImageIcon("./image/downloading.gif");
    public static ImageIcon imgComplete = new ImageIcon("./image/complete.png");
    public static ImageIcon imgSL = new ImageIcon("./image/탐색기1.png");

    private TitledBorder borderDate;
    private TitledBorder borderGenre;


    int btnCondition = 1; // 1 : download, 2 : downloading,  3 : complete
    public static boolean[] cBCondition = {false, false, false, false, false};
    CallBackEvent callBackEvent;

    public MainPanel() {
        callBackEvent = new CallBackEvent() {
            @Override
            public void callBackMethod() {
                System.out.println("콜백함수");
                jBtnCrawling.setIcon(imgDownloading);
            }
        };

        // component 초기화
        setvariable();
        // set option
        setComponent();
    }

    public void setvariable() {
        jPSet = new JPanel();
        jPInput = new JPanel();
        jPGenre = new JPanel();
        jPLoading = new JPanel();
        jPSaveLocation = new JPanel();

        jBtnSaveLocation = new JButton(imgSL);
        jBtnCrawling = new JButton(imgDownload);
        jLLoginID = new JLabel("ID");
        jLLoginPW = new JLabel("PW");
        jTLoginID = new JTextField("dbsdlswp11");
        jTLoginPW = new JPasswordField("1111");
        jLStartDate = new JLabel("Start Date ( **** . ** . ** )");
        jLEndDate = new JLabel("End Date ( **** . ** . ** )");
        jTStartDate = new JTextField("2019.03.30");
        jTEndDate = new JTextField("2019.03.30");

        jTSaveLocation = new JTextArea("버튼을 눌러 저장공간을 정하세요.");
        jLPercent = new JLabel("");

        jCBSite = new JComboBox<>(siteArray);
        jProgressBar = new JProgressBar();

        jCB1_manpeace = new JCheckBox(genreArray_mampeace[0]);    // 스포츠 bo_table=ssam
        jCB2_manpeace = new JCheckBox(genreArray_mampeace[1]);    // 연예인 bo_table=celeb
        jCB3_manpeace = new JCheckBox(genreArray_mampeace[2]);    // 인물 bo_table=grateful
        jCB4_manpeace = new JCheckBox(genreArray_mampeace[3]);    // 꼴릿 bo_table=ggolit
        jCB5_manpeace = new JCheckBox(genreArray_mampeace[4]);    // 유저짤 bo_table=jap

        jFileChooser = new JFileChooser();

        borderDate = new TitledBorder("");
        borderGenre = new TitledBorder("게시판");
    }

    public void setComponent() {
        borderDate.setTitleColor(Color.BLACK);
        borderDate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderDate.setTitleJustification(TitledBorder.CENTER);
        borderGenre.setTitleFont(new Font("aa", Font.BOLD, 20));
        borderGenre.setTitleColor(Color.BLACK);
        borderGenre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderGenre.setTitleJustification(TitledBorder.CENTER);

        // set JProgressBar option
        jProgressBar.setPreferredSize(new Dimension(270, 30));
        jProgressBar.setValue(0);

        // set JBtnCrawling option
        jBtnCrawling.setPreferredSize(new Dimension(imgDownload.getIconWidth(), imgDownload.getIconHeight()));
        jBtnCrawling.addActionListener(btnClickListener);
        jBtnCrawling.setHorizontalAlignment(SwingConstants.CENTER);
        jBtnCrawling.setVerticalAlignment(SwingConstants.CENTER);
        jBtnCrawling.setBorderPainted(false); // btn Border X
        jBtnCrawling.setContentAreaFilled(false);  // btn 내용영역 채우기 X
        jBtnCrawling.setFocusPainted(false);  // btn 클릭시 focus 테두리 X

        jBtnSaveLocation.setPreferredSize(new Dimension(30, 30));
        jBtnSaveLocation.addActionListener(btnClickListener);
        jBtnSaveLocation.setHorizontalAlignment(SwingConstants.CENTER);
        jBtnSaveLocation.setVerticalAlignment(SwingConstants.CENTER);
        jBtnSaveLocation.setBorderPainted(false); // btn Border X
        jBtnSaveLocation.setContentAreaFilled(false);  // btn 내용영역 채우기 X
        jBtnSaveLocation.setFocusPainted(false);  // btn 클릭시 focus 테두리 X

        jLLoginID.setVerticalAlignment(SwingConstants.CENTER);
        jLLoginID.setHorizontalAlignment(SwingConstants.CENTER);
        jLLoginPW.setVerticalAlignment(SwingConstants.CENTER);
        jLLoginPW.setHorizontalAlignment(SwingConstants.CENTER);

        jTLoginPW.setHorizontalAlignment(JTextField.CENTER);
        jTLoginID.setHorizontalAlignment(JTextField.CENTER);

        jLLoginID.setBorder(borderDate);
        jTLoginID.setBorder(borderDate);
        jLLoginPW.setBorder(borderDate);
        jTLoginPW.setBorder(borderDate);

        jLStartDate.setVerticalAlignment(SwingConstants.CENTER);
        jLStartDate.setHorizontalAlignment(SwingConstants.CENTER);
        jTStartDate.setHorizontalAlignment(JTextField.CENTER);
        jLEndDate.setVerticalAlignment(SwingConstants.CENTER);
        jLEndDate.setHorizontalAlignment(SwingConstants.CENTER);
        jTEndDate.setHorizontalAlignment(JTextField.CENTER);

        jLStartDate.setBorder(borderDate);
        jTStartDate.setBorder(borderDate);
        jLEndDate.setBorder(borderDate);
        jTEndDate.setBorder(borderDate);

        jTSaveLocation.setPreferredSize(new Dimension(260, 60));
        jTSaveLocation.setLineWrap(true);
        jLEndDate.setVerticalAlignment(SwingConstants.CENTER);
        jLEndDate.setHorizontalAlignment(SwingConstants.CENTER);
        jTEndDate.setHorizontalAlignment(JTextField.CENTER);

        jBtnSaveLocation.setBorder(borderDate);

        jFileChooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
        jFileChooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용
        jFileChooser.setDialogTitle("타이틀"); // 창의 제목
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드

        jLPercent.setPreferredSize(new Dimension(40, 30));
        jLPercent.setVerticalAlignment(SwingConstants.CENTER);
        jLPercent.setHorizontalAlignment(SwingConstants.CENTER);
        jLPercent.setHorizontalAlignment(JTextField.CENTER);

        //checkbox
        jCB1_manpeace.addItemListener(cBListener);
        jCB2_manpeace.addItemListener(cBListener);
        jCB3_manpeace.addItemListener(cBListener);
        jCB4_manpeace.addItemListener(cBListener);
        jCB5_manpeace.addItemListener(cBListener);

        //jPInput
        jPInput.setLayout(new GridLayout(4, 2));
        jPInput.setPreferredSize(new Dimension(300, 160));
        jPInput.add(jLLoginID);
        jPInput.add(jLLoginPW);
        jPInput.add(jTLoginID);
        jPInput.add(jTLoginPW);
        jPInput.add(jLStartDate);
        jPInput.add(jLEndDate);
        jPInput.add(jTStartDate);
        jPInput.add(jTEndDate);
        jPInput.setBorder(borderDate);


        //jPGenre
        jPGenre.setLayout(new GridLayout(3, 3));
        jPGenre.setPreferredSize(new Dimension(300, 80));
        jPGenre.setBorder(borderGenre);
        jPGenre.add(jCB1_manpeace);
        jPGenre.add(jCB2_manpeace);
        jPGenre.add(jCB3_manpeace);
        jPGenre.add(jCB4_manpeace);
        jPGenre.add(jCB5_manpeace);

        //jPSaveLocation
        jPSaveLocation.setLayout(new FlowLayout());
        jPSaveLocation.setPreferredSize(new Dimension(300, 80));
        jPSaveLocation.add(jTSaveLocation);
        jPSaveLocation.add(jBtnSaveLocation);

        //jPSet
        jPSet.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        jPSet.add(jPInput, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        jPSet.add(jPGenre, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        jPSet.add(jPSaveLocation, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        jPSet.add(jBtnCrawling, gbc);

        //jPLoading
        jPLoading.setLayout(new FlowLayout());
        jPLoading.add(jProgressBar);
        jPLoading.add(jLPercent);

        //MainPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_THICK, BORDER_THICK, BORDER_THICK, BORDER_THICK));
        add(jCBSite, BorderLayout.NORTH);
        add(jPSet, BorderLayout.CENTER);
        add(jPLoading, BorderLayout.SOUTH);
    }

    String fileChooser() {
        String folderPath = "";
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Binary File", "cd11"); // filter 확장자 추가
        jFileChooser.setFileFilter(filter); // 파일 필터를 추가

        int returnVal = jFileChooser.showOpenDialog(null); // 열기용 창 오픈
        if (returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭
            folderPath = jFileChooser.getSelectedFile().toString();
        } else if (returnVal == JFileChooser.CANCEL_OPTION) { // 취소를 클릭
            System.out.println("cancel");
            folderPath = "";
        }
        return folderPath;
    }

        public static String getPW (JPasswordField jTLoginPW){
            String password = "";
            char[] pw = jTLoginPW.getPassword();

            for (char c : pw) {
                Character.toString(c);
                password += c;
            }

            return password;
        }

        public interface CallBackEvent {
            public void callBackMethod();
        }

        ActionListener btnClickListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(jBtnCrawling)) {
                    switch (btnCondition) {
                        case 1:
                            jBtnCrawling.setIcon(imgDownloading);
                            btnCondition = 2;
                            try {
                                if (jBtnCrawling.getIcon().equals(imgDownloading)) {
                                    System.out.println("아이디 : " + jTLoginID.getText() + "   비번 : " + getPW(jTLoginPW));
                                    Crawling crawling = new Crawling(callBackEvent, jTLoginID.getText(), getPW(jTLoginPW), jTStartDate.getText(), jTEndDate.getText(), jTSaveLocation.getText()+"/");
                                    crawling.start();
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            // crawling
                            break;
                        case 2:
                            System.out.println("click downloading");
                            // no action
                            break;
                        case 3:
                            System.out.println("click complete");
                            jBtnCrawling.setIcon(imgDownload);
                            break;
                        default:
                            System.out.println();
                    }
                } else if (e.getSource().equals(jBtnSaveLocation)) {
                    jTSaveLocation.setText(fileChooser());
                }
            }
        };
        ItemListener cBListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getSource().equals(jCB1_manpeace)) {
                    if (jCB1_manpeace.isSelected()) {
                        System.out.println("jCB1_manpeace 체크 !");
                        cBCondition[0] = true;
                    } else {
                        System.out.println("jCB1_manpeace 언체크 !");
                        cBCondition[0] = false;
                    }
                } else if (e.getSource().equals(jCB2_manpeace)) {
                    if (jCB2_manpeace.isSelected()) {
                        System.out.println("jCB2_manpeace 체크 !");
                        cBCondition[1] = true;
                    } else {
                        System.out.println("jCB2_manpeace 언체크 !");
                        cBCondition[1] = false;
                    }
                } else if (e.getSource().equals(jCB3_manpeace)) {
                    if (jCB3_manpeace.isSelected()) {
                        System.out.println("jCB3_manpeace 체크 !");
                        cBCondition[2] = true;
                    } else {
                        System.out.println("jCB3_manpeace 언체크 !");
                        cBCondition[2] = false;
                    }
                } else if (e.getSource().equals(jCB4_manpeace)) {
                    if (jCB4_manpeace.isSelected()) {
                        System.out.println("jCB4_manpeace 체크 !");
                        cBCondition[3] = true;
                    } else {
                        System.out.println("jCB4_manpeace 언체크 !");
                        cBCondition[3] = false;
                    }
                } else if (e.getSource().equals(jCB5_manpeace)) {
                    if (jCB5_manpeace.isSelected()) {
                        System.out.println("jCB5_manpeace 체크 !");
                        cBCondition[4] = true;
                    } else {
                        System.out.println("jCB5_manpeace 언체크 !");
                        cBCondition[4] = false;
                    }
                }
            }
        };
    }