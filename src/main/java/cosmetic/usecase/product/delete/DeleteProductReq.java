package cosmetic.usecase.product.delete;
import cosmetic.usecase.RequestData;

public class DeleteProductReq extends RequestData {
    public Long id;
    public DeleteProductReq(Long id) { this.id = id; }
}

