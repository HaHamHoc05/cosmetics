package cosmetic.usecase.user.register;

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
            // 1. VALIDATION INPUT (Business Rule)
            if (req.fullName == null || req.fullName.isEmpty()) {
                throw new IllegalArgumentException("Họ tên không được để trống.");
            }
            if (req.email == null || !req.email.contains("@")) {
                throw new IllegalArgumentException("Email không hợp lệ.");
            }
            if (req.password == null || req.password.length() < 6) {
                throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
            }

            // 2. CHECK DUPLICATE EMAIL (Business Rule)
            User existingUser = userRepo.findByEmail(req.email);
            if (existingUser != null) {
                throw new RuntimeException("Email '" + req.email + "' đã được sử dụng.");
            }

            // 3. CREATE ENTITY
            User newUser = new User();
            newUser.setFullName(req.fullName);
            newUser.setEmail(req.email);
            newUser.setPassword(req.password); // Lưu ý: Thực tế nên mã hóa ở đây (MD5/BCrypt)
            newUser.setPhone(req.phone);
            newUser.setAddress(req.address);
            newUser.setRoleId(2); // Mặc định là Khách hàng (Customer)

            // 4. SAVE DB
            userRepo.save(newUser);

            // 5. SUCCESS RESPONSE
            res.success = true;
            res.message = "Đăng ký thành công!";
            res.newUserId = newUser.getId();

        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }

        // 6. PRESENTER
        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}