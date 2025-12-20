package cosmetic.usecase.user.login;

import cosmetic.entities.User;
import cosmetic.repository.UserRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class LoginUseCase implements UseCase<LoginReq, LoginRes> {

    private final UserRepository userRepo;
    private final OutputBoundary<LoginRes> outputBoundary;

    public LoginUseCase(UserRepository userRepo, OutputBoundary<LoginRes> outputBoundary) {
        this.userRepo = userRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(LoginReq req) {
        LoginRes res = new LoginRes();

        try {
            // 1. FIND USER
            User user = userRepo.findByEmail(req.email);
            
            // 2. CHECK EXISTENCE
            if (user == null) {
                throw new RuntimeException("Email không tồn tại hoặc sai thông tin.");
            }

            // 3. CHECK PASSWORD (Business Rule)
            // Nếu có class PasswordEncoder thì dùng: !encoder.matches(req.password, user.getPassword())
            if (!user.getPassword().equals(req.password)) { 
                throw new RuntimeException("Mật khẩu không chính xác.");
            }

            // 4. SUCCESS
            res.success = true;
            res.message = "Đăng nhập thành công!";
            res.userId = user.getId();
            res.fullName = user.getFullName();
            res.roleId = user.getRoleId();

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}