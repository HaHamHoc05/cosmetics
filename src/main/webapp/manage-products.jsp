<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Sản phẩm - Admin</title>
</head>
<body>
    <jsp:include page="/header.jsp" />

    <div class="container my-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-danger border-start border-4 border-danger ps-3">QUẢN LÝ SẢN PHẨM</h2>
            <a href="${pageContext.request.contextPath}/admin/product/create" class="btn btn-danger rounded-pill px-4 shadow-sm">
                <i class="fa-solid fa-plus me-2"></i>Thêm sản phẩm
            </a>
        </div>

        <div class="card border-0 shadow-sm rounded-4 overflow-hidden">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light text-secondary small text-uppercase">
                        <tr>
                            <th class="ps-4 py-3">ID</th>
                            <th>Hình ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá bán</th>
                            <th>Kho</th>
                            <th class="text-end pe-4">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td class="ps-4 fw-bold text-muted">#${p.id}</td>
                                <td>
                                    <div class="bg-white border rounded p-1" style="width: 60px; height: 60px; display: flex; align-items: center; justify-content: center;">
                                        <img src="${not empty p.imageUrl ? p.imageUrl : 'https://via.placeholder.com/150'}" 
                                             alt="${p.name}" style="max-width: 100%; max-height: 100%; object-fit: contain;">
                                    </div>
                                </td>
                                <td>
                                    <div class="fw-bold text-dark text-truncate" style="max-width: 250px;" title="${p.name}">${p.name}</div>
                                </td>
                                <td class="text-danger fw-bold">
                                    <fmt:formatNumber value="${p.price}" type="currency" currencySymbol="đ"/>
                                </td>
                                <td>
                                    <span class="badge ${p.quantity > 0 ? 'bg-success-subtle text-success' : 'bg-danger-subtle text-danger'} rounded-pill px-3">
                                        ${p.quantity > 0 ? p.quantity : 'Hết hàng'}
                                    </span>
                                </td>
                                <td class="text-end pe-4">
                                    <a href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}" class="btn btn-sm btn-outline-primary rounded-circle me-2" title="Sửa">
                                        <i class="fa-solid fa-pen"></i>
                                    </a>
                                    <button type="button" class="btn btn-sm btn-outline-danger rounded-circle" 
                                            onclick="confirmDelete('${p.id}', '${p.name}')" title="Xóa">
                                        <i class="fa-solid fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <c:if test="${empty products}">
                    <div class="text-center py-5 text-muted">
                        <i class="fa-solid fa-box-open fs-1 mb-3 text-secondary opacity-50"></i>
                        <p>Chưa có sản phẩm nào trong hệ thống.</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <form id="deleteForm" action="${pageContext.request.contextPath}/admin/product/delete" method="post" style="display:none;">
        <input type="hidden" name="id" id="deleteId">
    </form>

    <script>
        function confirmDelete(id, name) {
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm "' + name + '" không?')) {
                document.getElementById('deleteId').value = id;
                document.getElementById('deleteForm').submit();
            }
        }
    </script>

    <jsp:include page="/footer.jsp" />
</body>
</html>