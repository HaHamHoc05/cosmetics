package adapters.order.create;

import java.security.PublicKey;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.order.create.CreateOrderRes;

public class CreateOrderPresenter implements OutputBoundary<CreateOrderRes>{
	
	private final CreateOrderViewModel viewModel;
	
	// dua viewmodel vao presenter
	public CreateOrderPresenter(CreateOrderViewModel viewModel) {
		this.viewModel = viewModel;
	}
	
	@Override
	public void present(CreateOrderRes response) {
		// nhan du lieu tu uc
		viewModel.isSuccess = response.success;
		viewModel.message = response.message;
		
		if (response.success & response.newOrderId != null) {
			viewModel.newOrderId = String.valueOf(response.newOrderId);
			
		} else {
			viewModel.newOrderId ="";
			
		}
		
		//bao cho view biet
		viewModel.notifySubscribers();
	}

}
