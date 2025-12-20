package adapters.product.getlist;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.products.getlist.GetListProductRes;

public class GetListProductPresenter implements OutputBoundary<GetListProductRes> {
    private final GetListProductViewModel viewModel;

    public GetListProductPresenter(GetListProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetListProductRes res) {
        viewModel.setState(res.success, res.message, res.products);
    }
}