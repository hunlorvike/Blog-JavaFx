package hung.pj.login.ultis;

import javafx.scene.control.Control;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        // Regular expression for email validation
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    public static boolean isValidPassword(String password) {
        // Implement your password validation logic here
        return password.length() >= 8; // For example, password length must be at least 8 characters
    }

    public static void setRedBorder(Control control) {
        BorderStroke borderStroke = new BorderStroke(Color.valueOf("#BB2525"),
                BorderStrokeStyle.SOLID, null, BorderStroke.THIN);
        control.setBorder(new Border(borderStroke));
    }

    public static void removeBorder(Control control) {
        control.setBorder(null);
    }

    // Add more validation methods as needed

}

