package controles;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.user;
import services.userSession;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import services.userservice;
import javafx.scene.control.Alert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

public class ClientProfile {
    private userservice userService;


    // Inject the UserService instance into your controller
    public void setUserService(userservice userService) {
        this.userService = userService;
    }
    private String imagePath="";
    private File selectedImageFile;
    @FXML
    private ImageView ClientImg;

    @FXML
    private TextField CnewPw;

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
    private TextField currentEmail;

    @FXML
    private TextField currentPw;

    @FXML
    private TextField currentUsername;

    @FXML
    private HBox dashboardBtn;

    @FXML
    private ImageView dashboardIcon;

    @FXML
    private HBox fundrisingBtn;

    @FXML
    private TextField newPw;

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
            ClientImg.setImage(image);

            Circle clip = new Circle(adminIMG.getFitWidth() / 2, adminIMG.getFitHeight() / 2, adminIMG.getFitWidth() / 2);
            adminIMG.setClip(clip);
            // Set the username
            username.setText(currentUser.getUsername());
            currentEmail.setText(currentUser.getEmail());
            currentUsername.setText(currentUser.getUsername());
            ClientImg.setImage(image);

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

    @FXML
    public void UploadPic(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        selectedImageFile = fileChooser.showOpenDialog(((Button) mouseEvent.getSource()).getScene().getWindow());

        if (selectedImageFile != null){
            imagePath = "file:///" + selectedImageFile.getAbsolutePath().replace("\\", "\\\\");
            Image image = new Image(selectedImageFile.toURI().toString(),101, 127, false, true);
            ClientImg.setImage(image);
        }
    }
    @FXML
    public void UpdateProf(MouseEvent mouseEvent) {
        userService = new userservice();

        String newEmail = currentEmail.getText();
        String newUsername = currentUsername.getText();
        String currentPassword = currentPw.getText();
        String newPassword = newPw.getText();
        String confirmNewPassword = CnewPw.getText();

        user currentUser = userSession.getCurrentUser();

        Verifyer verifyer = BCrypt.verifyer();
        boolean currentPasswordMatches = verifyer.verify(currentPassword.toCharArray(), currentUser.getPassword()).verified;
        if (!currentPassword.isEmpty()) {
            if (!currentPasswordMatches) {
                showAlert(Alert.AlertType.WARNING, "Incorrect Current Password", "Please type your current password again.");
                return; // Exit method if current password does not match
            }

            // Regex pattern for password validation
            String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

            if (!newPassword.matches(passwordPattern)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Password",
                        "New password must contain at least 8 characters, one uppercase letter, one special character, and no whitespaces.");
                return; // Exit method if password is invalid
            }

            if (!newPassword.equals(confirmNewPassword)) {
                showAlert(Alert.AlertType.WARNING, "Passwords Mismatch", "New password and confirm password do not match.");
                return; // Exit the method
            }

            Hasher hasher = BCrypt.with(Version.VERSION_2A);
            String hashedNewPassword = hasher.hashToString(12, newPassword.toCharArray());

            currentUser.setPassword(hashedNewPassword);
        }

        if (selectedImageFile != null) {
            currentUser.setPicture(imagePath);
        }

        if (!isValidEmail(newEmail)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Invalid email address. Please provide a valid email.");
            return; // Exit method if email is invalid
        }



        try {

                if (userService.emailExists(newEmail)) {
                    showAlert(Alert.AlertType.WARNING, "Email Exists", "Email address already exists. Please choose a different email.");
                    return; // Exit method if email already exists
                }

                currentUser.setEmail(newEmail);
            currentUser.setUsername(newUsername);
            currentUser.setRoles(currentUser.getRoles());
            userService.update(currentUser);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
            userSession.setCurrentUser(currentUser);
            Affichage();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update profile!");
        }
    }

    boolean isValidEmail(String email) {
        // Simple email validation regex pattern
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    @FXML
    public void BecomeDesigner(MouseEvent mouseEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BecomeDesigner.fxml"));
            Parent root = loader.load();

            // Create a new scene for the front office interface
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) ((HBox) mouseEvent.getSource()).getScene().getWindow();

            // Set the scene to the current stage and show it
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any potential exceptions while loading the profile interface
        }
    }
    void Affichage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ClientProfile.fxml"));
            adminIMG.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
