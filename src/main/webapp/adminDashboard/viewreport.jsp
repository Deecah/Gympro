<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
    <head>
        <title>Admin - Violation Reports</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="../stylecss/adminDashboard.css">     
    </head>
    <body>
        <div class="d-flex" id="wrapper">
            <jsp:include page="sidebar.jsp"/>
            <div id="page-content-wrapper">
                <jsp:include page="navbar.jsp"/>
                <div class="container-fluid py-4">
                    <div class="container mt-5">
                        <h2><i class="fas fa-flag me-2"></i> Violation Reports</h2>
                        <table class="table table-bordered table-hover mt-3">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Reported User</th>
                                    <th>Reason</th>
                                    <th>By</th>
                                    <th>Time</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty reportList}">
                                        <c:forEach var="r" items="${reportList}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>
                                                    <c:set var="reportedUser" value="${mapUser[r.reportedUserID]}"/>
                                                    <img src="/images/avatar/${reportedUser.avatarUrl}" alt="avatar" width="40" height="40" class="rounded-circle" />
                                                    ${reportedUser.userName}
                                                </td>
                                                <td>${r.reason}</td>
                                                <td>
                                                    <c:set var="fromUser" value="${mapUser[r.fromUserID]}"/>
                                                    <img src="/images/avatar/${fromUser.avatarUrl}" alt="avatar" width="40" height="40" class="rounded-circle" />
                                                    ${fromUser.userName}
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${r.createdAt}" pattern="HH:mm dd/MM/yy" />
                                                </td>

                                                <td>
                                                    <a class="btn btn-sm btn-primary" href="AdminManagementServlet?action=reviewreport&reportId=${r.violationID}">Review</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr><td colspan="5" class="text-center text-muted">Không có báo cáo nào để hiển thị.</td></tr>
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
