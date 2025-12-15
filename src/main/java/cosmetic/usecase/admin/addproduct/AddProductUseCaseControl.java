package cosmetic.usecase.admin.addproduct;

import cosmetic.entities.Product;
import cosmetic.entities.User;
import cosmetic.repository.ProductRepository;
import cosmetic.repository.UserRepository;

public class AddProductUseCaseControl implements AddProductInputInterface {
    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final AddProductOutputInterface outputInterface;

    public AddProductUseCaseControl(ProductRepository pRepo, UserRepository uRepo, AddProductOutputInterface out) {
        this.productRepo = pRepo;
        this.userRepo = uRepo;
        this.outputInterface = out;
    }

    @Override
    public void execute(AddProductInputData input) {
        try {
            //Kiểm tra quyền Admin
            User requester = userRepo.findById(input.getRequesterId());
            if (requester == null || !requester.isAdmin()) {
                outputInterface.present(new AddProductOutputData("Truy cập bị từ chối! Bạn không phải Admin."));
                return;
            }

            // Tạo Entity
            Product newProduct = new Product(
                null, // ID null để DB tự sinh
                input.getName(),
                input.getPrice(),
                input.getStockQuantity(),
                input.getCategoryId()
            );
            newProduct.setDescription(input.getDescription());
            newProduct.setImage(input.getImage());

            // Lưu xuống DB
            productRepo.save(newProduct);

            // Báo thành công
            outputInterface.present(new AddProductOutputData(newProduct.getId(), "Thêm sản phẩm thành công!"));

        } catch (Exception e) {
            outputInterface.present(new AddProductOutputData("Lỗi: " + e.getMessage()));
        }
    }
}
