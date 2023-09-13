package hung.pj.login.dao.Notifications;

import hung.pj.login.model.Notification;

import java.util.List;

public interface INotifications {
     List<Notification> getAll();
     Notification getEntityByID(int id);
     void add(Notification notification);
     void update(Notification notification);
     void delete(int id);
     String GetNameOfUser(int userid);
}
