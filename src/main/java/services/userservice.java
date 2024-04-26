package services;
import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import com.google.gson.Gson;
import models.user;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class userservice implements IService<user> {
    private Connection connection;

    public userservice() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void create(user user) throws SQLException {
        Hasher argon2Hasher = BCrypt.with(Version.VERSION_2A); // Use Argon2 with bcrypt compatibility
        String hashedPassword = argon2Hasher.hashToString(12, user.getPassword().toCharArray());

        String rolesJson = new Gson().toJson(user.getRoles()); // Convert roles list to JSON string
        String sql = "INSERT INTO user (email, password, picture, username, roles) VALUES ('"
                + user.getEmail() + "', '"
                + hashedPassword + "', '"
                + user.getPicture() + "', '"
                + user.getUsername() + "', '"
                + rolesJson + "')";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    @Override
    public void update(user user) throws SQLException {
        String rolesJson = new Gson().toJson(user.getRoles()); // Convert roles list to JSON string
        String sql = "UPDATE user SET email = ?, password = ?, picture = ?, username = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getPicture());
        ps.setString(4, user.getUsername());
        ps.setInt(5, user.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {

    }
    public boolean deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);

        // Execute the update and check if rows were affected
        int rowsDeleted = ps.executeUpdate();

        // Return true if at least one row was deleted, false otherwise
        return rowsDeleted > 0;
    }
    @Override
    public List<user> read() throws SQLException {
        String sql = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<user> users = new ArrayList<>();
        while (rs.next()) {
            user user = new user();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            String rolesString = rs.getString("roles");
            List<String> rolesList = Arrays.asList(rolesString.split(","));
            user.setRoles(rolesList);
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setPicture(rs.getString("picture"));

            users.add(user);
        }
        return users;
    }

    public boolean authenticate(String email, String password) throws SQLException {
        String query = "SELECT password FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet resultSet = ps.executeQuery();

        if (resultSet.next()) {
            String hashedPasswordFromDB = resultSet.getString("password");
            // Use BCrypt.verifyer() to verify the password
            boolean isPasswordCorrect = BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromDB).verified;
            return isPasswordCorrect;
        } else {
            return false; // No user found with the provided email
        }
    }


    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1) > 0;
        } else {
            return false; // No rows returned
        }
    }

    public user getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            user user = new user();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            String rolesString = rs.getString("roles");
            List<String> rolesList = Arrays.asList(rolesString.split(","));
            user.setRoles(rolesList);
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setPicture(rs.getString("picture"));
            return user;
        } else {
            return null; // No user found with the provided email
        }
    }

    public user getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            user user = new user();
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            String rolesString = rs.getString("roles");
            List<String> rolesList = Arrays.asList(rolesString.split(","));
            user.setRoles(rolesList);
            user.setPassword(rs.getString("password"));
            user.setUsername(rs.getString("username"));
            user.setPicture(rs.getString("picture"));
            return user;
        } else {
            return null; // No user found with the provided email
        }
    }


    public void updateUserRoleToDesigner(int userId) throws SQLException {
        // SQL statement to update the role
        String sql = "UPDATE user SET roles = ? WHERE id = ?";

        // New role list containing only ROLE_DESIGNER
        List<String> newRoles = new ArrayList<>();
        newRoles.add("ROLE_DESIGNER");

        // Convert the new role list to JSON string
        String newRolesJson = new Gson().toJson(newRoles);

        // Prepare the statement
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, newRolesJson); // Set the new roles JSON string
        statement.setInt(2, userId); // Set the user id to update

        // Execute the update
        statement.executeUpdate();

        // Close the statement
        statement.close();
    }
}
