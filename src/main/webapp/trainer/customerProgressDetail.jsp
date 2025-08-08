<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Customer Progress Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/stylecss/customers.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/stylecss/avatar-fix.css" rel="stylesheet">
        <style>
            body { margin: 0; }
            .sidebar { width: 240px; min-height: 100vh; }
        </style>
        
        <script>
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
                <!-- Back Button -->
                <a href="${pageContext.request.contextPath}/CustomerServlet" class="back-btn">
                    <i class="fas fa-arrow-left me-2"></i>Back to Clients
                </a>

                <!-- Customer Info -->
                <div class="progress-header">
                    <div class="row align-items-center">
                        <div class="col-md-2">
                            <img src="${customer.avatarUrl}" class="customer-avatar" 
                                 onerror="this.src='../img/default-avatar.jpg'"
                                 style="width: 80px; height: 80px; border-radius: 50%;"/>
                        </div>
                        <div class="col-md-6">
                            <h2>${customer.userName}</h2>
                            <p class="mb-0">${customer.email}</p>
                        </div>
                        <div class="col-md-4 text-end">
                            <h3>${programProgress.programName}</h3>
                            <div class="progress-stats">
                                <div class="progress-percentage">${programProgress.progressPercent}%</div>
                                <div class="workout-count">
                                    ${programProgress.completedWorkouts}/${programProgress.totalWorkouts} workouts completed
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Progress Bar -->
                <div class="progress mb-4" style="height: 20px;">
                    <div class="progress-bar bg-success" 
                         style="width: ${programProgress.progressPercent}%">
                        ${programProgress.progressPercent}%
                    </div>
                </div>

                <!-- Workouts Summary -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-dumbbell me-2"></i>Workouts Summary
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-3">
                                <div class="text-center">
                                    <h4 class="text-success">${programProgress.completedWorkouts}</h4>
                                    <small class="text-muted">Completed</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="text-center">
                                    <h4 class="text-warning">${programProgress.totalWorkouts - programProgress.completedWorkouts}</h4>
                                    <small class="text-muted">Pending</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="text-center">
                                    <h4 class="text-info">${programProgress.totalWorkouts}</h4>
                                    <small class="text-muted">Total</small>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="text-center">
                                    <h4 class="text-primary">${programProgress.progressPercent}%</h4>
                                    <small class="text-muted">Progress</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Workouts List -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-list me-2"></i>Workouts List
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty workouts}">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Workout</th>
                                                <th>Week</th>
                                                <th>Day</th>
                                                <th>Status</th>
                                                <th>Scheduled</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="workout" items="${workouts}">
                                                <tr>
                                                    <td>
                                                        <strong>${workout.workoutName}</strong>
                                                        <c:if test="${not empty workout.description}">
                                                            <br><small class="text-muted">${workout.description}</small>
                                                        </c:if>
                                                    </td>
                                                    <td>Week ${workout.weekNumber}</td>
                                                    <td>${workout.dayName}</td>
                                                    <td>
                                                        <span class="badge ${workout.completed ? 'bg-success' : 'bg-warning'}">
                                                            <i class="fas ${workout.completed ? 'fa-check' : 'fa-clock'} me-1"></i>
                                                            ${workout.statusText}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <small>${workout.scheduledTimeFormatted}</small>
                                                        <c:if test="${workout.completed}">
                                                            <br><small class="text-success">Completed: ${workout.completedAtFormatted}</small>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="fas fa-dumbbell fa-3x text-muted mb-3"></i>
                                    <h5>No Workouts Found</h5>
                                    <p class="text-muted">This program doesn't have any workouts assigned yet.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 