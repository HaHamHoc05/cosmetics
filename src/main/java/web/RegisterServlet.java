package web;

import adapters.BCryptPasswordEncoder;
import adapters.user.register.*;
import cosmetic.usecase.user.register.RegisterUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    public void init() { WebAppContext.init(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String u = req.getParameter("username");
        String p = req.getParameter("password");
        String f = req.getParameter("fullname");

        // Setup Clean Architecture
        RegisterViewModel vm = new RegisterViewModel();
        RegisterPresenter presenter = new RegisterPresenter(vm);
        RegisterUseCase useCase = new RegisterUseCase(
            WebAppContext.getUserRepo(), 
            WebAppContext.getPasswordEncoder(), // Dùng BCrypt
            presenter
        );
        RegisterController controller = new RegisterController(useCase);

        // Gọi Controller
        RegisterController.InputDTO input = new RegisterController.InputDTO();
        input.username = u;
        input.password = p;
        input.fullName = f;
        input.roleId = 2L; // Mặc định là User (Role ID = 2), Admin là 1
        
        controller.execute(input);

        if (vm.isSuccess) {
            // Đăng ký thành công -> Chuyển sang login
            resp.sendRedirect("login.jsp?msg=success");
        } else {
            req.setAttribute("error", vm.message);
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}