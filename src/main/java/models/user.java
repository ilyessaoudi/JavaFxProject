package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class user {
    private int id; // L'ID sera auto-incrémenté dans la base de données
    private String email;
    private List<String> roles; // Modification de String en List<String>
    private String password;
    private String username;
    private String picture;

    // Constructeur sans l'ID (car il est auto-incrémenté)
    public user()
    {

    }
    public user(String email, List<String> roles, String password, String username, String picture) {
        this.email = email;
        this.roles = new ArrayList<>(); // Initialize the roles list
        this.roles.addAll(roles); // Add the passed roles
        this.roles.add("ROLE_USER"); // Add ROLE_USER as a default role
        this.password = password;
        this.username = username;
        this.picture = picture;
    }


    // Getters et Setters pour id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = new ArrayList<>(roles);
    }


    public String getRolesAsString() {
        StringBuilder rolesString = new StringBuilder();
        for (String role : roles) {
            if (rolesString.length() > 0) {
                rolesString.append(", ");
            }
            rolesString.append(role);
        }
        return rolesString.toString();
    }

    // Méthode utilitaire pour définir les rôles à partir d'une chaîne séparée par des virgules
    public void setRolesFromString(String rolesString) {
        this.roles = Arrays.asList(rolesString.split(","));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof user)) return false;
        user user = (user) o;
        return getId() == user.getId() && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getRoles(), user.getRoles()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPicture(), user.getPicture());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getRoles(), getPassword(), getUsername(), getPicture());
    }
}
