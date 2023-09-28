package server;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.model.MessageModel;
import server.ClientHandler;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private List<ObjectOutputStream> clientOutputStreams = new CopyOnWriteArrayList<>(); // Danh sách ObjectOutputStream của các client

    public static void main(String[] args) {
        Server server = new Server();

        ExecutorService executorService = Executors.newFixedThreadPool(10); // Số lượng luồng trong thread pool

        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server đang lắng nghe trên cổng 8080...");

            // Tạo một đối tượng ConnectionProvider
            ConnectionProvider connectionProvider = new ConnectionProvider(/*các thông tin cấu hình kết nối*/);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                server.clientOutputStreams.add(clientOutputStream); // Thêm ObjectOutputStream của client vào danh sách

                Runnable clientHandler = new ClientHandler(clientSocket, connectionProvider, clientOutputStream, server);
                executorService.execute(clientHandler); // Sử dụng thread pool để xử lý client
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown(); // Đóng thread pool khi kết thúc
        }
    }

    // Phương thức để gửi tin nhắn đến tất cả các client
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
