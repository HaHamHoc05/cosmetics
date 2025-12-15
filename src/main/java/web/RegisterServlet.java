package web;

import adapters.user.register.RegisterPresenter;
import adapters.user.register.RegisterPublisher;
import adapters.user.register.RegisterViewModel;
import cosmetic.repository.UserRepository;
import cosmetic.repository.impl.MySQLUserRepository;
import cosmetic.usecase.user.register.RegisterRequest;
import cosmetic.usecase.user.register.RegisterResponse;
import cosmetic.usecase.user.register.RegisterUseCase;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        // Khởi tạo DB connection
        this.userRepository = new MySQLUserRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Clean Architecture Wiring
        RegisterPresenter presenter = new RegisterPresenter();
        
        RegisterPublisher publisher = new RegisterPublisher() {
            @Override
            public void notifySubscribers(RegisterResponse response) {
                // Dummy publisher
            }
        };

        RegisterUseCase useCase = new RegisterUseCase(userRepository, publisher);

        try {
            RegisterRequest request = new RegisterRequest(username, password, email);
            useCase.execute(request, presenter);

            // Giả sử Presenter có hàm getViewModel()
            RegisterViewModel viewModel = presenter.getViewModel(); 

            // Logic kiểm tra ViewModel (cần tự implement trong ViewModel)
            // Ví dụ: if (viewModel.errors == null || viewModel.errors.isEmpty())
            
            // Tạm thời redirect để test
            resp.sendRedirect(req.getContextPath() + "/login?success=true");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/views/register.jsp").forward(req, resp);
        }
    }
}