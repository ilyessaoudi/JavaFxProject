package controles;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.user;
import services.userSession;
import services.userservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class login {
    @FXML
    private TextField Email;

    @FXML
    private TextField Password;

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpMenu;


    @FXML
    void Login(ActionEvent event) {
        String email = Email.getText();
        String password = Password.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter both email and password.");
            return;
        }

        try {
            userservice userService = new userservice();
            if (userService.authenticate(email, password)) {
                user currentUser = userService.getUserByEmail(email);
                userSession.setCurrentUser(currentUser);

                System.out.println(currentUser.getRolesAsString());

                if (currentUser.getRolesAsString().contains("ROLE_CLIENT")) {
                    // Redirect to client page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontOffice.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else if (currentUser.getRolesAsString().contains("ROLE_ADMIN")) {
                    // Redirect to admin page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/backOffice.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }   else if (currentUser.getRolesAsString().contains("ROLE_DESIGNER")) {
                    // Redirect to admin page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontDesignerOffice.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    // Handle other roles if needed
                    showAlert(Alert.AlertType.WARNING, "Role Not Supported", "Your role is not supported.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Authentication Failed", "Invalid email or password.");
            }
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Authentication Error", "An error occurred during authentication.");
            e.printStackTrace();
        }
    }


    @FXML
    void NavigateToSignUpPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/register.fxml"));
            Scene scene = SignUpMenu.getScene();
            if (scene != null) {
                scene.setRoot(root);
            }
            Stage stage = (Stage) scene.getWindow();
            //new ZoomIn(root).play();

        } catch (IOException e) {
            System.err.println(e.getMessage());
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