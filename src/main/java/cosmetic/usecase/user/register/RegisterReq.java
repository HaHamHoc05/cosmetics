package cosmetic.usecase.user.register;

import cosmetic.usecase.RequestData;

public class RegisterReq extends RequestData {
    public String fullName;
    public String email;
    public String password;
    public String phone;
    public String address;
}