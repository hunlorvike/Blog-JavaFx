package hung.pj.login.dao.notifications;

import hung.pj.login.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationsImpl implements  INotifications{
    private Connection connection;

    public NotificationsImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public List<Notification> getAll() {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notifications";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.notificationId = resultSet.getInt("notification_id");
                notification.userid = resultSet.getInt("user_id");
                notification.content = resultSet.getString("content");
                notification.is_read = resultSet.getBoolean("is_read");
                notification.created_at = resultSet.getTimestamp("created_at");

                notifications.add(notification);
            }
        } catch (SQLException  e) { 
            e.printStackTrace();
        }

        return notifications;
    }

    @Override
    public Notification getEntityByID(int id) {
        String query = "SELECT * FROM notifications WHERE notification_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.notificationId = resultSet.getInt("notification_id");
                    notification.userid = resultSet.getInt("user_id");
                    notification.content = resultSet.getString("content");
                    notification.is_read = resultSet.getBoolean("is_read");
                    notification.created_at = resultSet.getTimestamp("created_at");
                    return notification;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy thông báo có id tương ứng
    }

    @Override
    public void add(Notification notification) {
        String query = "INSERT INTO notifications (user_id, content, is_read, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.userid);
            statement.setString(2, notification.content);
            statement.setBoolean(3, notification.is_read);
            statement.setTimestamp(4, notification.created_at);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Notification notification) {
        String query = "UPDATE notifications SET user_id = ?, content = ?, is_read = ?, created_at = ? WHERE notification_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.userid);
            statement.setString(2, notification.content);
            statement.setBoolean(3, notification.is_read);
            statement.setTimestamp(4, notification.created_at);
            statement.setInt(5, notification.notificationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM notifications WHERE notification_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String GetNameOfUser(int userid) {
        String query = "SELECT fullname FROM users where user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
             statement.setInt(1, userid);
             ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
               return resultSet.getString("fullname");
            }
        }catch (SQLException  e) {
            e.printStackTrace();
        }
        return null;
    }


}
