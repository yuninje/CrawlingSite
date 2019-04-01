import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Crawling extends Thread {
    public static final int POST_PER_PAGE = 16;
    public static final String SITE_BASE_URL = "http://manpeace.net/";
    public static final String SITE_NAME = "manpeace";
    private static final String LOGIN_SITE = "http://manpeace.net/bbs/login.php?urlString=http://manpeace.net/";
    private static Connection.Response loginPageResponse;
    private static Map<String, String> loginTryCookie;
    private static Document loginPageDocument;
    private static String stx;
    private static String url;
    private static Map<String, String> data;
    private static Connection.Response response;
    public static Map<String, String> loginCookie;

    static File file;
    static File folder;

    Document mainDocument;
    URL fileUrl;

    List<Post> postList = new ArrayList<>();
    List<Content> contentList = new ArrayList<>();

    Elements elements;
    Elements elementsVideo;
    Elements elementsImg;
    Elements elementsA;

    String imgString = "";
    String videoString = "";

    private static String loginID = "";
    private static String loginPW = "";
    private static String startDate = "";
    private static String endDate = "";
    private static String saveLocation = "";


    String postNum = "";
    String newPostNum = "";

    String[] genreUrlArray = {
            "http://manpeace.net/bbs/board.php?bo_table=ssam&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=celeb&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=grateful&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=ggolit&page=1",
            "http://manpeace.net/bbs/board.php?bo_table=jap&page=1"
    };

    int elementsVideoCount;
    int elementsImgCount;
    int elementsACount;
    int num = 1;

    int total =    contentList.size();
    int done = 0;

    private MainPanel.CallBackEvent callbackEvent;

    public Crawling(MainPanel.CallBackEvent event, String loginID, String loginPW, String startDate, String endDate, String saveLocation) throws Exception {
        System.out.println("Crawling 생성자함수");
        callbackEvent = event;
        callbackEvent.callBackMethod();

        this.loginID = loginID;
        this.loginPW = loginPW;
        this.startDate = startDate;
        this.endDate = endDate;
        this.saveLocation = saveLocation;
        //this.saveLocation = saveLocation


        //로그인
        getToken();
        getSessionID();
        mainDocument = getDocument(SITE_BASE_URL);

    }

    public void run() {
        System.out.println("run()");
        try {
            int arrayIndex = 0;
            for (String genreUrl : genreUrlArray) {
                if (MainPanel.cBCondition[arrayIndex]) {
                    System.out.println("================================================================================================    " + MainPanel.genreArray_mampeace[arrayIndex] + "     ===========");

                    setTotalPostList(genreUrl);
                    filtOutPost();
                    getContents();

                    total =    contentList.size();
                    done = 0;

                    downloadFile();
                }
                arrayIndex++;
            }
            System.out.println("완료");
            MainPanel.jBtnCrawling.setIcon(MainPanel.imgComplete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    List<Post> getPostList(String genreUrl, int page) throws Exception {
        List<Post> postList = new ArrayList<>();
        Post post;
        int postListCount = 0;

        Document d = getDocument(genreUrl + "&page=" + page);
        Elements elementsUrl = d.select("tr").select("td").select("a");
        Elements elementsDate = d.select("tr").select("td");

        for (Element element : elementsUrl) {
            post = new Post(element.attr("href"), getPostNum(element.attr("href")));
            postList.add(post);
        }

        int k = 0;
        for (Element element : elementsDate) {
            if ((k - 2) % 4 == 0) {
                postList.get(postListCount).date = element.text();
                postListCount++;
            }
            k++;
        }

        return postList;
    }

    String getExtension(String fileName) {
        return fileName.substring(fileName.length() - 6, fileName.length()).split("\\.")[1];
    }

    void downloadFile() throws Exception {
        for(Content content : contentList) {
            System.out.println("total : "+total + "     done : " + done + "    done/total : "+(done*100)/total);
            MainPanel.jLPercent.setText(done+"/"+total);
            MainPanel.jProgressBar.setValue((done*100)/total);
            done++;
            num++;

            newPostNum = content.postNum;
            System.out.println("postNum = "+postNum + "      newPostNum = "+newPostNum);
            if(!postNum.equals(newPostNum)){
                num = 1;
                postNum = newPostNum;
            }

            String extension = "";
            folder = new File(saveLocation + SITE_NAME + "/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            folder = new File(saveLocation + SITE_NAME + "/" + postNum+"/");
            if (!folder.exists()) {
                folder.mkdir();
            }
            if (content.urlString.substring(0, 1).equals("/")) {
                content.urlString = SITE_BASE_URL + content.urlString;
            }

            System.out.print(content.urlString + " 다운로드 시작");

            extension = getExtension(content.urlString);

            fileUrl = new URL(content.urlString);
            if (content.type == 1) {
                downloadImage(fileUrl, postNum,extension, saveLocation + SITE_NAME + "/" + postNum + "/");
            } else {
                downVideo(fileUrl, postNum,extension, saveLocation + SITE_NAME + "/" + postNum + "/");
            }
            System.out.println(" ----->다운로드 완료");

        }
    }

    void downloadImage(URL fileUrl, String postNum, String extension, String saveLocation) throws Exception {
        System.out.println("postNum = "+postNum+"\n이미지 저장 : "+saveLocation + postNum + "-" + num + "." + extension);
        byte[] b = new byte[1];
        URLConnection urlConnection = fileUrl.openConnection();
        urlConnection.connect();
        DataInputStream di = new DataInputStream(urlConnection.getInputStream());
        FileOutputStream fo = new FileOutputStream(saveLocation + postNum + "-" + num + "." + extension);
        while (-1 != di.read(b, 0, 1))
            fo.write(b, 0, 1);
        di.close();
        fo.close();
    }

    void downVideo(URL fileUrl,String postNum,  String extension, String saveLocation) throws Exception {
        System.out.println("postNum = "+postNum+"\n동영상 저장 : "+saveLocation + postNum + "-" + num + "." + extension);
        byte[] b = new byte[1];
        URLConnection urlConnection = fileUrl.openConnection();
        urlConnection.connect();
        DataInputStream di = new DataInputStream(urlConnection.getInputStream());
        FileOutputStream fo = new FileOutputStream(saveLocation + postNum + "-" + num + "." + extension);
        while (-1 != di.read(b, 0, 1))
            fo.write(b, 0, 1);
        di.close();
        fo.close();
    }

    void getContents() throws Exception {
        for(Post post : postList){
            Document doc = getDocument(post.url);

            elements = doc.select("div").eq(36).select("div").eq(7);
            elementsVideo = elements.select("video").select("source");
            elementsImg = elements.select("img");
            elementsA = elements.select("a");

            elementsVideoCount = elementsVideo.indexOf(elementsVideo.last());
            elementsImgCount = elementsImg.indexOf(elementsImg.last());
            elementsACount = elementsA.indexOf(elementsA.last());

            for (Element element : elementsVideo) {
                videoString = element.attr("src");
                if (getExtension(videoString).equals("mp4")) {
                    contentList.add(new Content(videoString, 2, post.postNum));
                }
            }
            for (Element element : elementsImg) {
                imgString = element.attr("src");
                contentList.add(new Content(imgString, 1, post.postNum));
            }
        }
    }
    public String getPostNum(String url){
        String postNum = url;
        System.out.println("postNum = "+postNum);
        postNum = postNum.split("wr_id=")[1];
        postNum = postNum.split("&")[0];
        return postNum;
    }

    public static void getToken() throws IOException {
        loginPageResponse = Jsoup.connect(LOGIN_SITE)
                .timeout(3000)
                .header("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Cookie", "_ga=GA1.2.1091269015.1553798095; _gid=GA1.2.1826486296.1553798095; __gads=ID=71de50cab6ee4a47:T=1553798094:S=ALNI_MYmK-yGtFtcCtF-c9_E0_HMyKdL1Q; PHPSESSID=e9p7tqfvnbr6rdfqt60kvq1a95; e1192aefb64683cc97abb83c71057733=amFnZQ%3D%3D; 2a0d2363701f23f8a75028924a3af643=MTEwLjkuMzcuMjMz; impx={%22imp_usy%22:{%22capCount%22:5%2C%22capExpired%22:1554097347}%2C%22screen%22:null}; GED_PLAYLIST_ACTIVITY=W3sidSI6InM4T1kiLCJ0c2wiOjE1NTQwMTI0MjUsIm52IjowLCJ1cHQiOjE1NTQwMTI0MDksImx0IjoxNTU0MDEyNDE3fV0.; _gat_gtag_UA_131352783_1=1; impx_a={%22count%22:53%2C%22timestamp%22:1554016514}")
                .header("Host", "manpeace.net")
                .header("If-Modified-Since", "Sun, 31 Mar 2019 07:15:10 GMT")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .execute();

        // 로그인 페이지에서 얻은 쿠키
        loginTryCookie = loginPageResponse.cookies();

        // 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
        loginPageDocument = loginPageResponse.parse();

        stx = loginPageDocument.select("input.fp").val();
        url = loginPageDocument.select("input.urlString").val();
    }

    public static void getSessionID() throws IOException {

        //전송할 폼 데이터
        data = new HashMap<>();
        data.put("mb_id", loginID);
        data.put("mb_password", loginPW);
        data.put("urlString", "http://manpeace.net/bbs/search.php");
        data.put("urlString", "http%3A%2F%2Fmanpeace.net%2F");
        data.put("stx", "");

        //로그인
        response = Jsoup.connect(LOGIN_SITE)
                .timeout(3000)
                .header("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Cookie", "_ga=GA1.2.1091269015.1553798095; _gid=GA1.2.1826486296.1553798095; __gads=ID=71de50cab6ee4a47:T=1553798094:S=ALNI_MYmK-yGtFtcCtF-c9_E0_HMyKdL1Q; PHPSESSID=e9p7tqfvnbr6rdfqt60kvq1a95; e1192aefb64683cc97abb83c71057733=amFnZQ%3D%3D; 2a0d2363701f23f8a75028924a3af643=MTEwLjkuMzcuMjMz; impx={%22imp_usy%22:{%22capCount%22:5%2C%22capExpired%22:1554097347}%2C%22screen%22:null}; GED_PLAYLIST_ACTIVITY=W3sidSI6InM4T1kiLCJ0c2wiOjE1NTQwMTI0MjUsIm52IjowLCJ1cHQiOjE1NTQwMTI0MDksImx0IjoxNTU0MDEyNDE3fV0.; _gat_gtag_UA_131352783_1=1; impx_a={%22count%22:53%2C%22timestamp%22:1554016514}")
                .header("Host", "manpeace.net")
                .header("If-Modified-Since", "Sun, 31 Mar 2019 07:15:10 GMT")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .cookies(loginTryCookie)
                .data(data)
                .method(Connection.Method.POST)
                .execute();

        System.out.println("Login Complete");

        // 로그인 성공 후 얻은 쿠키.
        // 쿠키 중 TSESSION 이라는 값을 확인할 수 있다.
        loginCookie = response.cookies();
    }

    public static Document getDocument(String siteUrl) throws IOException {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

        Document PageDocument = Jsoup.connect(siteUrl)
                .userAgent(userAgent)
                .header("Accept", "ext/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Cookie", "_ga=GA1.2.1091269015.1553798095; _gid=GA1.2.1826486296.1553798095; __gads=ID=71de50cab6ee4a47:T=1553798094:S=ALNI_MYmK-yGtFtcCtF-c9_E0_HMyKdL1Q; PHPSESSID=e9p7tqfvnbr6rdfqt60kvq1a95; e1192aefb64683cc97abb83c71057733=amFnZQ%3D%3D; 2a0d2363701f23f8a75028924a3af643=MTEwLjkuMzcuMjMz; impx={%22imp_usy%22:{%22capCount%22:5%2C%22capExpired%22:1554097347}%2C%22screen%22:null}; GED_PLAYLIST_ACTIVITY=W3sidSI6InM4T1kiLCJ0c2wiOjE1NTQwMTI0MjUsIm52IjowLCJ1cHQiOjE1NTQwMTI0MDksImx0IjoxNTU0MDEyNDE3fV0.; _gat_gtag_UA_131352783_1=1; impx_a={%22count%22:53%2C%22timestamp%22:1554016514}")
                .header("Host", "manpeace.net")
                .header("If-Modified-Since", "Sun, 31 Mar 2019 07:15:10 GMT")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .cookies(loginCookie)
                .get();
        return PageDocument;
    }

    public int inttoString(String a, int startNum, int endNum) {
        return Integer.parseInt(a.substring(startNum, endNum));
    }

    public boolean compareDate(String firstDate, String lastDate) {
        boolean result = true;
        int firstDateYear = inttoString(firstDate, 0, 4);
        int firstDateMonth = inttoString(firstDate, 5, 7);
        int firstDateDay = inttoString(firstDate, 8, 10);
        int lastDateYear = inttoString(lastDate, 0, 4);
        int lastDateMonth = inttoString(lastDate, 5, 7);
        int lastDateDay = inttoString(lastDate, 8, 10);

        if (firstDateYear > lastDateYear) {
            //firstDate > lastDate
            result = true;
        } else if (firstDateYear == lastDateYear) {
            if (firstDateMonth > lastDateMonth) {
                //firstDate > lastDate
                result = true;
            } else if (firstDateMonth == lastDateMonth) {
                if (firstDateDay > lastDateDay) {
                    result = true;
                } else if (firstDateDay == lastDateDay) {
                    result = false;
                } else if (firstDateDay < lastDateDay) {
                    // firstDate < lastDate
                    result = false;
                }
            } else if (firstDateMonth < lastDateMonth) {
                result = false;
                // firstDate < lastDate
            }
        } else {
            result = false;
            // firstDate < lastDate
        }
        return result;
    }

    public void setTotalPostList(String genreUrl) throws Exception {
        System.out.println("setTotalPostList()");
        // 1페이지부터 포스트 만들기
        List<Post> pList;
        // start page


        for (int page = 1; ; page++) {
            pList = getPostList(genreUrl, page);
            if (compareDate(pList.get(POST_PER_PAGE - 1).date, endDate)) {
                // bot>End
                break;
            } else if (compareDate(startDate, pList.get(0).date)) {
                //start > top
                break;
            } else {
                for (Post p : pList) {
                    postList.add(p);
                }
            }
        }
    }

    void filtOutPost() throws Exception{
        List<Integer> removeIndex = new ArrayList<>();
        for (Post post : postList) {
            if (compareDate(post.date, endDate) || compareDate(startDate, post.date)) {
                removeIndex.add(postList.indexOf(post));
            }
            num = 1;
        }
        for(int i =removeIndex.size()-1; i>=0;i--) {
            postList.remove(i);//걸러내기
        }
    }
}
