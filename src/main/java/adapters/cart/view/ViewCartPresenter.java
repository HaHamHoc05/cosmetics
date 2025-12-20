package adapters.cart.view;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.cart.view.ViewCartRes;

public class ViewCartPresenter implements OutputBoundary<ViewCartRes> {
    private final ViewCartViewModel viewModel;

    public ViewCartPresenter(ViewCartViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ViewCartRes res) {
        viewModel.setState(res.success, res.message, res.items, res.grandTotal);
    }
}