package hung.pj.login.singleton;

import hung.pj.login.model.UserModel;

public class UserSingleton {
    private static UserSingleton instance;
    private UserModel loggedInUser;

    private UserSingleton() {

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

    public void clearSingleton() {
        this.loggedInUser = null;
    }
}
