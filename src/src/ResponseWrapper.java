package src;

public class  ResponseWrapper {

    public String status="";
    public String data="";
    public String route="";

    public ResponseWrapper(String status, String data, String route) {
        this.status = status;
        this.data = data;
        this.route = route;
    }

}
