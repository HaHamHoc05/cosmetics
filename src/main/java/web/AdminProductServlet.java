package web;

import adapters.product.getlist.*;
import cosmetic.usecase.products.getlist.GetListProductUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Ánh xạ URL này khớp với href trong header.jsp
@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    
    @Override
    public void init() {
        WebAppContext.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Kiểm tra quyền Admin (Bảo mật)
        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equals("ADMIN")) {
            resp.sendRedirect(req.getContextPath() + "/home"); // Đuổi về trang chủ nếu không phải admin
            return;
        }

        // 2. Lấy danh sách sản phẩm để hiển thị cho Admin quản lý
        GetListProductViewModel vm = new GetListProductViewModel();
        GetListProductPresenter presenter = new GetListProductPresenter(vm);
        GetListProductUseCase useCase = new GetListProductUseCase(WebAppContext.getProductRepo(), presenter);
        GetListProductController controller = new GetListProductController(useCase);

        controller.execute("", null); // Lấy tất cả, không lọc

        // 3. Gửi dữ liệu sang trang JSP quản trị (bạn cần tạo file này)
        req.setAttribute("products", vm.products);
        
        // Lưu ý: Tạo thư mục "admin" trong webapp để chứa các file JSP quản trị
        req.getRequestDispatcher("/manage-products.jsp").forward(req, resp);
    }
}