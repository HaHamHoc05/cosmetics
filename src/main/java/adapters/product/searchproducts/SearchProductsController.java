package adapters.product.searchproducts;

import cosmetic.repository.ProductRepository;
import cosmetic.usecase.products.searchproducts.SearchProductsRequest;
import cosmetic.usecase.products.searchproducts.SearchProductsUseCase;

public class SearchProductsController {
	private final SearchProductsUseCase useCase;
    private final SearchProductsPresenter presenter;

    public SearchProductsController(ProductRepository repo, SearchProductsPublisher publisher){
        this.presenter = new SearchProductsPresenter();
        this.useCase = new SearchProductsUseCase(repo, publisher);
    }

    public SearchProductsViewModel handle(String keyword, Long categoryId){
        SearchProductsRequest request = new SearchProductsRequest(keyword, categoryId);
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}