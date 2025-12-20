package adapters.product.create;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.product.create.CreateProductRes;

public class CreateProductPresenter implements OutputBoundary<CreateProductRes> {
    private final CreateProductViewModel viewModel;

    public CreateProductPresenter(CreateProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(CreateProductRes res) {
        viewModel.setState(res.success, res.message, res.newProductId);
    }
}