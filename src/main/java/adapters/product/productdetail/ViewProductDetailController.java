package adapters.product.productdetail;

import adapters.product.viewproducts.ViewProductDetailViewModel;
import cosmetic.repository.ProductRepository;
import cosmetic.usecase.products.productdetail.ViewProductDetailRequest;
import cosmetic.usecase.products.productdetail.ViewProductDetailUseCase;

public class ViewProductDetailController {
	 	private final ViewProductDetailUseCase useCase;
	    private final ViewProductDetailPresenter presenter;

	    public ViewProductDetailController(ProductRepository repo, ViewProductDetailPublisher publisher){
	        this.presenter = new ViewProductDetailPresenter();
	        this.useCase = new ViewProductDetailUseCase(repo, publisher);
	    }

	    public ViewProductDetailViewModel handle(Long productId){
	        ViewProductDetailRequest request = new ViewProductDetailRequest(productId);
	        useCase.execute(request, presenter);
	        return presenter.getViewModel();
	    }
	}