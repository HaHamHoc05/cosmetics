package adapters.user.login;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.user.login.LoginReq;
import cosmetic.usecase.user.login.LoginRes;

public class LoginController {
	private final InputBoundary<LoginReq, LoginRes> useCase;
	
	public LoginController(InputBoundary<LoginReq, LoginRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(String username, String password) {
        // 1. Tạo Req từ dữ liệu đầu vào
        LoginReq req = new LoginReq();
        req.username = username;
        req.password = password;
        
        // 2. Gửi Req vào UseCase
        useCase.execute(req);
    }
}