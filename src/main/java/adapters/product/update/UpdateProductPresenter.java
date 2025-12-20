package adapters.product.update;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.product.update.UpdateProductRes;

public class UpdateProductPresenter implements OutputBoundary<UpdateProductRes> {
    private final UpdateProductViewModel viewModel;

    public UpdateProductPresenter(UpdateProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateProductRes res) {
        viewModel.setState(res.isSuccess, res.message);
    }
}