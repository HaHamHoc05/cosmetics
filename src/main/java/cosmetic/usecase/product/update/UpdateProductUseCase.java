// File: UpdateProductUseCase.java
package cosmetic.usecase.product.update;

import cosmetic.entities.Product;
import cosmetic.entities.ProductStatus;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class UpdateProductUseCase implements UseCase<UpdateProductReq, UpdateProductRes> {
    private final ProductRepository repository;
    private final OutputBoundary<UpdateProductRes> outputBoundary;

    public UpdateProductUseCase(ProductRepository repository, OutputBoundary<UpdateProductRes> outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(UpdateProductReq req) {
        UpdateProductRes res = new UpdateProductRes();
        try {
            Product p = new Product();
            p.setId(req.id);
            p.setName(req.name);
            p.setPrice(req.price);
            p.setQuantity(req.quantity);
            p.setDescription(req.description);
            p.setImageUrl(req.imageUrl);
            p.setCategoryId(req.categoryId);
            p.setStatus(ProductStatus.ACTIVE); // Hoặc giữ status cũ nếu muốn
            
            repository.update(p);
            
            res.isSuccess = true;
            res.message = "Cập nhật thành công!";
        } catch (Exception e) {
            res.isSuccess = false;
            res.message = "Lỗi cập nhật: " + e.getMessage();
        }
        outputBoundary.present(res);
    }
}