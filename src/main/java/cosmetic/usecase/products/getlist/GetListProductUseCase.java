package cosmetic.usecase.products.getlist;

import java.util.ArrayList;
import java.util.List;

import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetListProductUseCase implements UseCase<GetListProductReq, GetListProductRes> {

    private final ProductRepository productRepo;
    private final OutputBoundary<GetListProductRes> outputBoundary;

    public GetListProductUseCase(ProductRepository productRepo, OutputBoundary<GetListProductRes> outputBoundary) {
        this.productRepo = productRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GetListProductReq req) {
        GetListProductRes res = new GetListProductRes();
        List<Product> entities;

        try {
            // Lấy dữ liệu từ Repo
            if (req.keyword != null && !req.keyword.isEmpty()) {
                entities = productRepo.searchByName(req.keyword);
            } else if (req.categoryId != null) {
                entities = productRepo.findByCategory(req.categoryId);
            } else {
                entities = productRepo.findAll();
            }

            // Convert Entity -> Response DTO
            res.products = new ArrayList<>();
            for (Product p : entities) {
                GetListProductRes.ProductDTO dto = new GetListProductRes.ProductDTO();
                dto.id = p.getId();
                dto.name = p.getName();
                dto.price = p.getPrice();
                dto.imageUrl = p.getImageUrl();
                

                dto.quantity = p.getQuantity();       // Gán số lượng
                dto.description = p.getDescription(); // Gán mô tả
                dto.categoryId = p.getCategoryId();
                
                res.products.add(dto);
            }

            res.success = true;
            res.message = "Tìm thấy " + entities.size() + " sản phẩm.";

        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi: " + e.getMessage();
            e.printStackTrace();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}