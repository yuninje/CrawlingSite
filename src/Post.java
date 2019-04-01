public class Post {
    String url = "";
    String date;
    String postNum;

    public Post(String url, String postNum) throws Exception {
        this.url = url;
        this.postNum = postNum;
    }
    public Post(String url) {
        this.url = url;
    }
    public Post() {
    }
}
