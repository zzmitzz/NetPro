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

    /**
     * Sends a JSON request to the specified route.
     *
     * @param <T> the type of the object to be sent in the request
     * @param object the object to be sent in the request
     * @param route the route to which the request is sent
     */
    public <T> void doJsonRequest(T object, String route) {
        try {
            System.out.println("--> Send request: " + route);
            clientConnection.doJsonRequest(object, route);
        } catch (IOException ex) {
            Logger.getLogger(BaseClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initiates a live update process for the specified controller.
     * 
     * This method checks if the client connection is active. If not, it opens a new connection.
     * It then starts listening for responses from the server.
     * 
     * @param controllerName the name of the controller to start live updates for.
     * @throws IOException if an I/O error occurs when opening the connection.
     */
    public void onStartLiveUpdate(String controllerName) throws IOException {

        if (!clientConnection.isConnection()) {
            clientConnection.openConnection();
        }

        clientConnection.startListenning(new Response() {
            @Override
            public void onSuccess(String data) {
                if (callbackAction == null) {
                    System.out.println("null");
                }

                if (callbackAction != null) {
                    try {
                        System.out.println("DEBUG " + callbackAction.hashCode());
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
    
    /**
     * Checks if there is a valid callback action.
     *
     * @return true if the callback action is not null, false otherwise.
     */
    public boolean hasValidCallback() {
        return callbackAction != null;
    }

    /**
     * Handles the closure of live updates for a given controller.
     * 
     * This method stops the listening process and closes the connection
     * for live updates associated with the specified controller name.
     *
     * @param controllerName the name of the controller for which live updates should be closed
     */
    public void onCloseLiveUpdate(String controllerName) {
        isListening = false;
        clientConnection.closeListening(controllerName);
    }
}
