package service;

import model.UserData;

public record RegisterRequest(String username, String password, String email) {
    UserData userData() {
        return new UserData(username, password, email);
    }
}
