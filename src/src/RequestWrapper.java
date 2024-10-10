
package src;


public class RequestWrapper<T> {
    private String type;
    private T data;
    private String route;
    
    public String getType() {
        return type;
    }

    public T getData() {
        return data;
    }
    
    public String getRoute(){
        return route;
    }
}

