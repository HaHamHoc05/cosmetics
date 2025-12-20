package adapters.product.create;

import cosmetic.usecase.InputBoundary;
import cosmetic.usecase.product.create.CreateProductReq;
import cosmetic.usecase.product.create.CreateProductRes;
import java.math.BigDecimal;

public class CreateProductController {
    private final InputBoundary<CreateProductReq, CreateProductRes> useCase;

    public CreateProductController(InputBoundary<CreateProductReq, CreateProductRes> useCase) {
        this.useCase = useCase;
    }

    public void execute(InputDTO input) {
        CreateProductReq req = new CreateProductReq();
        req.name = input.name;
        req.description = input.description;
        req.price = input.price;
        req.quantity = input.quantity;
        req.categoryId = input.categoryId;
        req.image = input.image;
        
        useCase.execute(req);
    }

    public static class InputDTO {
        public String name;
        public String description;
        public BigDecimal price;
        public int quantity;
        public Long categoryId;
        public String image;
    }
}