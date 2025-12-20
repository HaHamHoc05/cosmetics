package adapters.user.login;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.user.login.LoginRes;

public class LoginPresenter implements OutputBoundary<LoginRes> {
    private final LoginViewModel viewModel;

    public LoginPresenter(LoginViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(LoginRes res) {
        // Chuyển đổi dữ liệu từ Res sang ViewModel để hiển thị
        viewModel.setState(res.success, res.message, res.userId, res.role);
    }
}