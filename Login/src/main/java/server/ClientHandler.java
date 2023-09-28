package server;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.message.IMessageDao;
import hung.pj.login.dao.message.MessageDaoImpl;
import hung.pj.login.model.MessageModel;

import java.io.*;
import java.net.*;


public class ClientHandler extends Thread {
    private Socket clientSocket;
    private IMessageDao messageDao;
    private ObjectOutputStream out;
    private Server server;


    public ClientHandler(Socket socket, ConnectionProvider connectionProvider, ObjectOutputStream out, Server server) {
        this.clientSocket = socket;
        this.messageDao = new MessageDaoImpl(connectionProvider.getConnection());
        this.out = out;
        this.server = server; // Khởi tạo tham chiếu đến đối tượng Server
    }


    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            // Đọc đối tượng tin nhắn từ máy khách
            MessageModel message = (MessageModel) in.readObject();
            System.out.println("Tin nhắn từ " + message.getSenderId() + ": " + message.getMessageText() + " gửi đến phòng " + message.getConversationId());

            // Xử lý tin nhắn (ví dụ: lưu vào cơ sở dữ liệu, gửi cho người nhận, v.v.)
            // Lưu tin nhắn vào cơ sở dữ liệu
            messageDao.sendMessage(message);

            // Gửi phản hồi cho máy khách người gửi (nếu cần)
            out.writeObject("Tin nhắn đã được nhận và xử lý thành công.");

            // Tự động cập nhật tin nhắn cho tất cả client trong phòng chat
            server.broadcastMessage(message); // Hàm broadcastMessage được định nghĩa trên Server

            // Đóng kết nối
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
