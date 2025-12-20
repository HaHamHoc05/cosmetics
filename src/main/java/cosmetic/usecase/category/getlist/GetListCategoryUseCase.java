package cosmetic.usecase.category.getlist;

import cosmetic.repository.CategoryRepository;
import cosmetic.usecase.OutputBoundary;
import cosmetic.usecase.UseCase;

public class GetListCategoryUseCase implements UseCase<GetListCategoryReq, GetListCategoryRes> {

    private final CategoryRepository categoryRepo;
    private final OutputBoundary<GetListCategoryRes> outputBoundary;

    public GetListCategoryUseCase(CategoryRepository categoryRepo, OutputBoundary<GetListCategoryRes> outputBoundary) {
        this.categoryRepo = categoryRepo;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(GetListCategoryReq req) {
        GetListCategoryRes res = new GetListCategoryRes();
        try {
            res.categories = categoryRepo.findAll();
            res.success = true;
        } catch (Exception e) {
            res.success = false;
            res.message = "Lỗi tải danh mục: " + e.getMessage();
        }

        if (outputBoundary != null) {
            outputBoundary.present(res);
        }
    }
}