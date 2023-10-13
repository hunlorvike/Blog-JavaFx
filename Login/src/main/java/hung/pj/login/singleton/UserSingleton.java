package hung.pj.login.singleton;

import hung.pj.login.model.UserModel;

public class UserSingleton {
    private static UserSingleton instance;
    private UserModel loggedInUser;
    private
    boolean onlineStatus; // Thêm trường trạng thái online/offline

    private UserSingleton() {
        onlineStatus = false; // Ban đầu, người dùng offline
    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public void setLoggedInUser(UserModel user) {
        this.loggedInUser = user;
    }

    public UserModel getLoggedInUser() {
        return this.loggedInUser;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public boolean isOnline() {
        return onlineStatus;
    }

    public void clearSingleton() {
        this.loggedInUser = null;
        this.onlineStatus = false; // Đảm bảo rằng người dùng được đánh dấu là offline khi đăng xuất
    }
}
