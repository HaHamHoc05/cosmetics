package cosmetic.usecase.products.getdetail;

import cosmetic.usecase.ResponseData;

public class GetDetailProductRes extends ResponseData {
    public boolean success;
    public String message;
    
    // Chi tiết đầy đủ
    public Long id;
    public String name;
    public double price;
    public int quantity; 
    public String description;
    public String imageUrl;
}