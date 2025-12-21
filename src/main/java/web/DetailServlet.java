package web;

import adapters.product.getdetail.*;
import cosmetic.usecase.products.getdetail.GetDetailProductUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// 1. Ánh xạ Servlet này với đường dẫn "/detail"
@WebServlet("/detail")
public class DetailServlet extends HttpServlet {
    
    @Override
    public void init() {
        // Đảm bảo kết nối DB đã sẵn sàng
        WebAppContext.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 2. Lấy ID sản phẩm từ URL (ví dụ: detail?id=10 -> lấy số 10)
            String idParam = req.getParameter("id");
            Long productId = Long.parseLong(idParam);

            // 3. Cấu hình theo kiến trúc Clean Architecture (giống HomeServlet)
            GetDetailProductViewModel vm = new GetDetailProductViewModel();
            GetDetailProductPresenter presenter = new GetDetailProductPresenter(vm);
            
            // Lưu ý: Kiểm tra lại constructor của GetDetailProductUseCase trong code của bạn 
            // để truyền tham số cho đúng (thường là Repository và Presenter)
            GetDetailProductUseCase useCase = new GetDetailProductUseCase(WebAppContext.getProductRepo(), presenter);
            
            GetDetailProductController controller = new GetDetailProductController(useCase);

            // 4. Thực thi logic lấy chi tiết
            controller.execute(productId);

            // 5. Kiểm tra kết quả và gửi sang JSP
            if (vm.isSuccess) {
                // Đặt tên biến là "p" để khớp với ${p.name}, ${p.price}... trong detail.jsp
                req.setAttribute("p", vm.productDetail);
                req.getRequestDispatcher("detail.jsp").forward(req, resp);
            } else {
                // Trường hợp không tìm thấy sản phẩm, có thể quay về trang chủ hoặc báo lỗi
                resp.sendRedirect("home");
            }
        } catch (NumberFormatException | NullPointerException e) {
            // Nếu ID không hợp lệ hoặc lỗi, quay về trang chủ
            resp.sendRedirect("home");
        }
    }
}