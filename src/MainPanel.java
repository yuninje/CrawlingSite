import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Properties;

public class MainPanel extends JPanel {
    private static final int BORDER_THICK = 20;

    private Crawling crawling;
    private String[] siteArray = {"manpeace", "aaa"};
    private  static String[] genreArray_mampeace = {"스포츠", "연예인", "인물", "므흣", "유저짤"};

    private static final String loginID = "dbsdlswp11";
    private static final String loginPW = "1111";
    private String saveLocation = "";
    private String startDate = "";
    private String endDate = "";

    private JPanel jPSet;
    private JPanel jPInput;
    private JPanel jPGenre;
    private JPanel jPLoading;
    private JPanel jPSaveLocation;

    private JTextArea jTSaveLocation;
    public static JTextArea jTProgressText;

    private JLabel jLStartDate;
    private JLabel jLEndDate;
    private JPanel jPanel1;
    private JPanel jPanel2;

    private JComboBox jCBSite;
    private  static JProgressBar jProgressBar;
    private JCheckBox jCB1_manpeace;    // 스포츠 bo_table=ssam
    private JCheckBox jCB2_manpeace;    // 연예인 bo_table=celeb
    private JCheckBox jCB3_manpeace;    // 인물 bo_table=grateful
    private JCheckBox jCB4_manpeace;    // 꼴릿 bo_table=ggolit
    private JCheckBox jCB5_manpeace;    // 유저짤 bo_table=jap
    public static JButton jBtnCrawling;
    private JButton jBtnSaveLocation;
    private JScrollPane jScrollPane;
    private JFileChooser jFileChooser;

    public static ImageIcon imgDownload = new ImageIcon("./image/download.png");//"./image/download.png"
    public static ImageIcon imgDownloading = new ImageIcon("./image/downloading.gif");
    public static ImageIcon imgComplete = new ImageIcon("./image/complete.png");
    public static ImageIcon imgSL = new ImageIcon("./image/탐색기1.png");

    private TitledBorder borderDate;
    private TitledBorder borderGenre;

    private GridBagConstraints gbc = new GridBagConstraints();
    private UtilDateModel modelSD;
    private Properties propertiesSD;
    private JDatePanelImpl jDatePanelSD;
    private JDatePickerImpl jDatePickerSD;

    private UtilDateModel modelED;
    private Properties propertiesED;
    private JDatePanelImpl jDatePanelED;
    private JDatePickerImpl jDatePickerED;

    public static int btnCondition = 1; // 1 : download, 2 : downloading,  3 : complete
    public static boolean[] cBCondition = {false, false, false, false, false};
    public static boolean[] falseArray = {false, false, false, false, false};
    private boolean setLocationFlag = false;

    private CallBackEvent callBackEvent;

    public MainPanel() {
        callBackEvent = new CallBackEvent() {
            @Override
            public void callBackMethod() {
                System.out.println("콜백함수");
                jBtnCrawling.setIcon(imgDownloading);
                jTProgressText.setFont(new Font("s",Font.BOLD,10));
            }
        };

        // component 초기화
        setvariable();
        // set option
        setComponent();
    }

