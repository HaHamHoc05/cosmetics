package cosmetic.usecase.user.register;

import adapters.user.register.RegisterPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.User;
import cosmetic.repository.UserRepository;

public class RegisterUseCase implements UseCase<RegisterRequest, RegisterResponse>{
	private final UserRepository userRepository;
    private final RegisterPublisher publisher;

    public RegisterUseCase(UserRepository repo, RegisterPublisher publisher){
        this.userRepository = repo;
        this.publisher = publisher;
    }

    @Override
    public void execute(RegisterRequest request, OutputBoundary<RegisterResponse> output){
        if(userRepository.findByUsername(request.getUsername()) != null)
            throw new RuntimeException("Username đã tồn tại");
        try {
        	User user = new User(request.getUsername(), request.getPassword(), request.getEmail(), "USER");
            userRepository.save(user);
            RegisterResponse response = new RegisterResponse(user);
            output.present(response);
            publisher.notifySubscribers(response);
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}