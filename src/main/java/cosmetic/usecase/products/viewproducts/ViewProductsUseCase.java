package cosmetic.usecase.products.viewproducts;

import java.util.List;

import adapters.product.viewproducts.ViewProductsPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;

public class ViewProductsUseCase implements UseCase<ViewProductsRequest, ViewProductsResponse> {

    private final ProductRepository productRepository;
    private final ViewProductsPublisher publisher;


    public ViewProductsUseCase(ProductRepository repo, ViewProductsPublisher publisher){
        this.productRepository = repo;
        this.publisher = publisher;
    }
    
    @Override
    public void execute(ViewProductsRequest request, OutputBoundary<ViewProductsResponse> output){
        List<Product> products = productRepository.findAll();
        ViewProductsResponse response = new ViewProductsResponse(products);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}
