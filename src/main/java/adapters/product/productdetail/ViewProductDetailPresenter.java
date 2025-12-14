package adapters.product.productdetail;

import adapters.product.viewproducts.ViewProductDetailViewModel;
import cosmetic.entities.Product;
import cosmetic.usecase.products.productdetail.ViewProductDetailOutputBoundary;
import cosmetic.usecase.products.productdetail.ViewProductDetailResponse;

public class ViewProductDetailPresenter implements ViewProductDetailOutputBoundary {

	private final ViewProductDetailViewModel vm = new ViewProductDetailViewModel();
    @Override
    public void present(ViewProductDetailResponse response){ vm.setProduct(response.getProduct()); }
    public ViewProductDetailViewModel getViewModel(){ return vm; }
}
