package cosmetic.usecase.user.register;

import cosmetic.ResponseData;
import cosmetic.entities.User;

public class RegisterResponse extends ResponseData {
    private final User user;
    public RegisterResponse(User user){ this.user = user; }
    public User getUser(){ return user; }
}