package cosmetic.usecase.product.create;

import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class CreateProductUseCase implements UseCase<CreateProductReq, CreateProductRes> {
    
    private final ProductRepository productRepo;
    private final OutputBoundary<CreateProductRes> outputBoundary;

    public CreateProductUseCase(ProductRepository productRepo, OutputBoundary<CreateProductRes> outputBoundary) {
        this.productRepo = productRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateProductReq req) {
        CreateProductRes res = new CreateProductRes();
        try {
            // 1. Validate
            if (req.name == null || req.name.isEmpty()) throw new IllegalArgumentException("Tên sản phẩm trống.");
            if (req.price == null || req.price.compareTo(java.math.BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Giá không hợp lệ.");
            if (req.quantity < 0) throw new IllegalArgumentException("Số lượng không hợp lệ.");

            // 2. Map Request -> Entity
            Product p = new Product();
            p.setName(req.name);
            p.setDescription(req.description);
            
            p.setPrice(req.price.doubleValue()); 
            
            p.setQuantity(req.quantity);
            p.setCategoryId(req.categoryId);
            
            p.setImageUrl(req.image); 

            productRepo.save(p);

            res.success = true;
            res.message = "Thêm sản phẩm thành công!";
            // Nếu repo.save() cập nhật ID lại vào p thì có thể lấy p.getId()
            res.newProductId = p.getId(); 

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }
        
        if (outputBoundary != null) outputBoundary.present(res);
    }
}