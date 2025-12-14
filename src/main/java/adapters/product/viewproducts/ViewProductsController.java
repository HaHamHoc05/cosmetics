package adapters.product.viewproducts;

import cosmetic.repository.ProductRepository;
import cosmetic.usecase.products.viewproducts.ViewProductsRequest;
import cosmetic.usecase.products.viewproducts.ViewProductsUseCase;

public class ViewProductsController {
	private final ViewProductsUseCase useCase;
    private final ViewProductsPresenter presenter;

    public ViewProductsController(ProductRepository repo, ViewProductsPublisher publisher){
        this.presenter = new ViewProductsPresenter();
        this.useCase = new ViewProductsUseCase(repo, publisher);
    }

    public ViewProductsViewModel handle(){
        ViewProductsRequest request = new ViewProductsRequest();
        useCase.execute(request, presenter);
        return presenter.getViewModel();
    }
}