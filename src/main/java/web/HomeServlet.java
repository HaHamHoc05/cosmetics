package web;

import adapters.product.getlist.*;
import cosmetic.usecase.products.getlist.GetListProductUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    @Override
    public void init() {
        WebAppContext.init(); // Khởi tạo kết nối DB khi server bật
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Cấu hình Adapter & UseCase
        GetListProductViewModel vm = new GetListProductViewModel();
        GetListProductPresenter presenter = new GetListProductPresenter(vm);
        GetListProductUseCase useCase = new GetListProductUseCase(WebAppContext.getProductRepo(), presenter);
        GetListProductController controller = new GetListProductController(useCase);

        // Lấy từ khóa tìm kiếm
        String keyword = req.getParameter("search");
        if (keyword == null) keyword = "";

        // Thực thi
        controller.execute(keyword, null);

        // Gửi dữ liệu sang JSP
        req.setAttribute("products", vm.products);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}