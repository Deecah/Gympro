<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
    <head>
        <title>Admin - User List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
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
                        <h2><i class="fas fa-users me-2"></i> View User List</h2>

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
                                                    <img src="/images/avatar/${t.avatarUrl}" alt="avatar" width="40" height="40" class="rounded-circle" />
                                                    ${u.userName}
                                                </td>
                                                <td>${u.email}</td>
                                                <td>${u.phone}</td>
                                                <td>${u.role}</td>
                                                <td>${u.status}</td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            Actions
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="dropdown-item" href="UserServlet?action=viewprofile&userId=${u.userId}">View Profile</a></li>
                                                                <c:choose>
                                                                    <c:when test="${u.status == 'Normal'}">
                                                                    <li><a class="dropdown-item text-danger" href="UserServlet?action=banuser&userId=${u.userId}">Ban User</a></li>
                                                                    </c:when>
                                                                    <c:when test="${u.status == 'Banned'}">
                                                                    <li><a class="dropdown-item text-success" href="UserServlet?action=unbanuser&userId=${u.userId}">Unban User</a></li>
                                                                    </c:when>
                                                                </c:choose>
                                                        </ul>
                                                    </div>
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
    </body>
</html>
