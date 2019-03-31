import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Crawling{
    public static final String SITE_BASEURL_10000IMG = "http://10000img.com/";
    public static final String SITE_URL_10000IMG = "http://10000img.com/ran.php";
    public static final String SITE_BASEURL_MANPEACE = "http://manpeace.net/";
    public static final String SITE_URL_MANPEACE = "";
    public static final String SAVE_LOCATION = "C:/Users/yuninje/Desktop/INJE/Image/Parsing Image/Project - crawling_pra/";

    static Scanner scanner;
    static Crawling crawling;
    static File file;
    static File folder;

    Document doc;
    URL fileUrl;
    BufferedImage bi = null;
    BufferedInputStream bufferedInputStream;
    FileOutputStream fileOutputStream;

    Elements elements;
    Elements elementsVideo;
    Elements elementsImg;
    Elements elementsA;

    List<Content> contents =  new ArrayList();
    Content content = new Content();

    String siteBaseUrl = "";
    String siteUrl = "";
    String saveLocation = "";
    String imgString = "";
    String videoString = "";
    String videoFileName= "";
    String imgFileName= "";
    String postNum = "";

    int elementsVideoCount;
    int elementsImgCount;
    int elementsACount;
    int selectSite = 0;
    int amount = 0;
    int siteNum = 2;
    int num = 1;

    public Crawling() throws Exception{
        scanner = new Scanner(System.in);
        //crawling();
        siteBaseUrl = SITE_BASEURL_MANPEACE;
        siteUrl = SITE_URL_MANPEACE;
        crawlingManpeace();
    }

    void crawling() throws Exception{
        switch (siteNum = scanner.nextInt()) {
            case 1:// http://10000img.com/
                System.out.println("-------  10000img   선택  -------");
                siteBaseUrl = SITE_BASEURL_10000IMG;
                siteUrl = SITE_URL_10000IMG;
                setAmount();
                crawling10000img();
                break;
            case 2:// http://manpeace.net/
                System.out.println("-------  manpeace   선택  -------");
                siteBaseUrl = SITE_BASEURL_MANPEACE;
                siteUrl = SITE_URL_MANPEACE;
                setAmount();
                crawlingManpeace();
                break;
            default:
                System.out.println("잘못 입력");
        }
    }

    int selectSite() {
        System.out.println("1 - http://10000img.com/");
        System.out.println("2 - http://manpeace.net/");
        System.out.print("이미지 크롤링 할 사이트 선택 : ");
        return scanner.nextInt();
    }

    void setAmount() {
        System.out.print("이미지 크롤링 할 이미지 갯수 입력 : ");
        amount = scanner.nextInt();
    }

    String getFolderName(String url) {
        String folderName;
        if (url.substring(0, 8).equals("https://")) {
            folderName = url.substring(8, url.indexOf("."));
        } else if (url.substring(0, 7).equals("http://")) {
            folderName = url.substring(7, url.indexOf("."));
        } else {
            folderName = url.substring(0, url.indexOf("."));
        }

        System.out.println("Folder name : " + folderName);
        return folderName + "/";
    }

    public void crawling10000img() throws Exception{
            saveLocation = SAVE_LOCATION + getFolderName(siteBaseUrl);

            folder = new File(saveLocation);
            if (!folder.exists()) {
                folder.mkdir();
            }

            for (int i = 0; i < amount; i++) {
                downloadFile(saveLocation);
            }
    }

    void downloadFile(String location) throws Exception{
        String extension = "";
        folder = new File(location);
        if (!folder.exists()) {
            folder.mkdir();
        }

        if(siteNum == 2) {      // manpeace
            postNum = siteUrl.split("wr_id=")[1];
            folder = new File(location+postNum);
            if(!folder.exists()){
                folder.mkdir();
            }
        }
        for(Content c : contents){
            if (c.urlString.substring(0, 1).equals("/")) {
                c.urlString = siteBaseUrl+c.urlString ;
            }
            extension ="."+
                    c.urlString.substring(c.urlString.length()-6,c.urlString.length()).split("\\.")[1];


            fileUrl = new URL(c.urlString);

            if(c.type == 1) {
                downImage(c.urlString, location+postNum+"/", extension);
            }else {
                downVideo(c.urlString, location+postNum+"/",extension);
            }
            num ++;
        }

    }

    void downImage(String imgString, String location, String extension) throws Exception{

        if(imgString.substring(0,1).equals("/")){
            fileUrl = new URL(siteBaseUrl+imgString);
        }else{
            fileUrl = new URL(imgString);
        }

        bi = ImageIO.read(fileUrl);
        imgFileName = imgString.replace("/", "");

        file = new File(location + postNum + "-"+num + extension);


        ImageIO.write(bi, "jpg", file);
        System.out.println("파일 저장 위치 : "+location);
        System.out.println("파일 절대 주소 : "+location + imgFileName);
        System.out.println("Content Name : " + imgFileName + "      저장 완료\n");
    }

    void downVideo(String videoString, String location, String extension) throws Exception{
        videoFileName = videoString.replace("/", "");
        file = new File(location + postNum + "-"+num + extension);
        System.out.println(videoFileName);

        downloadUsingNIO(fileUrl, location);
    }

    private static void downloadUsingNIO(URL url, String file) throws Exception {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    private static void downloadUsingStream(URL url, String file) throws Exception{
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count=0;
        while((count = bis.read(buffer,0,1024)) != -1)
        {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    public void crawlingManpeace() throws Exception{
        saveLocation = SAVE_LOCATION + getFolderName(siteBaseUrl);

        siteUrl = "http://manpeace.net/bbs/board.php?bo_table=ssam&wr_id=16372";
        doc = Jsoup.connect(siteUrl).get();
        elements = doc.select("div").eq(36).select("div").eq(7);
        getDataUrl_Manpeace();
        downloadFile(saveLocation);
    }

    void getImgUrl_10000img() throws Exception {
        doc = Jsoup.connect(siteUrl).get();
        contents.add(new Content(doc.select("img").first().attr("src"), 1));
    }

    void getDataUrl_Manpeace() throws Exception{
        int i;
        elementsVideo = elements.select("file").select("source[src$=.mp4]");
        elementsImg = elements.select("img");
        elementsA = elements.select("a");

        elementsVideoCount = elementsVideo.indexOf(elementsVideo.last());
        elementsImgCount = elementsImg.indexOf(elementsImg.last());
        elementsACount = elementsA.indexOf(elementsA.last());

        System.out.println("file src ===========================================");
        i = 0;
        for (Element element : elementsVideo) {
            videoString = element.attr("src");
            fileUrl = new URL(videoString);
            //downVideo();
            System.out.println((i++) + " - " + videoString);
            contents.add(new Content(videoString, 2));
        }
        System.out.println();

        System.out.println("file src ===========================================");
        i = 0;
        for (Element element : elementsImg) {
            imgString = element.attr("src");
            System.out.println((i++) + " - " + imgString);
            contents.add(new Content(imgString, 1));
        }
        System.out.println();

        System.out.println("A href ===========================================");
        i = 0;
        for (Element element : elementsA) {
            imgString = element.attr("src");
            System.out.println((i++) + " - " + element.attr("href"));
        }
        System.out.println();
    }
}
