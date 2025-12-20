package cosmetic.usecase.products.getlist;

import cosmetic.usecase.RequestData;

public class GetListProductReq extends RequestData {
    public Long categoryId; 
    public String keyword;  
}