package models;

import java.io.File;

public class carriere {
    private int id;
    private int userId; // Foreign key referencing the user ID
    private File cv; // PDF file
    private int score;
    private String title;

    // Constructor
    public carriere(int userId, File cv, int score, String title) {
        this.userId = userId;
        this.cv = cv;
        this.score = score;
        this.title = title;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public File getCv() {
        return cv;
    }

    public void setCv(File cv) {
        this.cv = cv;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
