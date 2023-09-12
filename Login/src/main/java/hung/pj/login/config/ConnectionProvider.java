package hung.pj.login.config;

import hung.pj.login.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider implements IConnectionProvider, AutoCloseable {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final String DATABASE_NAME = "management";

    private Connection connection;

    public ConnectionProvider() {
        try {
            connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, DB_USER, DB_PASSWORD);
            System.out.println("Kết nối cơ sở dữ liệu thành công");
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thiết lập kết nối cơ sở dữ liệu", e);
        }
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

