<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head><title>Thanh toán</title></head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow-sm border-0 rounded-4">
                    <div class="card-header bg-white border-bottom-0 pt-4 px-4">
                        <h4 class="fw-bold text-danger text-uppercase">Xác nhận thanh toán</h4>
                    </div>
                    <div class="card-body p-4">
                        <div class="alert alert-light border">
                            <strong>Tổng tiền cần thanh toán: </strong> 
                            <span class="text-danger fw-bold fs-5 ms-2">
                                <fmt:formatNumber value="${sessionScope.totalAmount}" type="currency" currencySymbol="đ"/>
                            </span>
                        </div>

                        <form action="checkout" method="post">
                            <div class="mb-3">
                                <label class="form-label fw-bold">Người nhận</label>
                                <input type="text" class="form-control" value="${sessionScope.currentUser.fullName}" readonly style="background-color: #f8f9fa;">
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label fw-bold">Số điện thoại <span class="text-danger">*</span></label>
                                <input type="text" name="phone" class="form-control" required placeholder="Nhập số điện thoại...">
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Địa chỉ giao hàng <span class="text-danger">*</span></label>
                                <textarea name="address" class="form-control" rows="3" required placeholder="Số nhà, đường, phường/xã..."></textarea>
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Phương thức thanh toán</label>
                                <div class="form-check p-3 border rounded mb-2">
                                    <input class="form-check-input" type="radio" name="payment" id="cod" value="COD" checked>
                                    <label class="form-check-label w-100" for="cod">
                                        <i class="fa-solid fa-money-bill-wave text-success me-2"></i> Thanh toán khi nhận hàng (COD)
                                    </label>
                                </div>
                                <div class="form-check p-3 border rounded text-muted bg-light">
                                    <input class="form-check-input" type="radio" name="payment" id="bank" disabled>
                                    <label class="form-check-label" for="bank">
                                        <i class="fa-solid fa-building-columns me-2"></i> Chuyển khoản (Đang bảo trì)
                                    </label>
                                </div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="cart" class="btn btn-outline-secondary rounded-pill px-4">Quay lại giỏ</a>
                                <button type="submit" class="btn btn-danger rounded-pill px-5 fw-bold">ĐẶT HÀNG</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>