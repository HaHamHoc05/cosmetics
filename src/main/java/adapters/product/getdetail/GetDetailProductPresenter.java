package adapters.product.getdetail;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.products.getdetail.GetDetailProductRes;

public class GetDetailProductPresenter implements OutputBoundary<GetDetailProductRes> {
    private final GetDetailProductViewModel viewModel;

    public GetDetailProductPresenter(GetDetailProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetDetailProductRes res) {
        viewModel.setState(res.success, res.message, res);
    }
}