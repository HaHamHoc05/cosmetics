package adapters.user.register;

import cosmetic.repository.UserRepository;
import cosmetic.usecase.user.register.RegisterRequest;
import cosmetic.usecase.user.register.RegisterUseCase;

public class RegisterController {
	private final RegisterUseCase useCase;
    private final RegisterPresenter presenter;

    public RegisterController(UserRepository repo, RegisterPublisher publisher){
        this.presenter = new RegisterPresenter();
        this.useCase = new RegisterUseCase(repo, publisher);
    }

    public RegisterViewModel handle(String username, String password, String email){
        RegisterRequest request = new RegisterRequest(username,password,email);
        useCase.execute(request,presenter);
        return presenter.getViewModel();
    }
}
