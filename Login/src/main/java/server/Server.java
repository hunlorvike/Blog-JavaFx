package server;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.model.MessageModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final List<ObjectOutputStream> clientOutputStreams = new CopyOnWriteArrayList<>();  // Khởi tạo một danh sách có kiểu dữ liệu là ObjectOutputStream chứa các object được gửi từ phía client

    public static void main(String[] args) {
        Server server = new Server(); // Tạo một object có tên server từ class Server để sử dụng danh sách trên

        ExecutorService executorService = Executors.newFixedThreadPool(10); // Tạo một thread pool với 10 luồng sử dụng ExecutorService. Thread pool này được sử dụng để xử lý các kết nối từ các máy khách.

        try {
            ServerSocket serverSocket = new ServerSocket(8080); // Máy chủ tạo một ServerSocket trên cổng 8080 để lắng nghe các kết nối từ máy khách.
            System.out.println("Server đang lắng nghe trên cổng 8080...");

            // Tạo một đối tượng ConnectionProvider
            ConnectionProvider connectionProvider = new ConnectionProvider(/*các thông tin cấu hình kết nối*/);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream()); // Máy chủ tạo một ObjectOutputStream để gửi dữ liệu đến máy khách
                server.clientOutputStreams.add(clientOutputStream); // Thêm ObjectOutputStream của client vào danh sách clientOutputStreams

                Runnable clientHandler = new ClientHandler(clientSocket, connectionProvider, clientOutputStream, server); // Tạo một luồng ClientHandler để xử lý máy khách vừa kết nối và đưa nó vào thread pool để xử lý.
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Đóng thread pool khi kết thúc
        }
    }

    // Phương thức này được gọi để gửi một tin nhắn (MessageModel) đến tất cả các máy khách đã kết nối.
    public void broadcastMessage(MessageModel message) {
        for (ObjectOutputStream clientOutputStream : clientOutputStreams) {
            try {
                clientOutputStream.writeObject(message);
                clientOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
