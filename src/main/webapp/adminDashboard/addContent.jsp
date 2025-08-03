<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Add Content</title>
    
    <!-- Bootstrap & FontAwesome -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Custom CSS -->
    <link rel="stylesheet" href="<%= context %>/stylecss/adminDashboard.css">
</head>
<body>
<div class="d-flex" id="wrapper">
    
    <!-- Sidebar -->
    <jsp:include page="sidebar.jsp"/>

    <!-- Page Content -->
    <div id="page-content-wrapper">
        <jsp:include page="navbar.jsp"/>

        <div class="container-fluid py-4">
            <div class="container mt-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-plus-circle me-2"></i> Add New Content</h2>
                </div>

                <!-- Form Add Content -->
                <form class="row g-3" action="<%=context%>/ContentServlet?action=submitAdd" method="post">
                    <div class="col-md-12">
                        <label for="contentTitleAdd" class="form-label fw-bold">Title:</label>
                        <input type="text" class="form-control" id="contentTitleAdd" name="contentTitle" required>
                    </div>
                    <div class="col-12">
                        <label for="contentBodyAdd" class="form-label fw-bold">Content:</label>
                        <textarea class="form-control" id="contentBodyAdd" name="contentBody" rows="10" required></textarea>
                    </div>
                    <div class="col-12">
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-save me-1"></i>Save
                        </button>
                        <a href="<%=context%>/ContentServlet?action=viewAll" class="btn btn-secondary ms-2">
                            <i class="fas fa-arrow-left me-1"></i>Cancel
                        </a>
                    </div>
                </form>

                <!-- Error Message -->
                <div class="mt-3 p-2 text-danger">
                    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
