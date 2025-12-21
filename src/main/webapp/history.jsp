<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head><title>Lịch sử mua hàng</title></head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container my-5">
        <h3 class="fw-bold mb-4 text-uppercase text-secondary">Lịch sử đơn hàng</h3>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success rounded-4 mb-4">
                <i class="fa-solid fa-check-circle me-2"></i> Đặt hàng thành công! Cảm ơn bạn đã ủng hộ.
            </div>
        </c:if>

        <c:choose>
            <c:when test="${empty orders}">
                <div class="text-center py-5 bg-white rounded-4 shadow-sm">
                    <img src="https://cdn-icons-png.flaticon.com/512/4076/4076432.png" width="100" class="mb-3 opacity-50">
                    <p class="text-muted">Bạn chưa có đơn hàng nào.</p>
                    <a href="home" class="btn btn-outline-danger rounded-pill">Mua sắm ngay</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <c:forEach var="o" items="${orders}">
                        <div class="col-12 mb-3">
                            <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
                                <div class="card-header bg-white d-flex justify-content-between align-items-center py-3">
                                    <div>
                                        <span class="fw-bold text-dark">#DH${o.id}</span>
                                        <span class="text-muted small ms-2"><i class="fa-regular fa-clock"></i> ${o.createdAt}</span>
                                    </div>
                                    <span class="badge bg-${o.status == 'PENDING' ? 'warning text-dark' : (o.status == 'COMPLETED' ? 'success' : 'secondary')} rounded-pill px-3">
                                        ${o.status}
                                    </span>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <p class="mb-1"><i class="fa-solid fa-location-dot text-danger me-2"></i> ${o.shippingAddress}</p>
                                            <p class="mb-0 text-muted small">Thanh toán: ${o.paymentMethod}</p>
                                        </div>
                                        <div class="col-md-4 text-md-end mt-3 mt-md-0">
                                            <p class="small text-muted mb-1">Tổng tiền</p>
                                            <h5 class="text-danger fw-bold"><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="đ"/></h5>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>