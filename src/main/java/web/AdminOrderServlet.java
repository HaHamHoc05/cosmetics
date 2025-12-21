package web;

import adapters.order.getlist.*;
import cosmetic.usecase.order.getlist.GetListUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

// Ánh xạ URL này khớp với href trong header.jsp
@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    
    @Override
    public void init() {
        WebAppContext.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Kiểm tra quyền Admin
        HttpSession session = req.getSession();
        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equals("ADMIN")) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // 2. Lấy danh sách đơn hàng
        GetListViewModel vm = new GetListViewModel();
        GetListPresenter presenter = new GetListPresenter(vm);
        // Lưu ý: Kiểm tra lại constructor GetListUseCase của bạn
        GetListUseCase useCase = new GetListUseCase(WebAppContext.getOrderRepo(), presenter);
        GetListController controller = new GetListController(useCase);

        controller.execute(null); // Lấy tất cả đơn hàng

        // 3. Gửi sang JSP
        req.setAttribute("orders", vm.orders);
        req.getRequestDispatcher("/manage-orders.jsp").forward(req, resp);
    }
}