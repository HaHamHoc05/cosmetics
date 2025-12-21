<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    
    <style>
        /* Tông màu chủ đạo: Hồng Pastel & Hồng đậm */
        :root {
            --primary-color: #d81b60; /* Hồng đậm */
            --bg-pink: #fff0f5;       /* Hồng phấn nhạt */
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
        }

        .navbar-custom {
            background-color: white;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            padding: 15px 0;
        }

        .navbar-brand {
            font-weight: bold;
            color: var(--primary-color) !important;
            font-size: 1.5rem;
            letter-spacing: 1px;
        }

        .nav-link {
            color: #333 !important;
            font-weight: 500;
            margin: 0 10px;
            transition: 0.3s;
        }

        .nav-link:hover {
            color: var(--primary-color) !important;
        }

        .btn-search {
            background-color: var(--primary-color);
            color: white;
            border-radius: 20px;
            padding: 5px 20px;
        }
        
        .btn-search:hover {
            background-color: #ad1457;
            color: white;
        }

        .cart-icon {
            position: relative;
        }

        .cart-badge {
            position: absolute;
            top: -5px;
            right: -8px;
            font-size: 0.7rem;
            background-color: var(--primary-color);
        }
        
        /* Dropdown Admin */
        .dropdown-menu {
            border: none;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
    </style>
</head>

<nav class="navbar navbar-expand-lg navbar-custom sticky-top">
    <div class="container">
        <a class="navbar-brand" href="home">
            <i class="fa-solid fa-spa"></i> BEAUTY SHOP
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <form class="d-flex mx-auto" action="home" method="get" style="width: 40%;">
                <div class="input-group">
                    <input class="form-control rounded-start-pill border-end-0" type="search" name="search" placeholder="Tìm kiếm sản phẩm...">
                    <button class="btn border border-start-0 rounded-end-pill bg-white text-muted" type="submit">
                        <i class="fa-solid fa-magnifying-glass"></i>
                    </button>
                </div>
            </form>

            <ul class="navbar-nav ms-auto align-items-center">
                <li class="nav-item">
                    <a class="nav-link cart-icon" href="cart">
                        <i class="fa-solid fa-bag-shopping fs-5"></i>
                        <c:if test="${not empty sessionScope.cartSize && sessionScope.cartSize > 0}">
                            <span class="badge rounded-pill cart-badge">${sessionScope.cartSize}</span>
                        </c:if>
                    </a>
                </li>

                <c:choose>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <li class="nav-item dropdown ms-2">
                            <a class="nav-link dropdown-toggle d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown">
                                <img src="https://ui-avatars.com/api/?name=${sessionScope.currentUser.fullName}&background=random" class="rounded-circle me-2" width="30">
                                <span>${sessionScope.currentUser.fullName}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="#">Hồ sơ cá nhân</a></li>
                                <li><a class="dropdown-item" href="#">Lịch sử đơn hàng</a></li>
                                
                                <c:if test="${sessionScope.role == 'ADMIN'}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><h6 class="dropdown-header text-danger fw-bold">QUẢN TRỊ</h6></li>
									<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/products">Quản lý Sản phẩm</a></li>
									<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/orders">Quản lý Đơn hàng</a></li>
                                </c:if>
                                
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger" href="logout">Đăng xuất</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item ms-3">
                            <a href="login.jsp" class="btn btn-outline-dark rounded-pill px-4 btn-sm">Đăng nhập</a>
                        </li>
                        <li class="nav-item ms-2">
                            <a href="register.jsp" class="btn btn-search rounded-pill px-4 btn-sm">Đăng ký</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>