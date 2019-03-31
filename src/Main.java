import java.io.File;
import java.util.Scanner;

public class Main {
    public static final String IMG_PAGE = "http://10000img.com/";
    public static final String IMG_PAGE_URL = "http://10000img.com/ran.php";
    public static final String SAVE_FOLDER = "C:/Users/yuninje/Desktop/INJE/Image/Parsing Image/Project - crawling_pra/";

    static Scanner scanner;
    static Crawling crawling;
    static File outputFile;
    public static void main(String args[]) throws Exception{

        //new DoLogin_tistory();
        new DoLogin_manpeace();
        //new Crawling();
        //new Window();
    }
}
