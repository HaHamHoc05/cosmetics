package adapters.product.productdetail;

import cosmetic.entities.Product;

public class ViewProductDetailViewModel {
    private Product product;
    public void setProduct(Product product){ this.product = product; }
    public Product getProduct(){ return product; }
}
