package hung.pj.login.controller;

import hung.pj.login.model.PostModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;

public class ItemPostController {
    @FXML
    private Label labelCategory, labelTitle, labelContent, labelCreator, labelDate;

    public void setListData(PostModel postModel) {
        labelCategory.setText(postModel.getCategory());
        labelTitle.setText(postModel.getTitle());
        labelContent.setText(postModel.getContent());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(postModel.getCreated_at());
        labelDate.setText(formattedDate);
        labelCreator.setText("" + postModel.getCreator_id());
    }
}
