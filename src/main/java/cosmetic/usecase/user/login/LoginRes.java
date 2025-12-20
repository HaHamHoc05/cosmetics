package cosmetic.usecase.user.login;

import cosmetic.usecase.ResponseData;

public class LoginRes extends ResponseData {
    public boolean success;
    public String message;
    public Long userId;
    public String fullName;
    
    public String role;
    public String username;
}