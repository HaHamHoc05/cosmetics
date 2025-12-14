package cosmetic.usecase.user.login;

import adapters.user.login.LoginPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.User;
import cosmetic.repository.UserRepository;

public class LoginUseCase implements UseCase<LoginRequest, LoginResponse> {
    private final UserRepository userRepository;
    private final LoginPublisher publisher;

    public LoginUseCase(UserRepository userRepo, LoginPublisher publisher){
        this.userRepository = userRepo;
        this.publisher = publisher;
    }

    @Override
    public void execute(LoginRequest request, OutputBoundary<LoginResponse> output){
        User user = userRepository.findByUsername(request.getUsername());
        if(user == null) throw new RuntimeException("Username không tồn tại");
        if(!user.checkPassword(request.getPassword()))
            throw new RuntimeException("Sai mật khẩu");

        LoginResponse response = new LoginResponse(user);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}
