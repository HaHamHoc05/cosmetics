package adapters.product.viewproducts;

import java.util.List;

import cosmetic.entities.Product;

public class ViewProductDetailViewModel {
    private Product product;
    public void setProduct(Product product){ this.product = product; }
    public Product getProduct(){ return product; }
}
