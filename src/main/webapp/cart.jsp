<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head><title>Giỏ hàng</title></head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container my-5">
        <h2 class="text-center text-uppercase fw-bold mb-4" style="color: #333;">Giỏ hàng của bạn</h2>

        <div class="row">
            <div class="col-lg-8">
                <div class="bg-white p-4 rounded-4 shadow-sm">
                    <c:choose>
                        <c:when test="${empty cartItems}">
                            <div class="text-center py-5">
                                <i class="fa-solid fa-cart-arrow-down fs-1 text-muted mb-3"></i>
                                <p>Giỏ hàng của bạn đang trống.</p>
                                <a href="home" class="btn btn-danger rounded-pill">Tiếp tục mua sắm</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <table class="table align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-center">Số lượng</th>
                                        <th class="text-end">Thành tiền</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <div class="ms-3">
                                                        <h6 class="mb-0 text-dark">${item.productName}</h6>
                                                        <small class="text-muted"><fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/>đ</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                <input type="number" value="${item.quantity}" class="form-control form-control-sm text-center mx-auto" style="width: 60px;" readonly>
                                            </td>
                                            <td class="text-end fw-bold text-danger">
                                                <fmt:formatNumber value="${item.totalPrice}" type="number" groupingUsed="true"/>đ
                                            </td>
                                            <td class="text-end">
                                                <form action="cart" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.productId}">
                                                    <button class="btn btn-sm text-muted"><i class="fa-solid fa-trash"></i></button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <div class="col-lg-4 mt-4 mt-lg-0">
                <div class="bg-white p-4 rounded-4 shadow-sm">
                    <h5 class="fw-bold mb-4">Cộng giỏ hàng</h5>
                    <div class="d-flex justify-content-between mb-3">
                        <span>Tạm tính:</span>
                        <span class="fw-bold"><fmt:formatNumber value="${total}" type="number" groupingUsed="true"/>đ</span>
                    </div>
                    <div class="d-flex justify-content-between mb-4">
                        <span>Phí vận chuyển:</span>
                        <span class="text-success">Miễn phí</span>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between mb-4">
                        <span class="fs-5 fw-bold">Tổng cộng:</span>
                        <span class="fs-4 fw-bold text-danger"><fmt:formatNumber value="${total}" type="currency" currencySymbol="đ"/></span>
                    </div>
                    
                    <a href="checkout.jsp" class="btn btn-danger w-100 py-3 rounded-pill fw-bold text-uppercase" style="background-color: #d81b60; border:none;">
                        Tiến hành thanh toán
                    </a>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>