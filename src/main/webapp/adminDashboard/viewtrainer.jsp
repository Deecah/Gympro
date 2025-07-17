<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
    <head>
        <title>Admin - Trainer List</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/adminDashboard.css">
    </head>
    <body>
        <div class="d-flex" id="wrapper">
            <jsp:include page="sidebar.jsp"/>
            <div id="page-content-wrapper">
                <jsp:include page="navbar.jsp"/>
                <div class="container-fluid py-4">
                    <div class="container mt-5">
                        <h2><i class="fas fa-dumbbell me-2"></i> Trainer List</h2>
                        <table class="table table-striped table-hover mt-3">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Phone</th>
                                    <th>Specialization</th>
                                    <th>Experience</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty trainerList}">
                                        <c:forEach var="t" items="${trainerList}">
                                            <tr>
                                                <td>
                                                    <img src="/images/avatar/${t.avatarUrl}" alt="avatar" width="40" height="40" class="rounded-circle" />
                                                    ${t.userName}
                                                </td>
                                                <td>${t.email}</td>
                                                <td>${u.phone}</td>
                                                <td>${t.specialization}</td>
                                                <td>${t.experienceYears} years</td>
                                                <td>${t.status}</td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                                            Actions
                                                        </button>
                                                        <ul class="dropdown-menu">
                                                            <li><a class="dropdown-item" href="AdminManagementServlet?action=viewtrainerprofile&userId=${t.userId}">View Profile</a></li>
                                                                <c:choose>
                                                                    <c:when test="${t.status == 'Active'}">
                                                                    <li><a class="dropdown-item text-danger" href="UserServlet?action=bantrainer&userId=${t.userId}">Ban</a></li>
                                                                    </c:when>
                                                                    <c:when test="${t.status == 'Banned'}">
                                                                    <li><a class="dropdown-item text-success" href="UserServlet?action=unbantrainer&userId=${t.userId}">Unban</a></li>
                                                                    </c:when>
                                                                </c:choose>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="6" class="text-center text-muted">Không có huấn luyện viên nào để hiển thị.</td></tr>
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
