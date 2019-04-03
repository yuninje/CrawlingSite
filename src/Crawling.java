import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Crawling extends Thread {
    public static final int POST_PER_PAGE = 16;
    public static final String SITE_BASE_URL = "http://manpeace.net/";
    public static final String SITE_NAME = "manpeace";
    private static final String LOGIN_SITE = "http://manpeace.net/bbs/login.php";
    private String[] genreArray_mampeace = {"스포츠", "연예인", "인물", "므흣", "유저짤"};
    private int[] genreNBCount = {0, 0, 0, 0, 1};
    String[] genreUrlArray = {
            "http://manpeace.net/bbs/board.php?bo_table=ssam&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=celeb&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=grateful&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=ggolit&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=jap&page=1"
    };
    private static String progressText = "";
    static File folder;

    List<Post> postList = new ArrayList<>();
    List<Content> contentList = new ArrayList<>();
    Content content;

    String failedList = "Failed List...";
    private static String loginID = "";
    private static String loginPW = "";
    private static String startDate = "";
    private static String endDate = "";
    private static String saveLocation = "";


    String postNum = "";
    String newPostNum = "";

    int num;

    int total;
    int done = 0;
    int failed = 0;
    int arrayIndex;

    public static WebDriver webDriver;
    String chromeDriver = "C:\\Users\\yuninje\\Desktop\\INJE\\Coding\\Library\\Web driver\\chromedriver.exe";

    public Crawling(MainPanel.CallBackEvent event, String loginID, String loginPW, String startDate, String endDate, String saveLocation) {
        System.out.println("Crawling 생성자함수");
        event.callBackMethod();

        this.loginID = loginID;
        this.loginPW = loginPW;
        this.startDate = startDate;
        this.endDate = endDate;
        this.saveLocation = saveLocation;
    }

    public void run() {
        try {
            System.out.println("run()");
            System.setProperty("webdriver.chrome.driver", chromeDriver);
            webDriver = new ChromeDriver();
            webDriver.get(LOGIN_SITE);
            Thread.sleep(500);
            webDriver.findElement(By.id("login_id")).sendKeys(loginID);
            webDriver.findElement(By.id("login_pw")).sendKeys(loginPW);
            webDriver.findElement(By.xpath("//*[@id=\"thema_wrapper\"]/div/div[2]/div[1]/div/div[1]/div/div/div[2]/form/div[3]/div/button")).click();

            arrayIndex = 0;
            for (String genreUrl : genreUrlArray) {
                setReset();
                if (MainPanel.cBCondition[arrayIndex]) {
                    System.out.println("================================================================================================    " + genreArray_mampeace[arrayIndex] + "     ===========");
                    setTotalPostList(genreUrl);
                    filtOutPost();
                    getContents();
                    total = contentList.size();
                    downloadFile(genreArray_mampeace[arrayIndex]);
                }
                arrayIndex++;
            }
            System.out.println("완료");
            webDriver.close();
            progressText = progressText + "\n" + failedList;
            MainPanel.jBtnCrawling.setIcon(MainPanel.imgComplete);
            MainPanel.btnCondition = 3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printPostList() {
        int x = 0;
        for (Post post : postList) {
            System.out.println(x + "  -  post url : " + post.url + "    post date : " + post.date + "      post postNum : " + post.postNum);
            x++;
        }
    }

    void setReset() {
        postList = new ArrayList<>();
        contentList = new ArrayList<>();
    }

    List<Post> getPostList(String genreUrl, int page) throws Exception {
        List<Post> pL = new ArrayList<>();
        Post p = new Post();
        webDriver.get(genreUrl + "&page=" + page);
        WebElement wEPostBody = webDriver.findElement(By.xpath("//*[@id=\"fboardlist\"]/div[1]/table/tbody"));
        List<WebElement> wETrList = wEPostBody.findElements(By.tagName("tr"));
        int j = 0;
        int nBCount;
        if(page ==1) {
            nBCount = genreNBCount[arrayIndex];
        }else{
            nBCount = 0;
        }
        for (WebElement wETr : wETrList) {
            List<WebElement> wETdList = wETr.findElements(By.tagName("td"));
            int i = 0;
            if (nBCount == 0) {
                for (WebElement wETd : wETdList) {
                    if (i == 1) {
                        p = new Post();
                        p.url = wETd.findElement(By.tagName("a")).getAttribute("href");
                        System.out.println(j + " - p url : " + wETd.findElement(By.tagName("a")).getAttribute("href"));
                        p.postNum = getPostNum(p.url);

                    } else if (i == 2) {
                        p.date = wETd.getText();
                        System.out.println("           p.postNum : " + p.postNum + "            p date : " + wETd.getText());
                    }
                    i++;
                }
                pL.add(p);
                j++;
            } else {
                nBCount--;
            }
        }

        return pL;
    }

    String getExtension(String fileName) {
        return fileName.substring(fileName.length() - 6, fileName.length()).split("\\.")[1];
    }

    void downloadFile(String genre) throws Exception {
        num = 1;
        for (Content content : contentList) {
            done++;
            num++;

            newPostNum = content.postNum;
            if (!postNum.equals(newPostNum)) {
                num = 1;
                postNum = newPostNum;
            }

            String extension = getExtension(content.urlString);
            // manpeace 폴더
            folder = new File(saveLocation + SITE_NAME + "\\");
            if (!folder.exists()) {
                folder.mkdir();
            }
            // genre 폴더
            folder = new File(saveLocation + SITE_NAME + "\\" + genre + "\\");
            if (!folder.exists()) {
                folder.mkdir();
            }
            // post 폴더
            folder = new File(saveLocation + SITE_NAME + "\\" + genre + "\\" + postNum + "\\");
            if (!folder.exists()) {
                folder.mkdir();
            }

            if (content.urlString.substring(0, 1).equals("/")) {
                content.urlString = SITE_BASE_URL + content.urlString;
            }

            System.out.println("url : " + content.urlString + " 다운로드 시작");
            //jTProgressText setText, set Scroll Position
            progressText = progressText + "\n" + saveLocation + SITE_NAME + "\\" + genre + "\\" + postNum + "\\" + postNum + "." + extension + "\n     (" + (done) + "\\" + total + ")     Downloading...";
            MainPanel.jTProgressText.setCaretPosition(MainPanel.jTProgressText.getDocument().getLength());

            if (content.type == 1) {
                downloadImage(content.urlString, postNum, extension, saveLocation + SITE_NAME + "\\" + genre + "\\" + postNum + "\\");
            } else {
                downVideo(content.urlString, postNum, extension, saveLocation + SITE_NAME + "\\" + genre + "\\" + postNum + "\\");
            }

            progressText = progressText + "-----> Saved !";
            MainPanel.jTProgressText.setText(progressText);
            MainPanel.jTProgressText.setCaretPosition(MainPanel.jTProgressText.getDocument().getLength());
            System.out.println("                             ----->다운로드 완료");
        }
    }

    void downloadImage(String contentUrl, String postNum, String extension, String saveLocation) throws Exception {
        try {
            System.out.print("이미지 저장 : " + saveLocation + postNum + "-" + num + "." + extension);
            byte[] b = new byte[1];
            URL url = new URL(contentUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            DataInputStream di = new DataInputStream(urlConnection.getInputStream());
            FileOutputStream fo = new FileOutputStream(saveLocation + postNum + "-" + num + "." + extension);
            while (-1 != di.read(b, 0, 1))
                fo.write(b, 0, 1);
            di.close();
            fo.close();
        } catch (IOException e) {
            failedList = failedList + "\n" + contentUrl;
            System.out.println("다운로드 실패");
            return;
        }
    }

    void downVideo(String contentUrl, String postNum, String extension, String saveLocation) {
        try {
            System.out.println("동영상 저장 : " + saveLocation + postNum + "-" + num + "." + extension);
            byte[] b = new byte[1];
            URL url = new URL(contentUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            DataInputStream di = new DataInputStream(urlConnection.getInputStream());
            FileOutputStream fo = new FileOutputStream(saveLocation + postNum + "-" + num + "." + extension);
            while (-1 != di.read(b, 0, 1))
                fo.write(b, 0, 1);
            di.close();
            fo.close();
        } catch (IOException e) {
            failedList = failedList + "\n" + contentUrl;
            System.out.println("다운로드 실패");
            return;
        }
    }

    void getContents() {
        content = new Content();
        for (Post post : postList) {
            System.out.println("===================================================================================================================");
            webDriver.get(post.url);
            WebElement wEContentBody = webDriver.findElement(By.xpath("//*[@id=\"thema_wrapper\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[3]"));

            List<WebElement> wEVideoList = wEContentBody.findElements(By.tagName("source"));
            List<WebElement> wEImageList = wEContentBody.findElements(By.tagName("img"));
            for (WebElement webElement1 : wEVideoList) {
                System.out.println("video src : " + webElement1.getAttribute("src"));
                content = new Content();
                content.urlString = webElement1.getAttribute("src");
                content.postNum = post.postNum;
                content.type = 2;
                contentList.add(content);
            }
            for (WebElement webElement1 : wEImageList) {
                System.out.println("image src : " + webElement1.getAttribute("src"));
                content = new Content();
                content.urlString = webElement1.getAttribute("src");
                content.postNum = post.postNum;
                content.type = 1;
                contentList.add(content);
            }
        }
    }

    public String getPostNum(String url) {
        System.out.println("getPostNum : " + url);
        String postNum = url;
        postNum = postNum.split("wr_id=")[1];
        postNum = postNum.split("&")[0];
        return postNum;
    }

    public static Document getDocument(String siteUrl) {
        Document pageDocument;
        webDriver.get(siteUrl);
        String html = webDriver.getPageSource();
        pageDocument = Jsoup.parse(html);
        return pageDocument;
    }

    public static int inttoString(String a, int startNum, int endNum) {
        return Integer.parseInt(a.substring(startNum, endNum));
    }

    public static int compareDate(String firstDate, String lastDate) {
        int result;
        int firstDateYear = inttoString(firstDate, 0, 4);
        int firstDateMonth = inttoString(firstDate, 5, 7);
        int firstDateDay = inttoString(firstDate, 8, 10);

        int lastDateYear = inttoString(lastDate, 0, 4);
        int lastDateMonth = inttoString(lastDate, 5, 7);
        int lastDateDay = inttoString(lastDate, 8, 10);

        if (firstDateYear > lastDateYear) {
            //firstDate > lastDate
            result = 1;
        } else if (firstDateYear == lastDateYear) {
            if (firstDateMonth > lastDateMonth) {
                //firstDate > lastDate
                result = 1;
            } else if (firstDateMonth == lastDateMonth) {
                if (firstDateDay > lastDateDay) {
                    //firstDate > lastDate
                    result = 1;
                } else if (firstDateDay == lastDateDay) {
                    //firstDate == lastDate
                    result = 0;
                } else {
                    // firstDate < lastDate
                    result = -1;
                }
            } else {
                // firstDate < lastDate
                result = -1;
            }
        } else {
            result = -1;
            // firstDate < lastDate
        }
        return result;
    }

    public void setTotalPostList(String genreUrl) throws Exception {
        System.out.println("setTotalPostList()");
        // 1페이지부터 포스트 만들기
        List<Post> pList;

        for (int page = 1; ; page++) {
            pList = getPostList(genreUrl, page);
            if (compareDate(pList.get(POST_PER_PAGE - 1).date, endDate) == 1) {
                //page++
            } else {
                if (compareDate(startDate, pList.get(0).date) == 1)
                    break;
                else {//save
                    for (Post p : pList) {
                        postList.add(p);
                    }
                }
            }
        }
    }

    void filtOutPost() {
        ArrayList<Integer> removeIndex = new ArrayList<>();
        int r = 0;
        for (Post post : postList) {
            if (compareDate(post.date, endDate) != 1 && compareDate(startDate, post.date) != 1) {
                //놔두기.
            } else {
                removeIndex.add(r);
            }
            r++;
        }
        for (int i = removeIndex.size() - 1; i >= 0; i--) {
            postList.remove(removeIndex.get(i).intValue());
        }
    }
}
