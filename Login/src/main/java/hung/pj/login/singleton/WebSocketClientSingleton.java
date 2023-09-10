package hung.pj.login.singleton;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class WebSocketClientSingleton {
    private static WebSocketClientSingleton instance;
    private Session session;

    private WebSocketClientSingleton() {
        CountDownLatch latch = new CountDownLatch(1);

        ClientManager client = ClientManager.createClient();
        client.getProperties().put(ClientProperties.RETRY_AFTER_SERVICE_UNAVAILABLE, true);

        try {
            URI serverUri = new URI("ws://localhost:8080/websocket");
            session = client.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    latch.countDown();
                }
            }, serverUri);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized WebSocketClientSingleton getInstance() {
        if (instance == null) {
            instance = new WebSocketClientSingleton();
        }
        return instance;
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

