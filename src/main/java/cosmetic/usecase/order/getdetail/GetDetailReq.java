package cosmetic.usecase.order.getdetail;

import java.security.PublicKey;

import cosmetic.entities.Role;
import cosmetic.usecase.RequestData;

public class GetDetailReq extends RequestData{
	public Long orderId;
	public Long userId;
	public Role role;
	
	public GetDetailReq(Long orderId, Long userId) {
		this.orderId = orderId;
		this.userId = userId;
	}

}
