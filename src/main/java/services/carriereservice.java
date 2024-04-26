package services;

import models.carriere;
import utils.MyDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class carriereservice  {

    private Connection connection;

    public carriereservice() {
        connection = MyDatabase.getInstance().getConnection();
    }

    public carriere fetchCareer(int userId) throws SQLException {
        carriere Carriere = null;
        String sql = "SELECT titre, score, cv FROM carriere WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("titre");
                    int score = rs.getInt("score");
                    File cv = new File(rs.getString("cv")); // Assuming the cv path is stored as a string
                    Carriere = new carriere(userId, cv, score, title);
                }
            }
        }
        return Carriere;
    }


    public List<carriere> getAllCarrieres() throws SQLException {
        List<carriere> carrieres = new ArrayList<>();

        String sql = "SELECT * FROM carriere";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String title = rs.getString("titre");
                int score = rs.getInt("score");
                String cvAbsolutePath = rs.getString("cv");
                File cvFile = new File(cvAbsolutePath);
                // Create a new carriere object and add it to the list
                carriere career = new carriere(userId,cvFile, score, title);
                carrieres.add(career);
            }
        }

        return carrieres;
    }





    // Function to create a career for a user
    public boolean create(int userId, File cv, int score, String title) throws SQLException {
        String sql = "INSERT INTO carriere (user_id, cv, score, titre) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, cv.getAbsolutePath()); // Assuming the cv path is stored as a string
            ps.setInt(3, score);
            ps.setString(4, title);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Function to delete a career for a user
    public boolean delete(int userId) throws SQLException {
        String sql = "DELETE FROM carriere WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
}