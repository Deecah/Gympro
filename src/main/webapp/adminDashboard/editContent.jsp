<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
    <head>
        <title>Edit Content</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/adminDashboard.css">
    </head>
    <body>
        <div class="d-flex" id="wrapper">
            <jsp:include page="sidebar.jsp" />

            <div id="page-content-wrapper">
                <jsp:include page="navbar.jsp" />

                <div class="container-fluid py-4">
                    <div class="container mt-5">
                        <h2 class="mb-4"><i class="fas fa-edit me-2"></i> Edit Content</h2>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <form method="post" action="${pageContext.request.contextPath}/ContentServlet?action=submitEdit">
                            <input type="hidden" name="contentId" value="${content.id}" />

                            <div class="mb-3">
                                <label for="contentTitle" class="form-label">Title</label>
                                <input type="text" class="form-control" id="contentTitle" name="contentTitle" value="${content.title}" required>
                            </div>

                            <div class="mb-3">
                                <label for="contentBody" class="form-label">Body</label>
                                <textarea class="form-control" id="contentBody" name="contentBody" rows="5" required>${content.body}</textarea>
                            </div>

                            <button type="submit" class="btn btn-primary"><i class="fas fa-save me-1"></i> Save Changes</button>
                            <a href="${pageContext.request.contextPath}/ContentServlet?action=viewAll" class="btn btn-secondary"><i class="fas fa-arrow-left me-1"></i> Back to List</a>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
