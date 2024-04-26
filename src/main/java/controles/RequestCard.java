package controles;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import models.user;
import models.carriere;

import java.io.IOException;
import java.net.URL;

public class RequestCard extends Node {

    @FXML
    private Label UserEmail;

    @FXML
    private Label userCV;

    @FXML
    private ImageView userIMG;

    @FXML
    private Button AcceptButton;

    @FXML
    private Button DeclineButton;


    public void setData(user User, carriere Carriere) throws IOException {
       UserEmail.setText(User.getEmail());
    userCV.setText(Carriere.getCv().getAbsolutePath());
        String pictureURL = User.getPicture();
        URL imageUrl = new URL(pictureURL);

        int imageSize = 200;

        Image image = new Image(imageUrl.openStream(), imageSize, imageSize, true, true);


        Circle clip = new Circle(imageSize / 2.0, imageSize / 2.0, imageSize / 2.0);

        userIMG.setClip(clip);

        userIMG.setImage(image);


    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
