package hung.pj.login.utils;

import hung.pj.login.model.LoginInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RememberMeManager {


    public static void saveLoginInfo(String email, String password) {
        // Đầu tiên, tạo một đối tượng Properties để lưu thông tin.
        Properties properties = new Properties();

        // Đọc các thông tin đăng nhập đã lưu (nếu có)
        try (InputStream input = new FileInputStream(Constants.CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Đặt thông tin đăng nhập vào thuộc tính của Properties với tên duy nhất (ví dụ: email1, password1, email2, password2,...)
        int accountCount = properties.size() / 2 + 1;
        properties.setProperty("email" + accountCount, email);
        properties.setProperty("password" + accountCount, password);

        try {
            // Lưu lại Properties vào tệp rememberme.properties
            FileOutputStream outputStream = new FileOutputStream(Constants.CONFIG_FILE);
            properties.store(outputStream, "Remember Me Information");
            outputStream.close();
            System.out.println("Thông tin đăng nhập đã được lưu vào rememberme.properties.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<LoginInfo> getSavedLoginInfoList() {
        List<LoginInfo> loginInfoList = new ArrayList<>();

        try (InputStream input = new FileInputStream(Constants.CONFIG_FILE)) {
            Properties properties = new Properties();
            properties.load(input);

            // Sử dụng một vòng lặp để lấy tất cả các cặp tài khoản và mật khẩu
            int accountCount = properties.size() / 2;
            for (int i = 1; i <= accountCount; i++) {
                String email = properties.getProperty("email" + i);
                String password = properties.getProperty("password" + i);
                loginInfoList.add(new LoginInfo(email, password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loginInfoList;
    }


}

