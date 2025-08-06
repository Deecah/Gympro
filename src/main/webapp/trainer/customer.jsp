<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Clients</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/stylecss/customers.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/stylecss/avatar-fix.css" rel="stylesheet">
        <style>
            body {
                margin: 0;
            }
            .sidebar {
                width: 240px;
                min-height: 100vh;
            }
        </style>
        
        <script>
            // Set current user ID for notification.js
            <% if (session.getAttribute("user") != null) { %>
            var currentUserId = <%= ((model.User)session.getAttribute("user")).getUserId() %>;
            <% } else { %>
            var currentUserId = null;
            <% } %>
        </script>
        <script src="../js/notification.js"></script>
    </head>
    <body class="customers-container">

        <div class="d-flex">
            <!-- Sidebar -->
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp" />
            </div>

            <!-- Main Content -->
            <div class="flex-grow-1 p-4">
                <!-- Header Section -->
                <div class="customers-header">
                    <h1 class="customers-title">
                        <i class="fas fa-users me-3"></i>Clients
                    </h1>
                    <p class="customers-subtitle">Manage your clients and track their progress</p>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <button class="btn btn-add-client">
                        <i class="fas fa-plus me-2"></i>Add Client
                    </button>
                    <button class="btn btn-manage-groups">
                        <i class="fas fa-layer-group me-2"></i>Manage Groups
                    </button>
                    <button class="btn btn-export">
                        <i class="fas fa-download me-2"></i>Export
                    </button>
                </div>

                <!-- Tabs -->
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link active">
                            <i class="fas fa-check-circle me-2"></i>Active 
                            <span class="badge">${customers.size()}</span>
                        </a>
                    </li>
                </ul>

                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Search Bar -->
                <div class="search-container">
                    <i class="fas fa-search search-icon"></i>
                    <input class="form-control search-input" placeholder="Search clients..." id="searchInput"/>
                </div>

                <!-- Customers Table -->
                <div class="customers-table-container">
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>Customer Name</th>
                                <th>Program Name</th>
                                <th>Total Workouts</th>
                                <th>Completed Workouts</th>
                                <th>Progress (%)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="progress" items="${customers}">
                                <tr>
                                    <td>${progress.customerName}</td>
                                    <td>${progress.programName}</td>
                                    <td>${progress.totalWorkouts}</td>
                                    <td>${progress.completedWorkouts}</td>
                                    <td>${progress.progressPercent}%</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Empty State (if no customers) -->
                <c:if test="${empty customers}">
                    <div class="empty-state">
                        <div class="empty-state-icon">
                            <i class="fas fa-users"></i>
                        </div>
                        <h3 class="empty-state-title">No Clients Yet</h3>
                        <p class="empty-state-description">Start building your client base to track their progress</p>
                        <button class="btn btn-add-client">
                            <i class="fas fa-plus me-2"></i>Add Your First Client
                        </button>
                    </div>
                </c:if>

                <!-- Pro Tip -->
                <div class="pro-tip">
                    <strong>Pro tip!</strong> Tap <kbd>?</kbd> to view keyboard shortcuts for any page.
                </div>
            </div>
        </div>

        <script>
            // Search functionality
            document.getElementById('searchInput').addEventListener('input', function() {
                var searchTerm = this.value.toLowerCase();
                var rows = document.querySelectorAll('.customers-table tbody tr');
                
                for (var i = 0; i < rows.length; i++) {
                    var row = rows[i];
                    var customerName = row.querySelector('.customer-name').textContent.toLowerCase();
                    var customerEmail = row.querySelector('.customer-email').textContent.toLowerCase();
                    var programName = row.querySelector('.program-name').textContent.toLowerCase();
                    
                    if (customerName.indexOf(searchTerm) !== -1 || 
                        customerEmail.indexOf(searchTerm) !== -1 ||
                        programName.indexOf(searchTerm) !== -1) {
                        row.style.display = '';
                        row.style.animation = 'fadeInUp 0.3s ease forwards';
                    } else {
                        row.style.display = 'none';
                    }
                }
            });
            
            // Smooth animations for table rows
            var tableRows = document.querySelectorAll('.customers-table tbody tr');
            for (var j = 0; j < tableRows.length; j++) {
                tableRows[j].style.animationDelay = (j * 0.1) + 's';
            }
            
            // Add loading state to buttons
            document.querySelectorAll('button').forEach(function(button) {
                button.addEventListener('click', function() {
                    if (this.classList.contains('btn-add-client') || 
                        this.classList.contains('btn-manage-groups') || 
                        this.classList.contains('btn-export')) {
                        this.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';
                        this.disabled = true;
                    }
                });
            });
        </script>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
