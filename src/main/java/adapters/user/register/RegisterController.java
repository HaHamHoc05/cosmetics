package adapters.user.register;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.user.register.RegisterReq;
import cosmetic.usecase.user.register.RegisterRes; 

public class RegisterController {
    private final InputBoundary<RegisterReq, RegisterRes> useCase;

    public RegisterController(InputBoundary<RegisterReq, RegisterRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(InputDTO input) {
        RegisterReq req = new RegisterReq();
        req.username = input.username;
        req.password = input.password;
        req.confirmPassword = input.confirmPassword;
        req.fullName = input.fullName;
        req.email = input.email;
        req.phone = input.phone;
        req.address = input.address;
        
        useCase.execute(req);
    }

    public static class InputDTO {
        public String username;
        public String password;
        public String confirmPassword;
        public String fullName;
        public String email;
        public String phone;
        public String address;
    }
}