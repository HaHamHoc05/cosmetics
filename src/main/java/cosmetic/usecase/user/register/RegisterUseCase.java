package cosmetic.usecase.user.register;

import cosmetic.entities.PasswordEncoder;
import cosmetic.entities.Role;
import cosmetic.entities.User;
import cosmetic.repository.UserRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class RegisterUseCase implements UseCase<RegisterReq, RegisterRes> {
    
    private final UserRepository userRepo;
    private final OutputBoundary<RegisterRes> outputBoundary;

    public RegisterUseCase(UserRepository userRepo, OutputBoundary<RegisterRes> outputBoundary) {
        this.userRepo = userRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(RegisterReq req) {
        RegisterRes res = new RegisterRes();
        try {
            // 1. Validate
            if (req.username == null || req.username.isEmpty()) throw new IllegalArgumentException("Tên đăng nhập thiếu.");
            if (req.password == null || req.password.length() < 6) throw new IllegalArgumentException("Mật khẩu quá ngắn.");
            if (!req.password.equals(req.confirmPassword)) throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");

            // 2. Check duplicate
            if (userRepo.findByUsername(req.username) != null) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại.");
            }

            // 3. Create User & Hash Password
            User newUser = new User();
            newUser.setUsername(req.username);
            // Giả sử PasswordEncoder có hàm encode. Nếu là class utility static thì gọi trực tiếp.
            newUser.setPassword(new PasswordEncoder().encode(req.password)); 
            newUser.setRole(Role.USER); // Mặc định là USER

            userRepo.save(newUser);

            res.success = true;
            res.message = "Đăng ký thành công!";

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }
        
        if (outputBoundary != null) outputBoundary.present(res);
    }
}