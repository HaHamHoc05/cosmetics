package adapters.cart.delete;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.cart.delete.RemoveItemRes;

public class RemoveItemPresenter implements OutputBoundary<RemoveItemRes> {
    private final RemoveItemViewModel viewModel;

    public RemoveItemPresenter(RemoveItemViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(RemoveItemRes res) {
        viewModel.setState(res.success, res.message);
    }
}