    private void setvariable() {
        jPSet = new JPanel();
        jPInput = new JPanel();
        jPGenre = new JPanel();
        jPLoading = new JPanel();
        jPSaveLocation = new JPanel();

        jBtnSaveLocation = new JButton(imgSL);
        jBtnCrawling = new JButton(imgDownload);
        jLStartDate = new JLabel("Start Date");
        jLEndDate = new JLabel("End Date");
        jPanel1 = new JPanel();
        jPanel2 = new JPanel();

        modelSD = new UtilDateModel();
        propertiesSD = new Properties();
        jDatePanelSD = new JDatePanelImpl(modelSD, propertiesSD);
        jDatePickerSD = new JDatePickerImpl(jDatePanelSD, new DateLabelFormatter());

        modelED = new UtilDateModel();
        propertiesED = new Properties();
        jDatePanelED = new JDatePanelImpl(modelED, propertiesED);
        jDatePickerED = new JDatePickerImpl(jDatePanelED, new DateLabelFormatter());

        jTSaveLocation = new JTextArea("오른쪽 버튼을 눌러 저장공간을 정하세요.");
        jTProgressText = new JTextArea("Download File List");
        jScrollPane = new JScrollPane(jTProgressText);

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

    private void setComponent() {
        propertiesSD.put("text.today", "Today");
        propertiesSD.put("text.month", "Month");
        propertiesSD.put("text.year", "Year");
        propertiesED.put("text.today", "Today");
        propertiesED.put("text.month", "Month");
        propertiesED.put("text.year", "Year");

        borderDate.setTitleColor(Color.BLACK);
        borderDate.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderDate.setTitleJustification(TitledBorder.CENTER);

        borderGenre.setTitleFont(new Font("aa", Font.BOLD, 20));
        borderGenre.setTitleColor(Color.BLACK);
        borderGenre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        borderGenre.setTitleJustification(TitledBorder.CENTER);


        jLStartDate.setPreferredSize(new Dimension(60, 40));
        jLEndDate.setPreferredSize(new Dimension(60, 40));
        jLStartDate.setVerticalAlignment(SwingConstants.CENTER);
        jLStartDate.setHorizontalAlignment(SwingConstants.CENTER);
        jLEndDate.setVerticalAlignment(SwingConstants.CENTER);
        jLEndDate.setHorizontalAlignment(SwingConstants.CENTER);


        // set JProgressBar option
        jProgressBar.setPreferredSize(new Dimension(300, 30));
        jProgressBar.setValue(0);
        jProgressBar.setBorder(borderDate);

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


        //jTSaveLocation
        jTSaveLocation.setPreferredSize(new Dimension(260, 60));
        jTSaveLocation.setLineWrap(true);
        jTSaveLocation.setBorder(borderDate);


        jTProgressText.setLineWrap(true);
        jTProgressText.setBorder(borderDate);
        jTProgressText.setFont(new Font("aaa", Font.BOLD, 15));

        jScrollPane.setPreferredSize(new Dimension(300, 60));

        //jBtnSaveLocation
        jBtnSaveLocation.setBorder(borderDate);


        //jFileChooser
        jFileChooser.setCurrentDirectory(new File("/")); // 현재 사용 디렉토리를 지정
        jFileChooser.setAcceptAllFileFilterUsed(true);   // Fileter 모든 파일 적용
        jFileChooser.setDialogTitle("타이틀"); // 창의 제목
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // 파일 선택 모드


        //checkbox
        jCB1_manpeace.addItemListener(cBListener);
        jCB2_manpeace.addItemListener(cBListener);
        jCB3_manpeace.addItemListener(cBListener);
        jCB4_manpeace.addItemListener(cBListener);
        jCB5_manpeace.addItemListener(cBListener);

        jLStartDate.setPreferredSize(new Dimension(60, 30));
        jPanel1.add(jLStartDate);
        jPanel1.add(jDatePickerSD);


        jLEndDate.setPreferredSize(new Dimension(60, 30));
        jPanel2.add(jLEndDate);
        jPanel2.add(jDatePickerED);

        //jPInput
        jPInput.setLayout(new GridLayout(2, 1));
        jPInput.setPreferredSize(new Dimension(300, 80));

        jPInput.add(jPanel1);
        jPInput.add(jPanel2);

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
        gbc.gridx = 0;
        gbc.gridy = 4;
        jPSet.add(jScrollPane, gbc);

        //jPLoading
        jPLoading.setLayout(new FlowLayout());
        jPLoading.add(jProgressBar);

        //MainPanel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(BORDER_THICK, BORDER_THICK, BORDER_THICK, BORDER_THICK));
        add(jCBSite, BorderLayout.NORTH);
        add(jPSet, BorderLayout.CENTER);
        add(jPLoading, BorderLayout.SOUTH);
    }

    private String fileChooser() {
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

    public interface CallBackEvent {
        public void callBackMethod();
    }

    ActionListener btnClickListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(jBtnCrawling)) {
                if (setLocationFlag ) {
                    startDate = jDatePickerSD.getJFormattedTextField().getText();
                    endDate = jDatePickerED.getJFormattedTextField().getText();
                    if(!startDate.equals("") && !endDate.equals("") && Crawling.compareDate(startDate,endDate)!= 1) {
                        if(falseArray != cBCondition) {
                            switch (btnCondition) {
                                case 1:
                                    jBtnCrawling.setIcon(imgDownloading);
                                    btnCondition = 2;

                                    try {
                                        crawling = new Crawling(callBackEvent, loginID, loginPW,startDate, endDate, saveLocation);
                                        crawling.start();
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    break;
                                case 2:
                                    System.out.println("click downloading");
                                    // no action
                                    break;
                                case 3:
                                    jBtnCrawling.setIcon(imgDownload);
                                    btnCondition = 1;
                                    break;
                                default:
                                    System.out.println();
                            }
                        }else{
                            JOptionPane.showMessageDialog(Main.window, "선택한 게시판을 확인하세요.");
                        }
                    }else{
                        JOptionPane.showMessageDialog(Main.window, "입력하신 날짜를 확인하세요.");
                    }
                } else {
                    JOptionPane.showMessageDialog(Main.window, "저장할 폴더를 선택해주세요");
                }
            } else if (e.getSource().equals(jBtnSaveLocation)) {
                saveLocation = fileChooser() + "/";
                if (saveLocation.equals("/")) {
                } else {
                    jTSaveLocation.setText(saveLocation);
                    jTSaveLocation.setFont(new Font("a", Font.BOLD, 10));
                    setLocationFlag = true;
                }

            }
        }
    };

    ItemListener cBListener = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource().equals(jCB1_manpeace)) {
                if (jCB1_manpeace.isSelected()) {
                    cBCondition[0] = true;
                } else {
                    cBCondition[0] = false;
                }
            } else if (e.getSource().equals(jCB2_manpeace)) {
                if (jCB2_manpeace.isSelected()) {
                    cBCondition[1] = true;
                } else {
                    cBCondition[1] = false;
                }
            } else if (e.getSource().equals(jCB3_manpeace)) {
                if (jCB3_manpeace.isSelected()) {
                    cBCondition[2] = true;
                } else {
                    cBCondition[2] = false;
                }
            } else if (e.getSource().equals(jCB4_manpeace)) {
                if (jCB4_manpeace.isSelected()) {
                    cBCondition[3] = true;
                } else {
                    cBCondition[3] = false;
                }
            } else if (e.getSource().equals(jCB5_manpeace)) {
                if (jCB5_manpeace.isSelected()) {
                    cBCondition[4] = true;
                } else {
                    cBCondition[4] = false;
                }
            }
        }
    };
}