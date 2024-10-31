package src.client.common;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.client.ServiceLocator;
import src.client.data.ClientConnection;

public abstract class BaseClientController {

    public final ClientConnection clientConnection = ServiceLocator.initAPI();
    protected final Gson gson = new Gson();
    private volatile boolean isListening = false;
    public onAction callbackAction = null;
    public <T> void doJsonRequest(T object, String route) {
        try {
            clientConnection.doJsonRequest(object, route);
        } catch (IOException ex) {
            Logger.getLogger(BaseClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void onStartLiveUpdate(String controllerName) throws IOException {

        if (!clientConnection.isConnection()) {
            clientConnection.openConnection();
        }
        clientConnection.startListenning(new Response() {
            @Override
            public void onSuccess(String data) {
                if(callbackAction == null){
                    System.out.println("null");
                }
                if (callbackAction != null) {
                    try {
                        System.out.println("DEBUG" + callbackAction.hashCode());
                        callbackAction.onSuccess(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Thread.dumpStack();
                }
            }

            @Override
            public void onFailure() {
                if (callbackAction != null) {
                    callbackAction.onError("Error 404");
                }
            }
        }, controllerName);
    }
    // Add method to check callback status
    public boolean hasValidCallback() {
        return callbackAction != null;
    }

    public void onCloseLiveUpdate(String controllerName) {
        isListening = false;
        clientConnection.closeListening(controllerName);

    }
}
