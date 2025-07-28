<%-- 
    Document   : index
    Created on : Jun 17, 2025, 7:41:06 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Personal Trainer</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .post-card { border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); margin-bottom: 32px; }
            .post-header { display: flex; align-items: center; padding: 16px; }
            .post-avatar { width: 48px; height: 48px; border-radius: 50%; object-fit: cover; margin-right: 16px; }
            .post-body { padding: 16px; }
            .exercise-list { margin-top: 12px; }
            .exercise-list li { margin-bottom: 4px; }
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
    <body>
        <div class="sidebar bg-dark text-white">
            <jsp:include page="sidebar.jsp" />
        </div>
        <div class="container mt-4">
            <c:forEach var="post" items="${postList}">
                <div class="post-card bg-white">
                    <div class="post-header">
                        <img src="${post.user.avatarUrl}" class="post-avatar" alt="avatar"/>
                        <div>
                            <strong>${post.user.userName}</strong><br/>
                            <small>${post.workout.date}</small>
                        </div>
                    </div>
                    <div class="post-body">
                        <h5>${post.workout.title}</h5>
                        <p>${post.workout.notes}</p>
                        <ul class="exercise-list">
                            <c:forEach var="ex" items="${post.exercises}">
                                <li>
                                    <strong>${ex.exerciseName}</strong> - ${ex.sets} sets x ${ex.reps} reps
                                </li>
                            </c:forEach>
                        </ul>
                        <!-- Nút đánh giá chỉ hiển thị nếu ngày <= hôm nay -->
                        <c:if test="${post.workout.date <= now}">
                            <a href="feedback?workoutId=${post.workout.workoutID}" class="btn btn-primary btn-sm">Đánh giá buổi tập</a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </body>
</html>
