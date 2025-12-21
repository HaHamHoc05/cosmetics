package web;

import adapters.cart.add.*;
import adapters.cart.view.*;
import cosmetic.usecase.cart.add.AddToCartUseCase;
import cosmetic.usecase.cart.view.ViewCartUseCase;
import cosmetic.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    @Override
    public void init() { WebAppContext.init(); }

    // Xem giỏ hàng
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        ViewCartViewModel vm = new ViewCartViewModel();
        ViewCartPresenter presenter = new ViewCartPresenter(vm);
        ViewCartUseCase useCase = new ViewCartUseCase(WebAppContext.getCartRepo(), presenter);
        ViewCartController controller = new ViewCartController(useCase);

        controller.execute(user.getId());

        req.setAttribute("cartItems", vm.items);
        req.setAttribute("total", vm.grandTotal);
        req.getRequestDispatcher("cart.jsp").forward(req, resp);
    }

    // Thêm vào giỏ hàng
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            Long pId = Long.parseLong(req.getParameter("productId"));
            // Mặc định là 1 nếu không truyền số lượng
            String qtyStr = req.getParameter("quantity");
            int qty = (qtyStr != null) ? Integer.parseInt(qtyStr) : 1;

            AddToCartViewModel vm = new AddToCartViewModel();
            AddToCartPresenter presenter = new AddToCartPresenter(vm);
            AddToCartUseCase useCase = new AddToCartUseCase(WebAppContext.getCartRepo(), WebAppContext.getProductRepo(), presenter);
            AddToCartController controller = new AddToCartController(useCase);

            AddToCartController.InputDTO input = new AddToCartController.InputDTO();
            input.userId = user.getId();
            input.productId = pId;
            input.quantity = qty;

            controller.execute(input);
            resp.sendRedirect("cart");

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("home");
        }
    }
}