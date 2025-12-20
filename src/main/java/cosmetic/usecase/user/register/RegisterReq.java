package cosmetic.usecase.user.register;

import cosmetic.usecase.RequestData;

public class RegisterReq extends RequestData {
    public String fullName;
    
    // THÊM: Field bị thiếu
    public String username;
    public String email;
    public String password;
    
    // THÊM: Field confirmPassword
    public String confirmPassword;
    
    public String phone;
    public String address;
}