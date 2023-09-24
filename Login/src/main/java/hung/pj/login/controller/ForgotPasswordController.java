package hung.pj.login.controller;

import hung.pj.login.AppMain;
import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.user.UserDaoImpl;
import hung.pj.login.model.UserModel;
import hung.pj.login.singleton.DataHolder;
import hung.pj.login.utils.Constants;
import hung.pj.login.utils.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import hung.pj.login.utils.EmailUtils;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class ForgotPasswordController implements Initializable {
    @FXML
    public AnchorPane rootAnchorPane;
    @FXML
    private TextField emailTextField, otpTextField;
    @FXML
    private Button sendButton, buttonSubmit;
    @FXML
    private Label alertMessage;
    private String generatedOTP; // Biến để lưu mã OTP đã tạo
    private long otpExpirationTimeMillis; // Thời gian hết hạn của mã OTP
    ConnectionProvider connectionProvider = new ConnectionProvider();
    UserDaoImpl userDao = new UserDaoImpl(connectionProvider.getConnection());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleSendButtonAction() {
        // Lấy địa chỉ email từ emailTextField
        String toEmail = emailTextField.getText().trim();

        // Tạo mã OTP (ở đây, tạo mã ngẫu nhiên, bạn có thể thay bằng logic tạo mã OTP thực tế)
        generatedOTP = generateOTP();

        // Lưu thời gian tạo mã OTP và thời gian hết hạn (2 phút sau) vào biến
        otpExpirationTimeMillis = System.currentTimeMillis() + (2 * 60 * 1000); // 2 phút

        // Tiến hành gửi email với mã OTP
        boolean emailSent = sendOTPByEmail(toEmail, generatedOTP);

        if (emailSent) {
            // Nếu email gửi thành công, hiển thị thông báo cho người dùng
            alertMessage.setText("OTP gửi thành công!");
        } else {
            // Nếu gửi email thất bại, hiển thị thông báo lỗi
            alertMessage.setText("Lỗi khi gửi OTP. Vui lòng thử lại.");
        }
    }

    public void handleSubmitButtonAction() throws IOException {
        // Lấy mã OTP mà người dùng đã nhập
        String enteredOTP = otpTextField.getText().trim();

        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();

        // Kiểm tra xem thời gian hiện tại có còn trong khoảng 2 phút sau khi mã OTP được tạo hay không
        if (currentTimeMillis <= otpExpirationTimeMillis && enteredOTP.equals(generatedOTP)) {
            // Xác thực thành công, bạn có thể thực hiện hành động cần thiết ở đây
            UserModel userModel = userDao.getUserByEmail(emailTextField.getText());

            if (userModel != null) {
                DataHolder.getInstance().setData(emailTextField.getText());
                AppMain.setRoot("forgot_password2.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
            } else {
                ControllerUtils.showAlertDialog("Email của bạn không tồn tại trong hệ thống", Alert.AlertType.ERROR, rootAnchorPane.getScene().getWindow());
            }

        } else {
            // Xác thực thất bại hoặc mã OTP đã hết hạn, hiển thị thông báo lỗi
            alertMessage.setText("OTP không hợp lệ hoặc đã hết hạn. Vui lòng thử lại.");
        }
    }

    // Phương thức để gửi OTP qua email
    private boolean sendOTPByEmail(String toEmail, String otp) {
        try {
            // Gọi phương thức sendEmail từ EmailUtil (hoặc class bạn đã tạo)
            EmailUtils.sendEmail(toEmail, "Xác minh danh tính của bạn", "Mã OTP của bạn là: " + otp + ". Mã xác thực này chỉ có hiệu lực trong vòng 2 phút. Vui lòng không chia sẻ mã OTP này với người khác để tránh bị lừa đảo!");
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
        AppMain.setRoot("login.fxml", Constants.DEFAULT_WIDTH, Constants.DEFAULT_HEIGHT, false);
    }
}
