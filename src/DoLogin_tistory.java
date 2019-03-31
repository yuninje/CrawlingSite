import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DoLogin_tistory {
    Connection.Response loginPageResponse;
    Map<String, String> loginTryCookie;
    Document loginPageDocument;
    Document adminPageDocument;
    String fp;
    String userAgent;
    Map<String, String> data;
    Connection.Response response;
    Map<String, String> loginCookie;
    Elements blogOptions;
    public DoLogin_tistory() throws IOException {
        // 로그인 페이지 접속
        getToken();
        getSessionID();
        getInformation();
    }

    void getToken()throws IOException{
        loginPageResponse = Jsoup.connect("https://tistory.com/auth/login/")
                .timeout(3000)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Content-Length", "136")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", "_ga=GA1.2.185161425.1553533578; _gid=GA1.2.502745599.1553687537; kakao_server_id=5f534a385e")
                .header("Host", "www.tistory.com")
                .header("Origin", "https://www.tistory.com")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .method(Connection.Method.GET)
                .execute();

// 로그인 페이지에서 얻은 쿠키
        loginTryCookie = loginPageResponse.cookies();

// 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
        loginPageDocument = loginPageResponse.parse();

        fp = loginPageDocument.select("input.fp").val();
    }

    void getSessionID()throws IOException{
        userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36";

// 전송할 폼 데이터
        data = new HashMap<>();
        data.put("loginId", "dbsdlswp@naver.com");
        data.put("password", "dbs1335479!");
        data.put("redirectUrl", "https://www.tistory.com/");
        data.put("fp", "311dd3c82507829803f3060062799276"); // 로그인 페이지에서 얻은 토큰들

// 로그인(POST)
        response = Jsoup.connect("https://tistory.com/auth/login/")
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Content-Length", "136")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", "_ga=GA1.2.185161425.1553533578; _gid=GA1.2.502745599.1553687537; kakao_server_id=5f534a385e")
                .header("Host", "www.tistory.com")
                .header("Origin", "https://www.tistory.com")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .cookies(loginTryCookie)
                .data(data)
                .method(Connection.Method.POST)
                .execute();

// 로그인 성공 후 얻은 쿠키.
// 쿠키 중 TSESSION 이라는 값을 확인할 수 있다.
        loginCookie = response.cookies();
    }

    void getInformation()throws IOException{
        adminPageDocument = Jsoup.connect("http://muifim.tistory.com/manage")
                .userAgent(userAgent)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Cache-Control", "max-age=0")
                .header("Connection", "keep-alive")
                .header("Content-Length", "136")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Cookie", "_ga=GA1.2.185161425.1553533578; _gid=GA1.2.502745599.1553687537; kakao_server_id=5f534a385e")
                .header("Host", "www.tistory.com")
                .header("Origin", "https://www.tistory.com")
                .header("Referer", "https://www.tistory.com/auth/login?redirectUrl=https%3A%2F%2Fmuifim.tistory.com%2F1")
                .header("Upgrade-Insecure-Requests", "1")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
                .cookies(loginCookie) // 위에서 얻은 '로그인 된' 쿠키
                .get();

        System.out.println(adminPageDocument);
    }
}
