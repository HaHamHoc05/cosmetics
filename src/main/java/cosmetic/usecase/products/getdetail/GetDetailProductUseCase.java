package cosmetic.usecase.products.getdetail;

import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetDetailProductUseCase implements UseCase<GetDetailProductReq, GetDetailProductRes> {

    private final ProductRepository productRepo;
    private final OutputBoundary<GetDetailProductRes> outputBoundary;

    public GetDetailProductUseCase(ProductRepository productRepo, OutputBoundary<GetDetailProductRes> outputBoundary) {
        this.productRepo = productRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GetDetailProductReq req) {
        GetDetailProductRes res = new GetDetailProductRes();

        try {
            if (req.productId == null) throw new RuntimeException("ID sản phẩm không hợp lệ");

            Product p = productRepo.findById(req.productId);
            
            if (p == null) {
                throw new RuntimeException("Sản phẩm không tồn tại.");
            }

            // Map Data
            res.id = p.getId();
            res.name = p.getName();
            res.price = p.getPrice();
            res.quantity = p.getQuantity();
            res.description = p.getDescription();
            res.imageUrl = p.getImageUrl();
            
            res.success = true;
        } catch (Exception e) {
            res.success = false;
            res.message = e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}