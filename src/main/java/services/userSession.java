package services;

import models.user;

public class userSession {
    private static user currentUser;

    public static user getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(user user) {
        currentUser = user;
    }
}