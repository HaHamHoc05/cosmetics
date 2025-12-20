// File: DeleteProductUseCase.java
package cosmetic.usecase.product.delete;

import cosmetic.repository.ProductRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class DeleteProductUseCase implements UseCase<DeleteProductReq, DeleteProductRes> {
    private final ProductRepository repository;
    private final OutputBoundary<DeleteProductRes> outputBoundary;

    public DeleteProductUseCase(ProductRepository repository, OutputBoundary<DeleteProductRes> outputBoundary) {
        this.repository = repository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteProductReq req) {
        DeleteProductRes res = new DeleteProductRes();
        try {
            repository.delete(req.id);
            res.isSuccess = true;
            res.message = "Xóa sản phẩm thành công!";
        } catch (Exception e) {
            res.isSuccess = false;
            res.message = "Lỗi xóa sản phẩm: " + e.getMessage();
        }
        outputBoundary.present(res);
    }
}