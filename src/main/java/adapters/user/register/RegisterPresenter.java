package adapters.user.register;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.user.register.RegisterRes;

public class RegisterPresenter implements OutputBoundary<RegisterRes> {
    private final RegisterViewModel viewModel;

    public RegisterPresenter(RegisterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RegisterRes res) {
        viewModel.setState(res.success, res.message, res.newUserId);
    }
}