package adapters.product.update;
import cosmetic.usecase.product.update.UpdateProductReq;
import cosmetic.usecase.product.update.UpdateProductUseCase;

public class UpdateProductController {
    private final UpdateProductUseCase useCase;

    public UpdateProductController(UpdateProductUseCase useCase) {
        this.useCase = useCase;
    }

    public static class InputDTO {
        public Long id;
        public String name;
        public double price;
        public int quantity;
        public String description;
        public String image;
        public Long categoryId;
    }

    public void execute(InputDTO input) {
        UpdateProductReq req = new UpdateProductReq();
        req.id = input.id;
        req.name = input.name;
        req.price = input.price;
        req.quantity = input.quantity;
        req.description = input.description;
        req.imageUrl = input.image;
        req.categoryId = input.categoryId;
        useCase.execute(req);
    }
}