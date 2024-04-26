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
import java.net.MalformedURLException;
import java.net.URL;

public class UserCard extends Node {


    @FXML
    private Button DeclineButton;

    @FXML
    private Label UserEmail;

    @FXML
    private Label UserName;

    @FXML
    private Label UserScore;

    @FXML
    private ImageView userIMG;

    @FXML
    private Label userTitle;
int score;
    public void setData(user User, carriere Carriere) throws IOException {
        UserEmail.setText(User.getEmail());
        UserName.setText(User.getUsername());
        if (Carriere == null) {
            UserScore.setText("Unavailable");
            userTitle.setText("Unavailable");
        } else {
            String scoreString = String.valueOf(Carriere.getScore());
            UserScore.setText(scoreString);
            userTitle.setText(Carriere.getTitle());
        }

        // Check if pictureURL is not null
        String pictureURL = User.getPicture();
        if (pictureURL != null) {
            try {
                URL imageUrl = new URL(pictureURL);
                int imageSize = 100;
                Image image = new Image(imageUrl.openStream(), imageSize, imageSize, true, true);
                Circle clip = new Circle(imageSize / 2.0, imageSize / 2.0, imageSize / 2.0);
                userIMG.setClip(clip);
                userIMG.setImage(image);
            } catch (MalformedURLException e) {
                // Handle MalformedURLException
                e.printStackTrace();
            }
        } else {
            // Handle case when pictureURL is null
            // Set a default image or display a message
        }
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
