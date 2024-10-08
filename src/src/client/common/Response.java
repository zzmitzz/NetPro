package src.client.common;

public interface Response {
    void onSuccess(String data);
    void onFailure();
}
