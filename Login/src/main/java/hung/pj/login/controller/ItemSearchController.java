package hung.pj.login.controller;

import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;

public class ItemSearchController {
    @FXML
    private Label labelTopBox1, labelBotBox1, labelBox2, labelBox3;

    public <T> void setListData(T model) {
        if (model instanceof UserModel) {
            UserModel userModel = (UserModel) model;
            labelTopBox1.setText(userModel.getFullname());
            labelBotBox1.setText(userModel.getRole());
            labelBox2.setText(userModel.getEmail());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(userModel.getCreated_at());
            labelBox3.setText(formattedDate);
        } else if (model instanceof PostModel) {
            PostModel postModel = (PostModel) model;
            labelTopBox1.setText(postModel.getTitle());
            labelBotBox1.setText(postModel.getCategory());
            labelBox2.setText("" + postModel.getView_count());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(postModel.getCreated_at());
            labelBox3.setText(formattedDate);
        }
    }


}
