package controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.carriere;
import models.user;
import services.carriereservice;
import services.userSession;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import services.userservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ListAllUsers {

    @FXML
    private GridPane Requests;
    private carriereservice carServ = new carriereservice();

    private userservice userServ= new userservice();
    @FXML
    private HBox CheckRequests;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/backOffice.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminProfile.fxml"));
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

        try {
            // Fetch all career data
            //List<carriere> careers = carServ.getAllCarrieres();
            List<user> users=userServ.read();

            if (users != null && !users.isEmpty()) {
                Requests.getChildren().clear();
                int row = 0;
                int column = 0;
                Requests.getRowConstraints().clear();
                Requests.getColumnConstraints().clear();
                for (user User : users) {
                    //user currentUserData = userServ.getUserById(career.getUserId());

                            carriere career=carServ.fetchCareer(User.getId());


                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/UserCard.fxml"));
                    VBox cardBox = fxmlLoader.load();
                    UserCard userCard = fxmlLoader.getController();
                    userCard.setData(User, career);

                    // Get references to buttons from FXML (assuming they have IDs)
                    Button declineButton = (Button) cardBox.lookup("#DeclineButton");


                    declineButton.setOnAction(event -> {
                        try {
                            // Assuming you have access to the user's ID

                            // Delete the career associated with the user
                            if (career!=null){
                            boolean deleted = carServ.delete(User.getId());
                            boolean deletedUser=userServ.deleteUser(User.getId());
                            // Display an alert based on the deletion result
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Career Deletion");
                            if (deleted && deletedUser) {
                                alert.setContentText("User deleted successfully.");
                                alert.showAndWait();
                                refresh();
                            } else {
                                alert.setContentText("Failed to delete User.");
                                alert.setAlertType(Alert.AlertType.ERROR);
                                alert.showAndWait();
                            }
                            }
                        else {
                                boolean deletedUser=userServ.deleteUser(User.getId());
                                // Display an alert based on the deletion result
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Career Deletion");
                                if (deletedUser) {
                                    alert.setContentText("User deleted successfully.");
                                    alert.showAndWait();
                                    refresh();
                                } else {
                                    alert.setContentText("Failed to delete User.");
                                    alert.setAlertType(Alert.AlertType.ERROR);
                                    alert.showAndWait();
                                }
                            }
                        }catch (SQLException e) {
                            // Handle the SQL exception
                            e.printStackTrace();
                            // Display an error message or perform any other error handling

                            // Show an alert for the SQL exception
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setContentText("An error occurred while deleting the User. Please try again.");
                            alert.showAndWait();
                        }
                    });
                    if (column == 6){
                        column = 0;
                        ++row;

                    }
                    Requests.add(cardBox,column++,row);
                    GridPane.setMargin(cardBox, new Insets(10));


                }
            } else {
                // Handle case when no career data is found
                Requests.add(new Label("Users were not found."), 0, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void CheckRequests(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RolesRequests.fxml"));
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


        void refresh () {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/listAllUsers.fxml"));
                adminIMG.getScene().setRoot(root);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }




    @FXML
    void listAllUsers(MouseEvent event) {

    }
}


