package cosmetic.usecase.user.login;

import cosmetic.entities.User;
import cosmetic.entities.PasswordEncoder;  // SỬA: Dùng interface từ entities
import cosmetic.repository.UserRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class LoginUseCase implements UseCase<LoginReq, LoginRes> {

    private final UserRepository userRepo;
    private final OutputBoundary<LoginRes> outputBoundary;
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
            // Validate input
            if (req.username == null || req.username.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên đăng nhập không được để trống");
            }
            
            if (req.password == null || req.password.isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống");
            }
            
            // Find user
            User user = userRepo.findByUsername(req.username);
            
            if (user == null) {
                throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu.");
            }
            
            // Verify password
            if (!passwordEncoder.matches(req.password, user.getPassword())) {
                throw new RuntimeException("Sai tên đăng nhập hoặc mật khẩu.");
            }

            // Success
            res.success = true;
            res.message = "Đăng nhập thành công";
            res.userId = user.getId();
            res.role = user.getRole().name();
            res.username = user.getUsername();
            res.fullName = user.getFullName();

        } catch (IllegalArgumentException e) {
            res.success = false;
            res.message = e.getMessage();
        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}