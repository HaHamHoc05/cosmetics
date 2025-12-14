package adapters.order.checkout;

import cosmetic.usecase.order.checkout.CheckoutOutputBoundary;
import cosmetic.usecase.order.checkout.CheckoutResponse;

public class CheckoutPresenter implements CheckoutOutputBoundary {
    private CheckoutViewModel viewModel;
    @Override
    public void present(CheckoutResponse response){
        this.viewModel = new CheckoutViewModel(response.getOrder());
    }
    public CheckoutViewModel getViewModel(){ return viewModel; }
}
