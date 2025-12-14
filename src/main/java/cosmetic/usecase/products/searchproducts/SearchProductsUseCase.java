package cosmetic.usecase.products.searchproducts;

import java.util.List;
import java.util.stream.Collectors;

import adapters.product.searchproducts.SearchProductsPublisher;
import cosmetic.OutputBoundary;
import cosmetic.UseCase;
import cosmetic.entities.Product;
import cosmetic.repository.ProductRepository;

public class SearchProductsUseCase implements UseCase<SearchProductsRequest, SearchProductsResponse> {

    private final ProductRepository productRepository;
    private final SearchProductsPublisher publisher;

    public SearchProductsUseCase(ProductRepository repo, SearchProductsPublisher publisher){
        this.productRepository = repo;
        this.publisher = publisher;
    }

    @Override
    public void execute(SearchProductsRequest request, OutputBoundary<SearchProductsResponse> output){
        List<Product> products = productRepository.search(request.getKeyword(), request.getCategoryId());
        SearchProductsResponse response = new SearchProductsResponse(products);
        output.present(response);
        publisher.notifySubscribers(response);
    }
}