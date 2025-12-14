package adapters.user.login;

import cosmetic.usecase.user.login.LoginOutputBoundary;
import cosmetic.usecase.user.login.LoginResponse;

public class LoginPresenter implements LoginOutputBoundary {
    private LoginViewModel viewModel;
    @Override
    public void present(LoginResponse response){
        this.viewModel = new LoginViewModel(response.getUser());
    }
    public LoginViewModel getViewModel(){ return viewModel; }
}