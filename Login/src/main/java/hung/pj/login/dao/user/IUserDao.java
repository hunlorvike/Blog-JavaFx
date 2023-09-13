package hung.pj.login.dao.user;

import hung.pj.login.model.UserModel;

import java.time.LocalDateTime;
import java.util.List;

public interface IUserDao {
    List<UserModel> getAllUsers();
    UserModel getUserByEmail(String email);
    List<UserModel> getUsersByPostCountDescending(int limit);

    void insertUser(UserModel userModel);
    void changeUserPassword(String email, String oldPassword, String newPassword);
    boolean authenticateUser(String email, String password);
    void updateUserProfile(UserModel userModel);
    void deleteUser(String email);
    List<UserModel> getUsersByRole(String role);
    void assignUserRole(String email, String role);
    void revokeUserRole(String email, String role);
    boolean isUserSuperAdmin(String email);
    void lockUserAccount(String email, LocalDateTime lockedUntil);
    void unlockUserAccount(String email);
    void resetLockedAccountRoles(String email, String role);
}


