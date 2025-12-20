package adapters.product.delete;
import cosmetic.usecase.product.delete.DeleteProductReq;
import cosmetic.usecase.product.delete.DeleteProductUseCase;

public class DeleteProductController {
    private final DeleteProductUseCase useCase;

    public DeleteProductController(DeleteProductUseCase useCase) {
        this.useCase = useCase;
    }

    public void execute(Long id) {
        useCase.execute(new DeleteProductReq(id));
    }
}