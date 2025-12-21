<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Đơn hàng - Admin</title>
</head>
<body>
    <jsp:include page="/header.jsp" />

    <div class="container my-5">
        <h2 class="fw-bold text-danger border-start border-4 border-danger ps-3 mb-4">QUẢN LÝ ĐƠN HÀNG</h2>

        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light text-secondary small text-uppercase">
                        <tr>
                            <th class="ps-4 py-3">Mã Đơn</th>
                            <th>Khách hàng</th>
                            <th>Ngày đặt</th>
                            <th>Tổng tiền</th>
                            <th>Trạng thái</th>
                            <th class="text-end pe-4">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td class="ps-4 fw-bold text-muted">#${o.id}</td>
                                <td class="fw-bold text-dark">${o.customerName}</td>
                                <td class="text-muted small">${o.createdAt}</td>
                                <td class="fw-bold text-danger">
                                    <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="đ"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${o.status == 'NEW'}">
                                            <span class="badge bg-primary rounded-pill">Mới</span>
                                        </c:when>
                                        <c:when test="${o.status == 'SHIPPING'}">
                                            <span class="badge bg-warning text-dark rounded-pill">Đang giao</span>
                                        </c:when>
                                        <c:when test="${o.status == 'COMPLETED'}">
                                            <span class="badge bg-success rounded-pill">Hoàn thành</span>
                                        </c:when>
                                        <c:when test="${o.status == 'CANCELLED'}">
                                            <span class="badge bg-secondary rounded-pill">Đã hủy</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-light text-dark border rounded-pill">${o.status}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end pe-4">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-sm btn-outline-danger dropdown-toggle rounded-pill px-3" data-bs-toggle="dropdown">
                                            Xử lý
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-end shadow border-0">
                                            <li><h6 class="dropdown-header">Cập nhật trạng thái</h6></li>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/admin/order/update-status" method="post" class="d-inline">
                                                    <input type="hidden" name="id" value="${o.id}">
                                                    <input type="hidden" name="status" value="SHIPPING">
                                                    <button class="dropdown-item text-warning" type="submit"><i class="fa-solid fa-truck-fast me-2"></i>Giao hàng</button>
                                                </form>
                                            </li>
                                            <li>
                                                <form action="${pageContext.request.contextPath}/admin/order/update-status" method="post" class="d-inline">
                                                    <input type="hidden" name="id" value="${o.id}">
                                                    <input type="hidden" name="status" value="COMPLETED">
                                                    <button class="dropdown-item text-success" type="submit"><i class="fa-solid fa-check me-2"></i>Hoàn thành</button>
                                                </form>
                                            </li>
                                            <li><hr class="dropdown-divider"></li>
                                            <li><a class="dropdown-item fw-bold" href="${pageContext.request.contextPath}/admin/order/detail?id=${o.id}">Xem chi tiết</a></li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <c:if test="${empty orders}">
                    <div class="text-center py-5 text-muted">
                        <i class="fa-solid fa-clipboard-list fs-1 mb-3 text-secondary opacity-50"></i>
                        <p>Chưa có đơn hàng nào.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <jsp:include page="/footer.jsp" />
</body>
</html>