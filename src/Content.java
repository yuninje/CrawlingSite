public class Content {
    public Content(String urlString, int type,String postNum) {
        this.urlString = urlString;
        this.type = type;
        this.postNum = postNum;
    }
    public Content(){
        this.urlString = "";
        this.type = 0;
    }

    String urlString;
    int type = 0;   // 1 : img , 2 : file
    String postNum;
}
