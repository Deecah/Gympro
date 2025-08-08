<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
    <head>
        <title>Admin - User List</title>
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
                            <h2><i class="fas fa-users me-2"></i> View User List</h2>
                            
                            <!-- Filter Dropdown -->
                            <div class="d-flex align-items-center">
                                <label for="userFilter" class="me-2 fw-bold">Filter by Role:</label>
                                <select id="userFilter" class="form-select" style="width: auto;" onchange="filterUsers()">
                                    <option value="all" ${selectedType == 'all' ? 'selected' : ''}>All</option>
                                    <option value="Trainer" ${selectedType == 'Trainer' ? 'selected' : ''}>Trainer</option>
                                    <option value="Customer" ${selectedType == 'Customer' ? 'selected' : ''}>Customer</option>
                                </select>
                            </div>
                        </div>

                        <table class="table table-striped table-hover mt-3">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty userList}">
                                        <c:forEach var="u" items="${userList}">
                                            <tr>
                                                <td>
                                                    <img src="${u.avatarUrl}" alt="avatar" width="40" height="40" class="rounded-circle" />
                                                    ${u.userName}
                                                </td>
                                                <td>${u.email}</td>
                                                <td>${u.phone}</td>
                                                <td>${u.role}</td>
                                                <td>${u.status}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${u.status == 'Normal'}">
                                                            <a href="UserServlet?action=banuser&userId=${u.userId}&currentType=${selectedType}" 
                                                               class="btn btn-danger btn-sm" 
                                                               title="Ban User"
                                                               onclick="return confirm('Bạn có chắc muốn cấm người dùng này?')"
                                                               style="width: 80px; text-align: center;">
                                                                <i class="fas fa-ban me-1"></i>Ban
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${u.status == 'Banned'}">
                                                            <a href="UserServlet?action=unbanuser&userId=${u.userId}&currentType=${selectedType}" 
                                                               class="btn btn-success btn-sm" 
                                                               title="Unban User"
                                                               onclick="return confirm('Bạn có chắc muốn bỏ cấm người dùng này?')"
                                                               style="width: 80px; text-align: center;">
                                                                <i class="fas fa-check-circle me-1"></i>Unban
                                                            </a>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="6" class="text-center text-muted">Không có người dùng nào để hiển thị.</td></tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            function filterUsers() {
                const selectedValue = document.getElementById('userFilter').value;
                window.location.href = 'UserServlet?action=view&type=' + selectedValue;
            }
        </script>
    </body>
</html>
