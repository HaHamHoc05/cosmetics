package adapters.order.create;

import adapters.Publisher;

public class CreateOrderViewModel extends Publisher{
	// du lieu hien thi ra man hinh
	public String message;
	public String newOrderId;
	public boolean isSuccess;
	
	// reset du lieu cu
	public void clear() {
		this.message = "";
		this.newOrderId = "";
		this.isSuccess = false;
	}

}
