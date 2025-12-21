package web;

import adapters.order.create.*;
import cosmetic.usecase.order.create.CreateOrderUseCase;
import cosmetic.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    @Override
    public void init() { WebAppContext.init(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Chỉ hiển thị form checkout
        req.getRequestDispatcher("checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("currentUser");
        
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String payment = req.getParameter("payment");

        // Setup Clean Architecture
        CreateOrderViewModel vm = new CreateOrderViewModel();
        CreateOrderPresenter presenter = new CreateOrderPresenter(vm);
        CreateOrderUseCase useCase = new CreateOrderUseCase(
            WebAppContext.getOrderRepo(),
            WebAppContext.getCartRepo(),
            WebAppContext.getProductRepo(),
            presenter
        );
        CreateOrderController controller = new CreateOrderController(useCase);

        // Tạo InputDTO
        CreateOrderController.InputDTO input = new CreateOrderController.InputDTO();
        input.userId = user.getId();
        input.address = address;
        input.phone = phone;
        input.paymentMethod = payment;

        controller.execute(input);

        if (vm.isSuccess) {
            // Reset số lượng giỏ hàng trên menu
            req.getSession().setAttribute("cartSize", 0);
            // Chuyển hướng đến trang lịch sử hoặc thông báo thành công
            resp.sendRedirect("history?msg=success");
        } else {
            req.setAttribute("error", vm.message);
            req.getRequestDispatcher("checkout.jsp").forward(req, resp);
        }
    }
}