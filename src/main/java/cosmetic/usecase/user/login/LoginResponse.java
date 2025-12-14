package cosmetic.usecase.user.login;

import cosmetic.ResponseData;
import cosmetic.entities.User;

public class LoginResponse extends ResponseData {
    private final User user;
    public LoginResponse(User user){ this.user=user; }
    public User getUser(){ return user; }
}