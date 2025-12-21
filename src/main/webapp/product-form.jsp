<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${not empty p ? 'Sửa' : 'Thêm'} Sản phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <jsp:include page="header.jsp" />
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card shadow border-0 rounded-3">
                    <div class="card-header bg-danger text-white p-3">
                        <h5 class="mb-0 fw-bold">${not empty p ? 'CẬP NHẬT SẢN PHẨM' : 'THÊM SẢN PHẨM MỚI'}</h5>
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/product-save" method="post">
                            <%-- ID ẩn nếu là chế độ sửa --%>
                            <c:if test="${not empty p}">
                                <input type="hidden" name="id" value="${p.id}">
                            </c:if>

                            <div class="mb-3">
                                <label class="form-label fw-bold">Tên sản phẩm</label>
                                <input type="text" name="name" class="form-control" value="${p.name}" required>
                            </div>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Giá bán (VNĐ)</label>
                                    <input type="number" name="price" class="form-control" value="${p.price}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label fw-bold">Số lượng kho</label>
                                    <input type="number" name="quantity" class="form-control" value="${p.quantity}" required>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label fw-bold">URL Hình ảnh</label>
                                <input type="text" name="imageUrl" class="form-control" value="${p.imageUrl}" placeholder="http://...">
                            </div>

                            <div class="mb-4">
                                <label class="form-label fw-bold">Mô tả sản phẩm</label>
                                <textarea name="description" class="form-control" rows="4">${p.description}</textarea>
                            </div>

                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-danger px-4 fw-bold">LƯU DỮ LIỆU</button>
                                <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline-secondary px-4">Hủy bỏ</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="footer.jsp" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>