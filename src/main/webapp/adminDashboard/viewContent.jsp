<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
    <head>
        <title>Admin - Content List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/adminDashboard.css">

    </head>
    <body>
        <div class="d-flex" id="wrapper">
            <jsp:include page="sidebar.jsp"/>

            <div id="page-content-wrapper">
                <jsp:include page ="navbar.jsp"/>

                <div class="container-fluid py-4">
                    <%-- Các phần nội dung sẽ được tải bằng AJAX hoặc hiển thị/ẩn --%>
                    <div class="container mt-5">
                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <h2><i class="fas fa-users me-2"></i> Content List</h2>


                        </div>

                        <table class="table table-striped table-hover mt-3">
                            <thead>
                                <tr>
                                    <th>Title</th>
                                    <th>Body</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty contents}">
                                        <<c:forEach var="c" items="${contents}">
                                            <tr>
                                                <td>${c.title}</td>
                                                <td>${c.body}</td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/ContentServlet?action=edit&id=${c.id}" class="btn btn-sm btn-primary me-2">
                                                        <i class="fas fa-edit"></i> Edit
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/ContentServlet?action=delete&id=${c.id}" class="btn btn-sm btn-danger"
                                                       onclick="return confirm('Bạn có chắc chắn muốn xóa nội dung này không?');">
                                                        <i class="fas fa-trash-alt"></i> Delete
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="6" class="text-center text-muted">Không có nội dung nào để hiển thị.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>
