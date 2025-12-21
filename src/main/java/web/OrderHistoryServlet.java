package web;

import adapters.order.getlist.*;
import cosmetic.usecase.order.getlist.GetListUseCase;
import cosmetic.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/history")
public class OrderHistoryServlet extends HttpServlet {
    @Override
    public void init() { WebAppContext.init(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("currentUser");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        GetListViewModel vm = new GetListViewModel();
        GetListPresenter presenter = new GetListPresenter(vm);
        GetListUseCase useCase = new GetListUseCase(WebAppContext.getOrderRepo(), presenter);
        GetListController controller = new GetListController(useCase);

        // Lấy đơn hàng của user hiện tại
        controller.execute(user.getId());

        req.setAttribute("orders", vm.orders);
        req.getRequestDispatcher("history.jsp").forward(req, resp);
    }
}