package cosmetic.usecase.order.getlist;

import cosmetic.usecase.RequestData;

public class GetListReq extends RequestData{
	// neu userid =! null lay danh sach cuar user do 
	// neu userid == null lay tat ca cho admin xem
	public Long userId;
	
	public GetListReq(Long userId) {
		this.userId = userId;
	}
}
