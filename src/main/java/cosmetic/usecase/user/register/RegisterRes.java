package cosmetic.usecase.user.register;

import cosmetic.usecase.ResponseData;

public class RegisterRes extends ResponseData {
    public boolean success;
    public String message;
    public Long newUserId; 
}