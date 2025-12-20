package adapters.category.getlist;

import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.category.getlist.GetListCategoryRes;

public class GetListCategoryPresenter implements OutputBoundary<GetListCategoryRes> {
    private final GetListCategoryViewModel viewModel;

    public GetListCategoryPresenter(GetListCategoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetListCategoryRes res) {
        viewModel.setState(res.success, res.message, res.categories);
    }
}