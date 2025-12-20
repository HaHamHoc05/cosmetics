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
    private final PasswordEncoder passwordEncoder;

    public RegisterUseCase(UserRepository userRepo, 
                          PasswordEncoder passwordEncoder,
                          OutputBoundary<RegisterRes> outputBoundary) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(RegisterReq req) {
        RegisterRes res = new RegisterRes();
        try {
            // 1. Validate
            if (req.username == null || req.username.isEmpty()) {
                throw new IllegalArgumentException("Tên đăng nhập thiếu.");
            }
            if (req.password == null || req.password.length() < 6) {
                throw new IllegalArgumentException("Mật khẩu quá ngắn.");
            }
            if (!req.password.equals(req.confirmPassword)) {
                throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");
            }

            // 2. Check duplicate
            if (userRepo.findByUsername(req.username) != null) {
                throw new RuntimeException("Tên đăng nhập đã tồn tại.");
            }

            // 3. Create User & Hash Password
            User newUser = new User();
            newUser.setUsername(req.username);
            newUser.setFullName(req.fullName);
            newUser.setEmail(req.email);
            newUser.setPhone(req.phone);
            newUser.setAddress(req.address);
            
            // SỬA: Sử dụng PasswordEncoder được inject
            newUser.setPassword(passwordEncoder.encode(req.password));
            newUser.setRole(Role.USER); // Mặc định là USER

            userRepo.save(newUser);

            res.success = true;
            res.message = "Đăng ký thành công!";
            res.newUserId = newUser.getId();

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }
        
        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}

