<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đăng nhập</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        body { background: linear-gradient(135deg, #fff0f5 0%, #ffe4e1 100%); height: 100vh; display: flex; align-items: center; justify-content: center; }
        .login-card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); width: 400px; padding: 2rem; background: white; }
        .form-control { border-radius: 25px; padding: 10px 20px; background: #f8f9fa; border: 1px solid #eee; }
        .form-control:focus { box-shadow: none; border-color: #d81b60; }
        .btn-login { border-radius: 25px; padding: 10px; background-color: #d81b60; border: none; font-weight: bold; width: 100%; color: white; margin-top: 10px; }
        .btn-login:hover { background-color: #ad1457; color: white; }
    </style>
</head>
<body>
    <div class="login-card text-center">
        <div class="mb-4">
            <i class="fa-solid fa-spa text-danger" style="font-size: 3rem;"></i>
            <h4 class="fw-bold mt-2" style="color: #333;">ĐĂNG NHẬP</h4>
            <p class="text-muted small">Chào mừng bạn quay trở lại!</p>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-danger rounded-pill py-2 small">${error}</div>
        </c:if>

        <form action="login" method="post">
            <div class="mb-3 text-start">
                <input type="text" name="username" class="form-control" placeholder="Tên đăng nhập" required>
            </div>
            <div class="mb-3 text-start">
                <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
            </div>
            <button type="submit" class="btn btn-login">ĐĂNG NHẬP</button>
        </form>

        <div class="mt-4 small">
            <a href="home" class="text-decoration-none text-muted me-3">Về trang chủ</a>
            <span>Chưa có tài khoản? <a href="register.jsp" class="text-danger fw-bold text-decoration-none">Đăng ký ngay</a></span>
        </div>
    </div>
</body>
</html>