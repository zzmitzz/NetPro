package src.client.data;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import src.RequestWrapper;
import src.client.common.Response;
import src.client.data.dto.User;
import src.server.Constant;

public class ClientConnection {

    private Socket mySocket;
    private final String serverHost = Constant.privateServerNetworkIP;
    private final int serverPort = Constant.portServer;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Map<String, Future<?>> taskMap = new HashMap<>();
    private volatile boolean isListening = true; // Control flag for the while loop

    public Socket openConnection() {
        try {
            mySocket = new Socket(serverHost, serverPort);
            in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            out = new PrintWriter(mySocket.getOutputStream(), true);
            System.out.println(mySocket);
        } catch (IOException ex) {
            System.out.println(ex + getClass().toString());
            return null;
        }
        return mySocket;
    }

    public boolean isConnection() {
        return mySocket != null && mySocket.isConnected();
    }

    public <T, R> void sendData(T object, String route) {
        if (in == null || out == null) {
            return;
        }
        try {
            Gson gson = new Gson();
            RequestWrapper<T> requestWrapper = new RequestWrapper("", object, route);
            String jsonRequest = gson.toJson(requestWrapper);
            out.println(jsonRequest);
            out.flush();
        } catch (Exception ex) {
            return;
        }
        return;
    }

    public void startListenning(Response callback, String controllerName) {
        Runnable listeningTask = () -> {
            while (isListening && !Thread.currentThread().isInterrupted() ) {
                String dataRes = null;
                System.out.println(Thread.currentThread().getName() + " listening");

                try {
                    dataRes = in.readLine();
                    System.out.println("Data read on: " + Thread.currentThread().getName());
                    if (dataRes == null) continue;
                    System.out.println("<-- 200 OK: Receive " + dataRes);
                } catch (IOException ex) {
                    Logger.getLogger(ClientConnection.class.getName()).log(Level.SEVERE, null, ex);
                }

                System.out.println(System.currentTimeMillis());
                if (dataRes != null && !dataRes.isEmpty()) {
                    callback.onSuccess(dataRes);
                }
            }
        };
        Future<?> future = executorService.submit(listeningTask);
        System.out.println("Task " + controllerName);
        taskMap.put(controllerName, future); // Map the task with its name
    }

    public void closeListening(String controllerName) {
        try {

            System.out.println("Task " + controllerName);
            Future<?> task = taskMap.get(controllerName);
            if (task != null) {
//                isListening = false;
                task.cancel(true);
                System.out.println(controllerName + " canceled");
                taskMap.remove(controllerName); // Remove from map
            }
            System.out.println(Thread.currentThread().getName() + ". Current listening has been stopped.");
        } catch (Exception e) {
        }
    }

    // Send object and return json string
    public <T> void doJsonRequest(T object, String route) throws IOException {
        sendData(object, route);
    }

    public void closeConnection() {
        try {
            mySocket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
