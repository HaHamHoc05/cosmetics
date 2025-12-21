<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head><title>${not empty p ? 'Chỉnh sửa' : 'Thêm mới'} Sản phẩm</title></head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container my-5" style="max-width: 600px;">
        <div class="card shadow-sm border-0 rounded-4 p-4">
            <h3 class="fw-bold text-danger mb-4">${not empty p ? 'CẬP NHẬT' : 'THÊM'} SẢN PHẨM</h3>
            
            <form action="product-save" method="post">
                <c:if test="${not empty p}">
                    <input type="hidden" name="id" value="${p.id}">
                </c:if>

                <div class="mb-3">
                    <label class="form-label fw-bold">Tên sản phẩm</label>
                    <input type="text" name="name" class="form-control" value="${p.name}" required>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Giá bán (đ)</label>
                        <input type="number" name="price" class="form-control" value="${p.price}" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Số lượng kho</label>
                        <input type="number" name="quantity" class="form-control" value="${p.quantity}" required>
                    </div>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold">Link ảnh sản phẩm</label>
                    <input type="text" name="imageUrl" class="form-control" value="${p.imageUrl}">
                </div>
                <div class="mb-4">
                    <label class="form-label fw-bold">Mô tả</label>
                    <textarea name="description" class="form-control" rows="4">${p.description}</textarea>
                </div>
                
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-danger flex-grow-1 rounded-pill fw-bold">LƯU THÔNG TIN</button>
                    <a href="admin/products" class="btn btn-outline-secondary rounded-pill px-4">HỦY</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>