<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Đăng ký tài khoản</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: linear-gradient(135deg, #fff0f5 0%, #ffe4e1 100%); height: 100vh; display: flex; align-items: center; justify-content: center; }
        .card { border: none; border-radius: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); width: 450px; }
        .form-control { border-radius: 25px; padding: 10px 20px; background: #f8f9fa; border: 1px solid #eee; }
        .form-control:focus { box-shadow: none; border-color: #d81b60; }
        .btn-register { border-radius: 25px; padding: 10px; background-color: #d81b60; border: none; font-weight: bold; width: 100%; color: white; }
        .btn-register:hover { background-color: #ad1457; color: white; }
    </style>
</head>
<body>
    <div class="card p-4">
        <h3 class="text-center text-uppercase fw-bold mb-4" style="color: #333;">Đăng Ký</h3>
        
        <c:if test="${not empty error}">
            <div class="alert alert-danger rounded-pill py-2 small text-center">${error}</div>
        </c:if>

        <form action="register" method="post">
            <div class="mb-3">
                <input type="text" name="username" class="form-control" placeholder="Tên đăng nhập" required>
            </div>
            <div class="mb-3">
                <input type="password" name="password" class="form-control" placeholder="Mật khẩu" required>
            </div>
            <div class="mb-3">
                <input type="text" name="fullname" class="form-control" placeholder="Họ và tên đầy đủ" required>
            </div>
            <button type="submit" class="btn btn-register mt-2">TẠO TÀI KHOẢN</button>
        </form>
        
        <div class="text-center mt-4 small">
            Đã có tài khoản? <a href="login.jsp" class="text-decoration-none fw-bold text-danger">Đăng nhập ngay</a>
            <br>
            <a href="home" class="text-muted mt-2 d-inline-block">Về trang chủ</a>
        </div>
    </div>
</body>
</html>