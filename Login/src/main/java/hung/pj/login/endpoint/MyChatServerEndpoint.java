package hung.pj.login.endpoint;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat")
public class MyChatServerEndpoint {
    @OnOpen
    public void onOpen(Session session) {
        // Xử lý khi kết nối WebSocket được mở
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Xử lý khi nhận được tin nhắn từ client
    }

    @OnClose
    public void onClose(Session session) {
        // Xử lý khi kết nối WebSocket bị đóng
    }
}

