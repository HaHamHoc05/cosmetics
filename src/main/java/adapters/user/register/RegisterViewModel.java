package adapters.user.register;

import adapters.Publisher;

public class RegisterViewModel extends Publisher {
    public boolean isSuccess;
    public String message;
    public Long newUserId;

    public void setState(boolean isSuccess, String message, Long newUserId) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.newUserId = newUserId;
        notifySubscribers();
    }
}