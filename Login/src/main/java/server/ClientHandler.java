package server;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.message.IMessageDao;
import hung.pj.login.dao.message.MessageDaoImpl;
import hung.pj.login.model.MessageModel;

import java.io.*;
import java.net.*;


public class ClientHandler extends Thread {
    private Socket clientSocket; // Đây là socket mà máy khách đã kết nối với máy chủ.
    private IMessageDao messageDao; // Lớp xử lý truy vấn và lưu trữ tin nhắn vào cơ sở dữ liệu.
    private ObjectOutputStream out; // Lớp xử lý truy vấn và lưu trữ tin nhắn vào cơ sở dữ liệu.
    private Server server; // Tham chiếu đến đối tượng Server để có khả năng gửi tin nhắn đến tất cả các máy khách.


    public ClientHandler(Socket socket, ConnectionProvider connectionProvider, ObjectOutputStream out, Server server) {
        this.clientSocket = socket;
        this.messageDao = new MessageDaoImpl(connectionProvider.getConnection());
        this.out = out;
        this.server = server; // Khởi tạo tham chiếu đến đối tượng Server
    }

    // Phương thức run được gọi khi luồng ClientHandler bắt đầu
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); // Tạo một luồng đầu vào (ObjectInputStream) để đọc tin nhắn từ máy khách.

            // Đọc tin nhắn từ máy khách
            MessageModel message = (MessageModel) in.readObject();
            System.out.println("Tin nhắn từ " + message.getSenderId() + ": " + message.getMessageText() + " gửi đến phòng " + message.getConversationId());

            // Lưu tin nhắn vào cơ sở dữ liệu thông qua messageDao.
            messageDao.sendMessage(message);

            // Gửi phản hồi cho máy khách người gửi (nếu cần)
            out.writeObject("Tin nhắn đã được nhận và xử lý thành công.");

            // Kiểm tra trạng thái kết nối trước khi gửi tin nhắn
            if (!clientSocket.isClosed()) {
                // Gửi tin nhắn cho tất cả máy khách khác bằng cách gọi server.broadcastMessage(message).
                server.broadcastMessage(message);
            }
            // Đóng kết nối với máy khách.
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof SocketException && e.getMessage().contains("Connection reset by peer")) {
                System.out.println("Máy khách đã đóng kết nối một cách không mong muốn.");
            } else {
                e.printStackTrace();
            }
        }
    }
}
