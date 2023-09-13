package hung.pj.login.controller;

import hung.pj.login.model.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.text.SimpleDateFormat;

public class ItemSearchController {
    @FXML
    private Label labelTopBox1, labelBotBox1, labelBox2, labelBox3;

    public void setListData(UserModel userModel) {
        labelTopBox1.setText(userModel.getFullname());
        labelBotBox1.setText(userModel.getRole());
        labelBox2.setText(userModel.getEmail());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(userModel.getCreated_at());
        labelBox3.setText(formattedDate);
    }

}
