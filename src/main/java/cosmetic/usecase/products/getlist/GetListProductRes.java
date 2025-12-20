package cosmetic.usecase.products.getlist;

import java.util.List;

import cosmetic.usecase.ResponseData;

public class GetListProductRes extends ResponseData {
    public boolean success;
    public String message;
    public List<ProductDTO> products;

    // DTO con để hiển thị thông tin gọn nhẹ ra màn hình danh sách
    public static class ProductDTO {
        public Long id;
        public String name;
        public double price;
        public String imageUrl;
		public Object quantity;
		public Object description;
    }
}