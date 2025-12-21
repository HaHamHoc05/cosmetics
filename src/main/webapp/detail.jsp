<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head><title>Chi tiết sản phẩm</title></head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container my-5">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="home" class="text-decoration-none text-muted">Trang chủ</a></li>
                <li class="breadcrumb-item active" aria-current="page">${p.name}</li>
            </ol>
        </nav>

        <div class="row bg-white p-4 rounded-4 shadow-sm">
            <div class="col-md-5 text-center mb-4 mb-md-0">
                <div class="border rounded-4 p-3 d-flex align-items-center justify-content-center" style="height: 400px; background: #fff;">
                    <img src="${not empty p.imageUrl ? p.imageUrl : 'https://via.placeholder.com/400'}" class="img-fluid" style="max-height: 100%;" alt="${p.name}">
                </div>
            </div>

            <div class="col-md-7 ps-md-5">
                <h2 class="fw-bold text-dark">${p.name}</h2>
                <div class="mb-3">
                    <span class="text-warning"><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star-half-stroke"></i></span>
                    <span class="text-muted ms-2">(Xem 204 đánh giá)</span>
                </div>

                <h3 class="text-danger fw-bold mb-3" style="font-size: 2rem;">
                    <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="đ"/>
                </h3>

                <p class="text-muted" style="line-height: 1.6;">${p.description}</p>

                <hr class="my-4">

                <form action="cart" method="post">
                    <input type="hidden" name="productId" value="${p.id}">
                    
                    <div class="d-flex align-items-center mb-4">
                        <span class="fw-bold me-3">Số lượng:</span>
                        <input type="number" name="quantity" class="form-control text-center" value="1" min="1" max="${p.quantity}" style="width: 80px;">
                        <span class="text-muted ms-3 small">(${p.quantity} sản phẩm có sẵn)</span>
                    </div>

                    <div class="d-flex gap-3">
                        <button type="submit" class="btn btn-outline-danger flex-grow-1 py-2 rounded-pill fw-bold">
                            <i class="fa-solid fa-cart-plus"></i> THÊM VÀO GIỎ
                        </button>
                        <button type="submit" class="btn btn-danger flex-grow-1 py-2 rounded-pill fw-bold" style="background-color: #d81b60; border: none;">
                            MUA NGAY
                        </button>
                    </div>
                </form>

                <div class="row mt-4 g-3">
                    <div class="col-6">
                        <div class="d-flex align-items-center p-2 rounded bg-light">
                            <i class="fa-solid fa-truck-fast text-danger fs-4 me-3"></i>
                            <span class="small">Giao hàng miễn phí<br>cho đơn từ 300k</span>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="d-flex align-items-center p-2 rounded bg-light">
                            <i class="fa-solid fa-shield-halved text-danger fs-4 me-3"></i>
                            <span class="small">Cam kết chính hãng<br>hoàn tiền 200%</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>