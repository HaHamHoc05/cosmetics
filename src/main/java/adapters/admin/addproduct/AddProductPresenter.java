package adapters.admin.addproduct;

import adapters.Publisher;
import cosmetic.usecase.admin.addproduct.AddProductOutputData;
import cosmetic.usecase.admin.addproduct.AddProductOutputInterface;

public class AddProductPresenter extends Publisher implements AddProductOutputInterface {

    // chứa dữ liệu cho GUI đọc
    private AddProductViewModel viewModel = new AddProductViewModel();

    @Override
    public void present(AddProductOutputData output) {
        // Chuyển dữ liệu từ OutputData sang ViewModel 
        if (output.isSuccess()) {
            viewModel.status = "SUCCESS";
            viewModel.message = output.getMessage();
            viewModel.newProductId = output.getNewProductId();
        } else {
            viewModel.status = "ERROR";
            viewModel.message = output.getMessage();
            viewModel.newProductId = null;
        }

        //  thông báo
        notifySubscribers();
    }

    public AddProductViewModel getViewModel() {
        return viewModel;
    }
}