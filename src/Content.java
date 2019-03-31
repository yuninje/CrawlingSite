public class Content {
    public Content(String urlString, int type) {
        this.urlString = urlString;
        this.type = type;
    }
    public Content(){
        this.urlString = "";
        this.type = 0;
    }

    String urlString;
    int type = 0;   // 1 : img , 2 : file
}
