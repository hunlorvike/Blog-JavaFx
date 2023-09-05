package hung.pj.login.controller;

import hung.pj.login.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import hung.pj.login.ultis.EmailUtil;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    private TextField emailTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Label alertMessage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
    }

    public void handleSendButtonAction(ActionEvent actionEvent) {
        // Lấy địa chỉ email từ emailTextField
        String toEmail = emailTextField.getText().trim();

        // Tạo mã OTP (ở đây, tạo mã ngẫu nhiên, bạn có thể thay bằng logic tạo mã OTP thực tế)
        String otp = generateOTP();

        // Tiến hành gửi email với mã OTP
        boolean emailSent = sendOTPByEmail(toEmail, otp);

        if (emailSent) {
            // Nếu email gửi thành công, hiển thị thông báo cho người dùng
            alertMessage.setText("OTP sent successfully!");
        } else {
            // Nếu gửi email thất bại, hiển thị thông báo lỗi
            alertMessage.setText("Error sending OTP. Please try again.");
        }
    }
    // Phương thức để gửi OTP qua email
    private boolean sendOTPByEmail(String toEmail, String otp) {
        try {
            // Gọi phương thức sendEmail từ EmailUtil (hoặc class bạn đã tạo)
            EmailUtil.sendEmail(toEmail, "OTP Verification", "Your OTP is: " + otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức tạo mã OTP ngẫu nhiên (đoạn này bạn có thể thay bằng logic tạo mã OTP thực tế)
    private String generateOTP() {
        // Độ dài của OTP (số chữ số)
        int otpLength = 6;

        // Dãy ký tự cho OTP (chỉ sử dụng chữ số từ 0 đến 9)
        String digits = "0123456789";

        // Sử dụng StringBuilder để xây dựng OTP
        StringBuilder otp = new StringBuilder();

        // Sử dụng đối tượng Random để lựa chọn ngẫu nhiên các chữ số
        Random random = new Random();

        // Tạo OTP bằng cách lựa chọn ngẫu nhiên các chữ số từ dãy ký tự digits
        for (int i = 0; i < otpLength; i++) {
            int index = random.nextInt(digits.length());
            char digit = digits.charAt(index);
            otp.append(digit);
        }

        return otp.toString();
    }

    public void handleClickLogin(MouseEvent mouseEvent) throws IOException {
        AppMain.setRoot("login.fxml", 1024, 600, false);
    }


}
