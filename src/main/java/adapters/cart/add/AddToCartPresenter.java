package adapters.cart.add;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.cart.add.AddToCartRes;

public class AddToCartPresenter implements OutputBoundary<AddToCartRes> {
    private final AddToCartViewModel viewModel;

    public AddToCartPresenter(AddToCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddToCartRes res) {
        viewModel.setState(res.success, res.message, res.totalItems);
    }
}