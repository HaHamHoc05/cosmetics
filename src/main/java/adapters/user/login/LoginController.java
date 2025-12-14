package adapters.user.login;

import cosmetic.repository.UserRepository;
import cosmetic.usecase.user.login.LoginRequest;
import cosmetic.usecase.user.login.LoginUseCase;

public class LoginController {
	private final LoginUseCase useCase;
    private final LoginPresenter presenter;

    public LoginController(UserRepository userRepo, LoginPublisher publisher){
        this.presenter = new LoginPresenter();
        this.useCase = new LoginUseCase(userRepo, publisher);
    }

    public LoginViewModel handle(String username, String password){
        LoginRequest request = new LoginRequest(username, password);
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}
