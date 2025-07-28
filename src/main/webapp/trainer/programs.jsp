<%@ page import="java.util.List" %>
<%@ page import="model.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    List<Program> programs = (List<Program>) request.getAttribute("programs");
    ArrayList<User> customers = (ArrayList<User>) request.getAttribute("customers");
    String programId = (String) request.getAttribute("programId");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Programs</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/programs.css">
        
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
    <body class="programs-container">
        <div class="d-flex">
            <!-- Sidebar -->
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp" />
            </div>

            <!-- Main Content -->
            <div class="flex-grow-1 p-4">
                <!-- Header Section -->
                <div class="programs-header">
                    <h1 class="programs-title">
                        <i class="fas fa-dumbbell me-3"></i>Programs
                    </h1>
                    <p class="programs-subtitle">Manage and organize your training programs</p>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <button class="btn btn-create-program" data-bs-toggle="modal" data-bs-target="#createProgramModal">
                        <i class="fas fa-plus me-2"></i>Create Program
                    </button>
                    <button class="btn btn-manage-tags">
                        <i class="fas fa-tags me-2"></i>Manage Tags
                    </button>
                </div>

                <!-- Search Bar -->
                <div class="search-container">
                    <i class="fas fa-search search-icon"></i>
                    <input class="form-control search-input" placeholder="Search programs..." id="searchInput"/>
                </div>

                <!-- Programs Table -->
                <div class="programs-table-container">
                    <table class="table programs-table">
                        <thead>
                            <tr>
                                <th><i class="fas fa-file-alt me-2"></i>Name</th>
                                <th><i class="fas fa-info-circle me-2"></i>Description</th>
                                <th><i class="fas fa-cogs me-2"></i>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Program p : programs) { %>
                            <tr>
                                <td>
                                    <div class="program-name"><%= p.getName() %></div>
                                </td>
                                <td>
                                    <div class="program-description"><%= p.getDescription() %></div>
                                </td>
                                <td class="action-buttons-cell">
                                    <div class="btn-group">
                                        <button class="btn btn-assign" onclick="openAssignModal(<%= p.getProgramId() %>)">
                                            <i class="fas fa-paper-plane me-1"></i>Assign
                                        </button>
                                        <button type="button" class="btn btn-dropdown dropdown-toggle dropdown-toggle-split"
                                                data-bs-toggle="dropdown" aria-expanded="false">
                                            <i class="fas fa-ellipsis-v"></i>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li>
                                                <a class="dropdown-item" href="<%= request.getContextPath() %>/ProgramDetailServlet?programId=<%= p.getProgramId() %>">
                                                    <i class="fas fa-edit me-2"></i>Edit Program
                                                </a>
                                            </li>
                                            <li>
                                                <form action="<%= request.getContextPath() %>/DeleteProgramServlet" method="post"
                                                      onsubmit="return confirm('Are you sure you want to delete this program?')">
                                                    <input type="hidden" name="programId" value="<%= p.getProgramId() %>">
                                                    <button type="submit" class="dropdown-item text-danger">
                                                        <i class="fas fa-trash me-2"></i>Delete Program
                                                    </button>
                                                </form>
                                            </li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- Empty State (if no programs) -->
                <% if (programs == null || programs.isEmpty()) { %>
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-dumbbell"></i>
                    </div>
                    <h3 class="empty-state-title">No Programs Yet</h3>
                    <p class="empty-state-description">Create your first training program to get started</p>
                    <button class="btn btn-create-program" data-bs-toggle="modal" data-bs-target="#createProgramModal">
                        <i class="fas fa-plus me-2"></i>Create Your First Program
                    </button>
                </div>
                <% } %>
            </div>
        </div>
        
        <!-- Create Program Modal -->
        <div class="modal fade" id="createProgramModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="<%= request.getContextPath() %>/ProgramServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-plus me-2"></i>Create New Program
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="action" value="create" />
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-file-alt me-2"></i>Program Name
                            </label>
                            <input type="text" name="name" class="form-control" placeholder="Enter program name..." required />
                        </div>
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-info-circle me-2"></i>Description
                            </label>
                            <textarea name="description" class="form-control" placeholder="Enter program description..." rows="3"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-box me-2"></i>Package
                            </label>
                            <select name="packageId" class="form-select">
                                <option value="0">No package assigned</option>
                                <c:forEach var="pkg" items="${packageList}">
                                    <option value="${pkg.packageID}">${pkg.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" type="submit">
                            <i class="fas fa-check me-2"></i>Create Program
                        </button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Assign Program Modal -->
        <div class="modal fade" id="assignProgramModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="<%= request.getContextPath() %>/AssignProgramServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-paper-plane me-2"></i>Assign Program to Customer
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="programId" id="assignProgramId" />
                        
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-info-circle me-2"></i>Program Information
                            </label>
                            <div class="alert program-info-alert">
                                <strong id="programName">Program Name</strong><br>
                                <span id="programDescription">Program Description</span>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-users me-2"></i>Select Customer
                            </label>
                            <% if (customers != null && !customers.isEmpty()) { %>
                                <select name="customerId" class="form-select" required>
                                    <option value="">Choose a customer...</option>
                                    <% for (User customer : customers) { %>
                                        <option value="<%= customer.getUserId() %>">
                                            <%= customer.getUserName() %> (<%= customer.getEmail() %>)
                                        </option>
                                    <% } %>
                                </select>
                            <% } else { %>
                                <div class="alert customer-warning-alert">
                                    <i class="fas fa-exclamation-triangle me-2"></i>
                                    <strong>No customers found!</strong><br>
                                    You don't have any active contracts with customers yet. 
                                    Customers will appear here once they purchase your packages.
                                </div>
                                <select name="customerId" class="form-select" disabled>
                                    <option value="">No customers available</option>
                                </select>
                            <% } %>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">
                                <i class="fas fa-calendar me-2"></i>Start Date
                            </label>
                            <input type="date" name="startDate" class="form-control" required 
                                   min="<%= java.time.LocalDate.now() %>" />
                            <div class="form-text">
                                <i class="fas fa-info-circle me-1"></i> Start date must be today or in the future. 
                                If you select a date that would cause workouts to fall in the past, 
                                the system will automatically adjust to the next available date.
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <% if (customers != null && !customers.isEmpty()) { %>
                            <button class="btn btn-success" type="submit">
                                <i class="fas fa-check me-2"></i>Assign Program
                            </button>
                        <% } else { %>
                            <button class="btn btn-success" type="submit" disabled>
                                <i class="fas fa-check me-2"></i>Assign Program
                            </button>
                        <% } %>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-2"></i>Cancel
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <script>
            // Search functionality
            document.getElementById('searchInput').addEventListener('input', function() {
                var searchTerm = this.value.toLowerCase();
                var rows = document.querySelectorAll('.programs-table tbody tr');
                
                for (var i = 0; i < rows.length; i++) {
                    var row = rows[i];
                    var name = row.querySelector('.program-name').textContent.toLowerCase();
                    var description = row.querySelector('.program-description').textContent.toLowerCase();
                    
                    if (name.indexOf(searchTerm) !== -1 || description.indexOf(searchTerm) !== -1) {
                        row.style.display = '';
                        row.style.animation = 'fadeInUp 0.3s ease forwards';
                    } else {
                        row.style.display = 'none';
                    }
                }
            });
            
            function openAssignModal(programId) {
                document.getElementById('assignProgramId').value = programId;
                
                // Lấy thông tin chương trình để hiển thị
                var programName = '';
                var programDescription = '';
                
                // Tìm thông tin chương trình từ bảng
                var rows = document.querySelectorAll('tbody tr');
                for (var i = 0; i < rows.length; i++) {
                    var cells = rows[i].cells;
                    if (cells.length > 0) {
                        var button = rows[i].querySelector('button[onclick*="' + programId + '"]');
                        if (button) {
                            programName = cells[0].querySelector('.program-name').textContent.trim();
                            programDescription = cells[1].querySelector('.program-description').textContent.trim();
                            break;
                        }
                    }
                }
                
                // Cập nhật tiêu đề modal và thông tin chương trình
                document.querySelector('#assignProgramModal .modal-title').innerHTML = 
                    '<i class="fas fa-paper-plane me-2"></i>Assign Program: ' + programName;
                document.getElementById('programName').textContent = programName;
                document.getElementById('programDescription').textContent = programDescription;
                
                var modal = new bootstrap.Modal(document.getElementById('assignProgramModal'));
                modal.show();
            }
            
            // Enhanced notifications
            <% if (request.getParameter("error") != null) { %>
                showNotification('Error: <%= request.getParameter("error") %>', 'error');
            <% } %>
            
            <% if (request.getParameter("success") != null) { %>
                showNotification('Success: Program assigned successfully!', 'success');
            <% } %>
            
            function showNotification(message, type) {
                const notification = document.createElement('div');
                const alertClass = type === 'error' ? 'danger' : 'success';
                const iconClass = type === 'error' ? 'exclamation-triangle' : 'check-circle';
                
                notification.className = 'alert alert-' + alertClass + ' notification-toast';
                notification.style.cssText = 
                    'position: fixed;' +
                    'top: 20px;' +
                    'right: 20px;' +
                    'z-index: 9999;' +
                    'min-width: 300px;' +
                    'border-radius: 10px;' +
                    'box-shadow: 0 4px 15px rgba(0,0,0,0.2);' +
                    'animation: slideInRight 0.3s ease;';
                
                notification.innerHTML = 
                    '<i class="fas fa-' + iconClass + ' me-2"></i>' +
                    message +
                    '<button type="button" class="btn-close ms-auto" onclick="this.parentElement.remove()"></button>';
                
                document.body.appendChild(notification);
                
                setTimeout(() => {
                    if (notification.parentElement) {
                        notification.style.animation = 'slideOutRight 0.3s ease';
                        setTimeout(() => notification.remove(), 300);
                    }
                }, 5000);
            }
            
            // Add loading state to buttons
            var forms = document.querySelectorAll('form');
            for (var i = 0; i < forms.length; i++) {
                forms[i].addEventListener('submit', function() {
                    var submitBtn = this.querySelector('button[type="submit"]');
                    if (submitBtn) {
                        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';
                        submitBtn.disabled = true;
                    }
                });
            }
            
            // Smooth scrolling for table
            var tableRows = document.querySelectorAll('.programs-table tbody tr');
            for (var j = 0; j < tableRows.length; j++) {
                tableRows[j].style.animationDelay = (j * 0.1) + 's';
            }
        </script>
        
        <style>
            @keyframes slideInRight {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            
            @keyframes slideOutRight {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
            
            .notification-toast {
                backdrop-filter: blur(10px);
                border: none;
            }
        </style>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
