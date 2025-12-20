package cosmetic.usecase.product.create;

import cosmetic.usecase.ResponseData;

public class CreateProductRes extends ResponseData {
    public boolean success;
    public String message;
    public Long newProductId;
}