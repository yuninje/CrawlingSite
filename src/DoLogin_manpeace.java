import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DoLogin_manpeace {
    public static final String SITE = "http://manpeace.net/bbs/login.php?url=http://manpeace.net/";
    Connection.Response loginPageResponse;
    Map<String, String> loginTryCookie;
    Document loginPageDocument;
    Document adminPageDocument;
    String stx;
    String url;
    String url2;
    String userAgent;
    Map<String, String> data;
    Connection.Response response;
    Map<String, String> loginCookie;

    public DoLogin_manpeace() throws IOException {
        getToken();
        getSessionID();
        getInformation();
    }

    void getToken() throws IOException {
        loginPageResponse = Jsoup.connect(SITE)
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
        url = loginPageDocument.select("input.url").val();
    }

    void getSessionID() throws IOException {
        userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

        //전송할 폼 데이터
        data = new HashMap<>();
        data.put("mb_id", "dbsdlswp11");
        data.put("mb_password", "1111");
        data.put("url", "http://manpeace.net/bbs/search.php");
        data.put("url", "http%3A%2F%2Fmanpeace.net%2F");
        data.put("stx", "");

        //로그인
        response = Jsoup.connect(SITE)
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


        // 로그인 성공 후 얻은 쿠키.
        // 쿠키 중 TSESSION 이라는 값을 확인할 수 있다.
        loginCookie = response.cookies();
    }

    void getInformation() throws IOException {
        adminPageDocument = Jsoup.connect(SITE)
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

        System.out.println(adminPageDocument);
    }
}
