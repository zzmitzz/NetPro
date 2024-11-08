
package src;


public class RequestWrapper<T> {
    private final String type;
    private final T data;
    private final String route;
    private String message;
    
    public void setMessage(String chat){
        this.message = chat;
    }
    public String getMessage() {
        return message;
    }

    public RequestWrapper(String type, T data, String route){
        this.type = type;
        this.data = data;
        this.route = route;
    }
    
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

