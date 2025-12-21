package web;

import adapters.user.login.*;
import cosmetic.usecase.user.login.LoginUseCase;
import cosmetic.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    public void init() { WebAppContext.init(); }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String u = req.getParameter("username");
        String p = req.getParameter("password");

        LoginViewModel vm = new LoginViewModel();
        LoginPresenter presenter = new LoginPresenter(vm);
        LoginUseCase useCase = new LoginUseCase(WebAppContext.getUserRepo(), WebAppContext.getPasswordEncoder(), presenter);
        LoginController controller = new LoginController(useCase);

        controller.execute(u, p);

        if (vm.isSuccess) {
            // Lấy thông tin user đầy đủ
            User user = WebAppContext.getUserRepo().findByUsername(u);
            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);
            session.setAttribute("role", vm.role);

            // Điều hướng dựa trên quyền
            if ("ADMIN".equalsIgnoreCase(vm.role)) {
                // Nếu bạn chưa làm trang admin, cho về home nhưng hiện menu admin
                resp.sendRedirect("home"); 
            } else {
                resp.sendRedirect("home");
            }
        } else {
            req.setAttribute("error", vm.message);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}