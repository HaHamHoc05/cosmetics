package adapters.product.delete;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.product.delete.DeleteProductRes;

public class DeleteProductPresenter implements OutputBoundary<DeleteProductRes> {
    private final DeleteProductViewModel viewModel;

    public DeleteProductPresenter(DeleteProductViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(DeleteProductRes res) {
        viewModel.setState(res.isSuccess, res.message);
    }
}