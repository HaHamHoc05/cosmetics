package cosmetic.usecase.user.login;

import cosmetic.RequestData;

public class LoginRequest extends RequestData {
    private final String username;
    private final String password;
    public LoginRequest(String username, String password){
        this.username=username;
        this.password=password;
    }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
}