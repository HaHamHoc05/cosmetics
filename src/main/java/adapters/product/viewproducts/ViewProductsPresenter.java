package adapters.product.viewproducts;

import java.util.List;
import java.util.stream.Collectors;

import cosmetic.usecase.products.viewproducts.ViewProductsOutputBoundary;
import cosmetic.usecase.products.viewproducts.ViewProductsResponse;

public class ViewProductsPresenter implements ViewProductsOutputBoundary {
    private final ViewProductsViewModel vm = new ViewProductsViewModel();
    @Override
    public void present(ViewProductsResponse response){
        vm.setProducts(response.getProducts());
    }
    public ViewProductsViewModel getViewModel(){ return vm; }
}
