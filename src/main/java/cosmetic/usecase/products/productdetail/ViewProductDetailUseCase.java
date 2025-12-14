package cosmetic.usecase.products.productdetail;

import adapters.product.productdetail.ViewProductDetailPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;

public class ViewProductDetailUseCase implements UseCase<ViewProductDetailRequest, ViewProductDetailResponse> {

    private final ProductRepository productRepository;
    private final ViewProductDetailPublisher publisher;

    public ViewProductDetailUseCase(ProductRepository productRepository, ViewProductDetailPublisher publisher) {
        this.productRepository = productRepository;
        this.publisher = publisher;
    }

    @Override
    public void execute(ViewProductDetailRequest request, OutputBoundary<ViewProductDetailResponse> output) {
        Product product = productRepository.findById(request.getProductId());

        // Business rule cơ bản: kiểm tra sản phẩm tồn tại
        if (product == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        ViewProductDetailResponse response = new ViewProductDetailResponse(product);
        output.present(response);        // Gọi presenter
        publisher.notifySubscribers(response);  // Gửi thông báo tới Subscriber chung
    }
}