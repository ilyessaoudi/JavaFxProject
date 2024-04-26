package controles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.user;
import services.userservice;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Arrays;
import javafx.stage.FileChooser;

public class register {

    @FXML
    private TextField CPassword;

    @FXML
    private TextField Email;

    @FXML
    private TextField Password;

    @FXML
    private Button SignInMenu;

    @FXML
    private TextField Username;

    @FXML
    private ImageView Picture;

    @FXML
    private Button UploadPhoto;


    private String imagePath="";

    private userservice userService; // Inject userservice

    private File selectedImageFile; // To store the selected image file


    public register() {
        userService = new userservice(); // Initialize userservice
    }

    @FXML
    void Register(ActionEvent event) {
        String email = Email.getText();
        String password = Password.getText();
        String confirmPassword = CPassword.getText();

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Invalid email address. Please provide a valid email.");
            return; // Exit method if email is invalid
        }

        // Regex pattern for password validation
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if (!password.matches(passwordPattern)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Password",
                    "Password must contain at least 8 characters, one uppercase letter, one special character, and no whitespaces.");
            return; // Exit method if password is invalid
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Passwords Mismatch", "Passwords do not match. Please re-enter.");
            return; // Exit method if passwords do not match
        }

        try {
            if (userService.emailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Email Exists", "Email address already exists. Please choose a different email.");
                return; // Exit method if email already exists
            }

            // Creating the new user with ROLE_CLIENT
            user newUser = new user(email, Arrays.asList("ROLE_CLIENT"), password, Username.getText(), imagePath);
            userService.create(newUser);

            // Navigate to the login page
            NavigateToLoginPage(event);
        } catch (SQLException e) {
            // Provide meaningful feedback to the user
            System.err.println("Registration failed. Please try again later.");
            e.printStackTrace();
        }
    }


    boolean isValidEmail(String email) {
        // Simple email validation regex pattern
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    void UploadPicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedImageFile = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());

        if (selectedImageFile != null){
            imagePath = "file:///" + selectedImageFile.getAbsolutePath().replace("\\", "\\\\");
           Image image = new Image(selectedImageFile.toURI().toString(),101, 127, false, true);
            Picture.setImage(image);
        }
    }
    @FXML
    void NavigateToLoginPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene = SignInMenu.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
            Stage stage = (Stage) scene.getWindow();
            //new ZoomIn(root).play();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
