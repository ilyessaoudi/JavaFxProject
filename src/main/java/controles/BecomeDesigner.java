package controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.user;
import services.userSession;
import services.userservice;
import services.carriereservice;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class BecomeDesigner {
    private carriereservice Carriereserv;

    public void setCarriereservice(carriereservice Carriereserv) {
        this.Carriereserv = Carriereserv;
    }

    private String cvPath="";
    private File cv;
    @FXML
    private HBox achatBtn;

    @FXML
    private ImageView achatIcon;

    @FXML
    private ImageView adminIMG;

    @FXML
    private HBox collectBtn;

    @FXML
    private HBox commandsBtn;

    @FXML
    private ImageView commandsIcon;

    @FXML
    private Pane content_area;

    @FXML
    private HBox dashboardBtn;

    @FXML
    private ImageView dashboardIcon;

    @FXML
    private HBox fundrisingBtn;

    @FXML
    private HBox productsBtn;

    @FXML
    private ImageView productsIcon;

    @FXML
    private HBox profileBtn;

    @FXML
    private ImageView profileIcon;

    @FXML
    private HBox sideBarLogout;

    @FXML
    private Label username;

    @FXML
    void logout(MouseEvent event) {
        try {
            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Load the FXML file for the profile interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Create a new stage for the profile interface
            Stage profileStage = new Stage();
            profileStage.setTitle("Login Page");
            profileStage.setScene(new Scene(root));

            // Show the profile interface stage
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions while loading the profile interface
        }
    }


    @FXML
    void open_achatList(MouseEvent event) {

    }

    @FXML
    void open_commandsList(MouseEvent event) {

    }

    @FXML
    void open_dashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontOffice.fxml"));
                Parent root = loader.load();

                // Create a new scene for the front office interface
                Scene scene = new Scene(root);

                // Get the current stage (window)
                Stage stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();

                // Set the scene to the current stage and show it
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle any potential exceptions while loading the profile interface
            }
        }


    @FXML
    void open_event(MouseEvent event) {

    }

    @FXML
    void open_fundrisingList(MouseEvent event) {

    }

    @FXML
    void open_productsList(MouseEvent event) {

    }

    @FXML
    void open_profile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientProfile.fxml"));
            Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((HBox) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @FXML
    public void UploadResume(MouseEvent mouseEvent) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Upload Resume");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(((Node) mouseEvent.getSource()).getScene().getWindow());

            if (selectedFile != null) {
                // Set the cv file object to the selected file
                cv = selectedFile;
                System.out.println("Selected file: " + cv.getAbsolutePath());
            } else {
                System.out.println("No file selected.");
            }
        }



    @FXML
    public void SubmitCar(MouseEvent mouseEvent) {
        Carriereserv = new carriereservice();
        int score=0;
        String title = "Amateur";
        String cvPath;

        user currentUser = userSession.getCurrentUser();
        int userId = currentUser.getId();

        try {
            // Create a career entry for the user
            boolean success = Carriereserv.create(userId, cv, score, title);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Career submitted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit career. Please try again later.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while processing your request.");
        }
    }



    @FXML
    public void initialize() {
        // Retrieve the current user data from the UserSession class
        user currentUser = userSession.getCurrentUser();

        if (currentUser != null) {
            String formattedImagePath = currentUser.getPicture().replace("\\", "/");
            Image image = new Image(formattedImagePath); // Load the image

            // Set the image to the ImageView
            adminIMG.setImage(image);

            Circle clip = new Circle(adminIMG.getFitWidth() / 2, adminIMG.getFitHeight() / 2, adminIMG.getFitWidth() / 2);
            adminIMG.setClip(clip);
            // Set the username
            username.setText(currentUser.getUsername());


        } else {
            // Handle case where no user is logged in
            username.setText("Guest"); // Or any other default value
        }
    }


    void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
