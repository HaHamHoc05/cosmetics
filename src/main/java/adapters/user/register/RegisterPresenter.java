package adapters.user.register;

import cosmetic.OutputBoundary;
import cosmetic.usecase.user.register.RegisterResponse;

public class RegisterPresenter implements OutputBoundary<RegisterResponse>{
	private RegisterViewModel viewModel;
    @Override
    public void present(RegisterResponse response){
        this.viewModel = new RegisterViewModel(response.getUser());
    }
    public RegisterViewModel getViewModel(){ return viewModel; }
}
