package hung.pj.login.controller;

import hung.pj.login.config.ConnectionProvider;
import hung.pj.login.dao.post.IPostDao;
import hung.pj.login.dao.post.PostDaoImpl;
import hung.pj.login.model.PostModel;
import hung.pj.login.model.UserModel;
import hung.pj.login.utils.ControllerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;

public class ItemSearchController {
    @FXML
    private Label labelTopBox1, labelBotBox1, labelBox2, labelBox3;
    ConnectionProvider connectionProvider = new ConnectionProvider();
    IPostDao postDao = new PostDaoImpl(connectionProvider.getConnection());


    public <T> void setListData(T model) {
        if (model instanceof UserModel) {
            UserModel userModel = (UserModel) model;
            labelTopBox1.setText(ControllerUtils.toTitleCase(userModel.getFullname()));
            labelBotBox1.setText(userModel.getRole());
            labelBox2.setText(userModel.getEmail());

            labelBox3.setText(ControllerUtils.formatDateTime(userModel.getCreated_at()));
        } else if (model instanceof PostModel) {
            PostModel postModel = (PostModel) model;
            UserModel user = postDao.getCreator(postModel.getPost_id());
            labelTopBox1.setText(ControllerUtils.toTitleCase(postModel.getTitle()));
            labelBotBox1.setText(postModel.getCategory());
            labelBox2.setText(ControllerUtils.toAuthorName(user.getFullname()));

            labelBox3.setText(ControllerUtils.formatDateTime(postModel.getScheduledDate()));

        }
    }


}
