<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Trang chủ - Beauty Shop</title>
    <style>
        .product-card {
            border: none;
            border-radius: 15px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            background: white;
            overflow: hidden;
        }
        .product-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .product-img-container {
            height: 250px;
            padding: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: white;
        }
        .product-img {
            max-height: 100%;
            max-width: 100%;
            object-fit: contain;
        }
        .price-tag {
            color: #d81b60;
            font-weight: bold;
            font-size: 1.1rem;
        }
        .btn-add-cart {
            background-color: #fff0f5;
            color: #d81b60;
            border: none;
            width: 100%;
            font-weight: 600;
            transition: 0.3s;
        }
        .btn-add-cart:hover {
            background-color: #d81b60;
            color: white;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-4">
        <div class="rounded-4 overflow-hidden shadow-sm position-relative">
            <img src="https://via.placeholder.com/1200x350/ffc1e3/d81b60?text=SALE+MÙA+HÈ+-+GIẢM+TỚI+50%" class="w-100" alt="Banner">
            <div class="position-absolute top-50 start-50 translate-middle text-center text-white">
                <h1 class="fw-bold text-shadow">RẠNG NGỜI VẺ ĐẸP VIỆT</h1>
                <a href="#products" class="btn btn-light rounded-pill px-4 mt-2 fw-bold text-danger">MUA NGAY</a>
            </div>
        </div>
    </div>

    <div class="container my-5" id="products">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-dark border-start border-4 border-danger ps-3">SẢN PHẨM MỚI</h3>
        </div>

        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4 g-4">
            <c:forEach var="p" items="${products}">
                <div class="col">
                    <div class="card h-100 product-card">
                        <a href="detail?id=${p.id}" class="text-decoration-none">
                            <div class="product-img-container">
                                <img src="${not empty p.imageUrl ? p.imageUrl : 'https://via.placeholder.com/200'}" class="product-img" alt="${p.name}">
                            </div>
                        </a>
                        <div class="card-body">
                            <h6 class="card-title text-truncate mb-1">
                                <a href="detail?id=${p.id}" class="text-decoration-none text-dark" title="${p.name}">${p.name}</a>
                            </h6>
                            <div class="mb-2">
                                <small class="text-warning"><i class="fa-solid fa-star"></i> 4.8</small>
                                <small class="text-muted ms-2">Đã bán ${p.quantity * 3 + 10}</small> </div>
                            <div class="d-flex justify-content-between align-items-center">
								<span class="price-tag"><fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/>đ</span>
                            </div>
                        </div>
                        <div class="card-footer bg-white border-top-0 p-3 pt-0">
                            <form action="cart" method="post">
                                <input type="hidden" name="productId" value="${p.id}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-add-cart rounded-pill py-2">
                                    <i class="fa-solid fa-cart-plus"></i> Thêm vào giỏ
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>