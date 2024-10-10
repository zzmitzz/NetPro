package src;

public class ResponseWrapper {

    private String type;
    private String data;
    private String route;

    public ResponseWrapper(String type, String data, String route) {
        this.type = type;
        this.data = data;
        this.route = route;
    }

}
