package hung.pj.login.dao.user;

import hung.pj.login.exception.DatabaseException;
import hung.pj.login.exception.UnauthorizedAccessException;
import hung.pj.login.model.UserModel;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements IUserDao {
    private final Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<UserModel> getAllUsers() {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String fullname = resultSet.getString("fullname");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role");
                Timestamp created_at = resultSet.getTimestamp("created_at");
                Timestamp updated_at = resultSet.getTimestamp("updated_at");
                UserModel userModel = new UserModel(id, fullname, email, password, role, created_at, updated_at);
                users.add(userModel);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching users from the database.", e);
        }

        return users;
    }

    @Override
    public UserModel getUserByEmail(String inputEmail) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, inputEmail);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("user_id");
                    String fullname = resultSet.getString("fullname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    LocalDateTime lockedUntil = resultSet.getTimestamp("locked_until") != null ?
                            resultSet.getTimestamp("locked_until").toLocalDateTime() :
                            null;
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");

                    UserModel userModel = new UserModel(id, fullname, email, password, role, created_at, updated_at);
                    userModel.setLockedUntil(lockedUntil);
                    return userModel;

                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching user by email", e);
        }

        return null; // Return null if no user with the given ID is found
    }

    @Override
    public List<UserModel> getUsersByName(String name) {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE fullname LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + name + "%"); // Sử dụng '%' để tìm kiếm theo phần của tên người dùng

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("user_id");
                    String fullname = resultSet.getString("fullname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    UserModel userModel = new UserModel(id, fullname, email, password, role, created_at, updated_at);
                    users.add(userModel);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching users by name.", e);
        }

        return users;
    }


    @Override
    public List<UserModel> getUsersByPostCountDescending(int limit) {
        List<UserModel> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT u.fullname, COUNT(p.post_id) AS post_count " +
                        "FROM users u " +
                        "LEFT JOIN post p ON u.user_id = p.creator_id " +
                        "GROUP BY u.fullname " +
                        "ORDER BY post_count DESC " +
                        "LIMIT ?"
        )) {
            preparedStatement.setInt(1, limit);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String fullname = resultSet.getString("fullname");
                int postCount = resultSet.getInt("post_count");

                UserModel userModel = new UserModel(fullname, postCount);
                users.add(userModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public void insertUser(UserModel userModel) {
        String query = "INSERT INTO users (fullname, email, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userModel.getFullname());
            preparedStatement.setString(2, userModel.getEmail());
            preparedStatement.setString(3, userModel.getPassword());
            preparedStatement.setString(4, userModel.getRole());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while inserting user into the database.", e);
        }
    }

    @Override
    public void changeUserPassword(String email, String oldPass, String newPass) {
        // Lấy mật khẩu hiện tại từ cơ sở dữ liệu
        String query = "SELECT password FROM users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    // Kiểm tra xem mật khẩu cũ có khớp với mật khẩu hiện tại không
                    if (BCrypt.checkpw(oldPass, hashedPassword)) {
                        // Nếu khớp, tiến hành cập nhật mật khẩu mới
                        String newHashedPassword = BCrypt.hashpw(newPass, BCrypt.gensalt());
                        updatePassword(email, newHashedPassword);
                    } else {
                        // Nếu không khớp, ném ngoại lệ hoặc xử lý lỗi tùy ý
                        throw new DatabaseException("Mật khẩu hiện tại không đúng.");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thay đổi mật khẩu người dùng.", e);
        }
    }

    public void updatePassword(String email, String newHashedPassword) {
        String updateQuery = "UPDATE users SET password = ? WHERE email = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setString(1, newHashedPassword);
            updateStatement.setString(2, email);
            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected == 0) {
                // Xử lý khi thay đổi mật khẩu thất bại
                throw new DatabaseException("Lỗi khi thay đổi mật khẩu người dùng.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Lỗi khi thay đổi mật khẩu người dùng.", e);
        }
    }


    @Override
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while authenticating user.", e);
        }

        return false; // Return false if no user with the given email is found
    }

    @Override
    public void updateUserProfile(UserModel userModel) {
        String query = "UPDATE users SET fullname = ?, role = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userModel.getFullname());
            preparedStatement.setString(2, userModel.getRole());
            preparedStatement.setString(3, userModel.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while updating user information.", e);
        }
    }

    @Override
    public void deleteUser(String email) {
        String query = "DELETE FROM users WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while deleting user.", e);
        }
    }

    @Override
    public List<UserModel> getUsersByRole(String roleInput) {
        List<UserModel> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, roleInput);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Integer id = resultSet.getInt("user_id");
                    String fullname = resultSet.getString("fullname");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");
                    Timestamp created_at = resultSet.getTimestamp("created_at");
                    Timestamp updated_at = resultSet.getTimestamp("updated_at");
                    UserModel userModel = new UserModel(id, fullname, email, password, role, created_at, updated_at);
                    users.add(userModel);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error while fetching users by role.", e);
        }

        return users;
    }


    @Override
    public void assignUserRole(String email, String role) {
        String query = "UPDATE users SET role = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while assigning user role.", e);
        }
    }


    @Override
    public void revokeUserRole(String email, String role) {
        String query = "UPDATE users SET role = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "User"); // Revoke role and set to default role
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while revoking user role.", e);
        }

    }

    @Override
    public boolean isUserSuperAdmin(String email) {
        String query = "SELECT role FROM users WHERE email = ? LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    return "Super Admin".equals(role); // Thay "Super Admin" bằng tên vai trò Super Admin của bạn
                }
            }

            return false; // Trả về false nếu không tìm thấy người dùng
        } catch (SQLException e) {
            throw new DatabaseException("Error while checking user's role.", e);
        }
    }

    @Override
    public void lockUserAccount(String email, LocalDateTime lockedUntil) {
        String query = "UPDATE users SET locked_until = ? WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, lockedUntil);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while locking user account.", e);
        }
    }

    @Override
    public void unlockUserAccount(String email) {
        String query = "UPDATE users SET locked_until = NULL WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while unlocking user account.", e);
        }
    }

    public void resetLockedAccountRoles(String email, String role) {
        String query = "UPDATE users SET role = ? WHERE email = ? AND locked_until IS NOT NULL AND locked_until <= NOW()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, role); // Đặt lại vai trò thành "User" sau khi hết thời gian khoá
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error while resetting locked account roles.", e);
        }
    }

    @Override
    public void resetPassword(String email, String password) {
        String newHashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        updatePassword(email, newHashedPassword);
    }

    @Override
    public boolean isFollowing(int userId, int followerUserId) {
        String query = "SELECT COUNT(*) FROM user_followers WHERE user_id = ? AND follower_user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, followerUserId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean followUser(int userId, int followerUserId) {
        String query = "INSERT INTO user_followers (user_id, follower_user_id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, followerUserId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean unfollowUser(int userId, int followerUserId) {
        String query = "DELETE FROM user_followers WHERE user_id = ? AND follower_user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, followerUserId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Integer> getFollowers(int userId) {
        List<Integer> followers = new ArrayList<>();
        String query = "SELECT follower_user_id FROM user_followers WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    followers.add(resultSet.getInt("follower_user_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }

    @Override
    public List<Integer> getFollowing(int userId) {
        List<Integer> following = new ArrayList<>();
        String query = "SELECT user_id FROM user_followers WHERE follower_user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    following.add(resultSet.getInt("user_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return following;
    }

}
