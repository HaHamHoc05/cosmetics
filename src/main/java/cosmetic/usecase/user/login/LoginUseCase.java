package cosmetic.usecase.user.login;

import cosmetic.entities.User;
import cosmetic.repository.UserRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class LoginUseCase implements UseCase<LoginReq, LoginRes> {

    private final UserRepository userRepo;
    private final OutputBoundary<LoginRes> outputBoundary;
    // THÊM: Cần inject PasswordEncoder qua constructor
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(UserRepository userRepo, 
                       PasswordEncoder passwordEncoder,
                       OutputBoundary<LoginRes> outputBoundary) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(LoginReq req) {
        LoginRes res = new LoginRes();
        try {
            User user = userRepo.findByUsername(req.username);
            
            // SỬA: Sử dụng PasswordEncoder được inject
            if (user == null || !passwordEncoder.matches(req.password, user.getPassword())) {
                throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu.");
            }

            res.success = true;
            res.message = "Đăng nhập thành công";
            res.userId = user.getId();
            res.role = user.getRole().name();
            res.username = user.getUsername();

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}

// SỬA: Tạo interface PasswordEncoder riêng trong package usecase
interface PasswordEncoder {
    boolean matches(String rawPassword, String encodedPassword);
}