package cosmetic.usecase.product.create;

import cosmetic.usecase.RequestData;
import java.math.BigDecimal;

public class CreateProductReq extends RequestData {
    public String name;
    public String description;
    public BigDecimal price;
    public int quantity;
    public Long categoryId;
    public String image;
}