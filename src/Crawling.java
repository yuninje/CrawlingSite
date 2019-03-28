import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class Crawling {
    public static final String SITE_BASEURL_10000IMG = "http://10000img.com/";
    public static final String SITE_URL_10000IMG = "http://10000img.com/ran.php";
    public static final String SITE_BASEURL_MANPEACE = "http://manpeace.net/";
    public static final String SITE_URL_MANPEACE = "";
    public static final String SAVE_LOCATION = "C:/Users/yuninje/Desktop/INJE/Image/Parsing Image/Project - crawling_pra/";
    
    static Scanner scanner;
    static Crawling crawling;
    static File image;
    static File folder;

    Document doc;
    URL imgUrl;
    BufferedImage bi = null;

    String siteBaseUrl="";
    String siteUrl="";
    String saveLocation = "";
    String saveFolder = "";
    String html = "";
    String imgName = "";

    int amount = 0;

    public Crawling() {
        scanner  =new Scanner(System.in);
        crawling();
        //crawlingManpeace(setAmount());
    }

    void crawling(){
        switch (selectSite()) {
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

    int selectSite(){
        System.out.println("1 - http://10000img.com/");
        System.out.println("2 - http://manpeace.net/");
        System.out.print("이미지 크롤링 할 사이트 선택 : ");
        return scanner.nextInt();
    }

    void setAmount() {
        System.out.print("이미지 크롤링 할 이미지 갯수 입력 : ");
        amount = scanner.nextInt();
    }

    String getFolderName(String url){
        String folderName;
        int idx = url.indexOf("://");
        folderName = url.substring(idx+3, url.indexOf("."));
        return folderName + "/";
    }

    public void crawling10000img() {
        try {
            saveLocation = SAVE_LOCATION + getFolderName(siteBaseUrl);

            folder = new File(saveLocation);
            if(!folder.exists()){
                folder.mkdir();
            }

            for (int i = 0; i < amount; i++) {
                doc = Jsoup.connect(siteUrl).get();
                imgName = doc.select("img").first().attr("src");
                imgUrl = new URL(siteBaseUrl + imgName);
                System.out.println("imgUrl : " + siteBaseUrl + imgName);

                bi = ImageIO.read(imgUrl);
                imgName = imgName.replace("/", "");



                image = new File(saveLocation + imgName);
                ImageIO.write(bi, "jpg", image);
                System.out.println("File Name : " + imgName + "      저장 완료\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void crawlingManpeace() {
        try {



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
