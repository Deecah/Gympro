<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<html>
    <head>
        <title>Admin - Violation Reports</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/adminDashboard.css">
    </head>
    <body>
        <div class="d-flex" id="wrapper">
            <jsp:include page="sidebar.jsp"/>
            <div id="page-content-wrapper">
                <jsp:include page="navbar.jsp"/>
                <div class="container-fluid py-4">
                    <div class="container mt-4">
                        <div class="report-card">
                            <div class="report-header">
                                <h2 class="mb-0">
                                    <i class="fas fa-flag me-3"></i> 
                                    Violation Reports
                                    <span class="badge ms-2">${reportList.size()}</span>
                                </h2>
                            </div>
                            <div class="report-body">
                                <c:choose>
                                    <c:when test="${not empty reportList}">
                                        <div class="table-responsive">
                                            <table class="table table-hover report-table">
                                                <thead>
                                                    <tr>
                                                        <th><i class="fas fa-user-slash me-2"></i>Reported User</th>
                                                        <th><i class="fas fa-exclamation-triangle me-2"></i>Reason</th>
                                                        <th><i class="fas fa-user me-2"></i>Reported By</th>
                                                        <th><i class="fas fa-clock me-2"></i>Time</th>
                                                        <th><i class="fas fa-cogs me-2"></i>Actions</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="r" items="${reportList}" varStatus="loop">
                                                        <tr class="align-middle">
                                                            <td>
                                                                <c:set var="reportedUser" value="${mapUser[r.reportedUserID]}"/>
                                                                <c:choose>
                                                                    <c:when test="${not empty reportedUser}">
                                                                        <div class="user-info">
                                                                            <c:choose>
                                                                                <c:when test="${not empty reportedUser.avatarUrl}">
                                                                                    <img src="${pageContext.request.contextPath}/img/avatar/${reportedUser.avatarUrl}" 
                                                                                         alt="avatar" class="user-avatar" 
                                                                                         onerror="this.src='${pageContext.request.contextPath}/img/default-avatar.jpg'" />
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <img src="${pageContext.request.contextPath}/img/default-avatar.jpg" 
                                                                                         alt="avatar" class="user-avatar" />
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                            <div>
                                                                                <strong>${reportedUser.userName}</strong>
                                                                                <br><small class="text-muted">${reportedUser.email}</small>
                                                                            </div>
                                                                        </div>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">User not found</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <span class="reason-badge">
                                                                    <i class="fas fa-exclamation-circle me-1"></i>
                                                                    ${r.reason}
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <c:set var="fromUser" value="${mapUser[r.fromUserID]}"/>
                                                                <c:choose>
                                                                    <c:when test="${not empty fromUser}">
                                                                        <div class="user-info">
                                                                            <c:choose>
                                                                                <c:when test="${not empty fromUser.avatarUrl}">
                                                                                    <img src="${pageContext.request.contextPath}/img/avatar/${fromUser.avatarUrl}" 
                                                                                         alt="avatar" class="user-avatar" 
                                                                                         onerror="this.src='${pageContext.request.contextPath}/img/default-avatar.jpg'" />
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <img src="${pageContext.request.contextPath}/img/default-avatar.jpg" 
                                                                                         alt="avatar" class="user-avatar" />
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                            <div>
                                                                                <strong>${fromUser.userName}</strong>
                                                                                <br><small class="text-muted">${fromUser.email}</small>
                                                                            </div>
                                                                        </div>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">User not found</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <span class="time-badge">
                                                                    <i class="fas fa-calendar-alt me-1"></i>
                                                                    <fmt:formatDate value="${r.createdAt}" pattern="HH:mm dd/MM/yyyy" />
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <c:set var="reportedUser" value="${mapUser[r.reportedUserID]}"/>
                                                                <c:choose>
                                                                    <c:when test="${not empty reportedUser}">
                                                                                                                                                 <button class="action-btn ban-btn ban-user-btn" 
                                                                                 data-user-id="${reportedUser.userId}"
                                                                                 data-user-name="<c:out value="${reportedUser.userName}" escapeXml="true"/>"
                                                                                 title="Ban User">
                                                                             <i class="fas fa-ban me-1"></i> Ban User
                                                                         </button>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-muted">User not found</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="empty-state">
                                            <i class="fas fa-flag"></i>
                                            <h4>No Reports Available</h4>
                                            <p class="text-muted">There are currently no violation reports to display.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Confirmation Modal -->
        <div class="modal fade" id="banUserModal" tabindex="-1" aria-labelledby="banUserModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="banUserModalLabel">
                            <i class="fas fa-exclamation-triangle text-warning me-2"></i>
                            Confirm Ban User
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to ban user <strong id="userNameToBan"></strong>?</p>
                        <p class="text-muted small">This action will prevent the user from accessing the system.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" class="btn btn-danger" id="confirmBanBtn">
                            <i class="fas fa-ban me-1"></i> Ban User
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            let userIdToBan = null;
            
            // Add event listeners for ban buttons
            document.addEventListener('DOMContentLoaded', function() {
                // Ban user buttons
                document.querySelectorAll('.ban-user-btn').forEach(button => {
                    button.addEventListener('click', function() {
                        const userId = this.getAttribute('data-user-id');
                        const userName = this.getAttribute('data-user-name');
                        banUser(userId, userName);
                    });
                });
                

            });
            
            function banUser(userId, userName) {
                userIdToBan = userId;
                document.getElementById('userNameToBan').textContent = userName;
                const modal = new bootstrap.Modal(document.getElementById('banUserModal'));
                modal.show();
            }
            

            
            document.getElementById('confirmBanBtn').addEventListener('click', function() {
                if (userIdToBan) {
                    // Redirect to UserServlet to ban the user
                    window.location.href = 'UserServlet?action=banuser&userId=' + userIdToBan;
                }
            });
            
            // Show success/error messages if any
            const urlParams = new URLSearchParams(window.location.search);
            const message = urlParams.get('message');
            if (message) {
                let alertType = 'info';
                let alertMessage = '';
                
                switch(message) {
                    case 'ban_success':
                        alertType = 'success';
                        alertMessage = 'User has been banned successfully!';
                        break;
                    case 'ban_failed':
                        alertType = 'danger';
                        alertMessage = 'Failed to ban user. Please try again.';
                        break;
                    case 'unban_success':
                        alertType = 'success';
                        alertMessage = 'User has been unbanned successfully!';
                        break;
                    case 'unban_failed':
                        alertType = 'danger';
                        alertMessage = 'Failed to unban user. Please try again.';
                        break;
                }
                
                if (alertMessage) {
                    const alertDiv = document.createElement('div');
                    alertDiv.className = `alert alert-${alertType} alert-dismissible fade show position-fixed`;
                    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
                    alertDiv.innerHTML = `
                        ${alertMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    `;
                    document.body.appendChild(alertDiv);
                    
                    // Auto remove after 5 seconds
                    setTimeout(() => {
                        if (alertDiv.parentNode) {
                            alertDiv.parentNode.removeChild(alertDiv);
                        }
                    }, 5000);
                }
            }
        </script>
    </body>
</html>
