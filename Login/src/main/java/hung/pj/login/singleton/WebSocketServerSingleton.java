package hung.pj.login.singleton;

import org.glassfish.tyrus.server.Server;
import hung.pj.login.endpoint.MyChatServerEndpoint; // Thay đổi đường dẫn package tương ứng

public class WebSocketServerSingleton {
    private static WebSocketServerSingleton instance;
    private Server server;

    private WebSocketServerSingleton() {
        server = new Server("localhost", 8080, "/websocket", null, MyChatServerEndpoint.class);
    }

    public static synchronized WebSocketServerSingleton getInstance() {
        if (instance == null) {
            instance = new WebSocketServerSingleton();
        }
        return instance;
    }

    public void startServer() {
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        server.stop();
    }
}
