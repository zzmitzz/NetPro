package src;

public class ResponseWrapper {

    public String type;
    public String data;
    public String route;

    public ResponseWrapper(String type, String data, String route) {
        this.type = type;
        this.data = data;
        this.route = route;
    }

}
