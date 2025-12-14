package adapters.product.searchproducts;

import cosmetic.usecase.products.searchproducts.SearchProductsOutputBoundary;
import cosmetic.usecase.products.searchproducts.SearchProductsResponse;

public class SearchProductsPresenter implements SearchProductsOutputBoundary {
    private SearchProductsViewModel viewModel;

    @Override
    public void present(SearchProductsResponse response){
        this.viewModel = new SearchProductsViewModel(response.getProducts());
    }

    public SearchProductsViewModel getViewModel() { return viewModel; }
}