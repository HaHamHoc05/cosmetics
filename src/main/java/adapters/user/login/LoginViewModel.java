package adapters.user.login;

import adapters.Publisher;

public class LoginViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public Long userId;
    public String role;

    public void setState(boolean isSuccess, String message, Long userId, String role) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.userId = userId;
        this.role = role;
        notifySubscribers();
    }
}