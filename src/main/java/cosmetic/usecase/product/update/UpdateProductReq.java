package cosmetic.usecase.product.update;
import cosmetic.usecase.RequestData;

public class UpdateProductReq extends RequestData {
    public Long id;
    public String name;
    public double price;
    public int quantity;
    public String description;
    public String imageUrl;
    public Long categoryId;
}

