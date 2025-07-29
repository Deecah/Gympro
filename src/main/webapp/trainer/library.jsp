<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="dao.ExerciseLibraryDAO, java.util.List, model.ExerciseLibrary" %>
<%
    ExerciseLibraryDAO dao = new ExerciseLibraryDAO();
    List<ExerciseLibrary> exercises = dao.getAllExercises();
    request.setAttribute("exercises", exercises);
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exercise Library</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <style>
            body {
                margin: 0;
            }
            .sidebar {
                width: 240px;
                min-height: 100vh;
            }
            td {
                word-wrap: break-word;
                max-width: 300px;
            }
            .action-buttons .edit-btn,
            .action-buttons .delete-btn {
                visibility: hidden;  /* <- keeps space reserved */
            }
            .exercise-row:hover .edit-btn,
            .exercise-row:hover .delete-btn {
                visibility: visible;
            }
            .delete-form {
                margin: 0;
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
<body>

<div class="d-flex">
    <!-- Sidebar -->
    <div class="sidebar bg-dark text-white">
        <jsp:include page="sidebar.jsp" />
    </div>

    <!-- Main Content -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2>Exercise Library</h2>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <div>
                <a href="create-exercise.jsp" class="btn btn-primary">+ Add Exercise</a>
            </div>
        </div>

        <input id="searchInput" class="form-control mb-3" placeholder="Search exercises..." onkeyup="filterExercises()" />

        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark text-center">
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Muscle Group</th>
                    <th>Equipment</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody id="exerciseTable">
                <c:forEach var="ex" items="${exercises}">
                    <tr class="exercise-row">
                        <td>${ex.name}</td>
                        <td>${ex.description}</td>
                        <td>${ex.muscleGroup}</td>
                        <td>${ex.equipment}</td>
                        <td class="text-center">
                            <div class="action-buttons d-flex justify-content-center gap-2">
                                <a href="${pageContext.request.contextPath}/EditExerciseServlet?id=${ex.exerciseID}" 
                                   class="btn btn-sm btn-primary edit-btn" title="Edit">
                                    <i class="fas fa-pen"></i>
                                </a>
                                <form action="${pageContext.request.contextPath}/DeleteExerciseLibraryServlet" method="post" class="d-inline delete-form" onClick="event.stopPropagation();">
                                    <input type="hidden" name="id" value="${ex.exerciseID}" />
                                    <button type="submit" class="btn btn-sm btn-danger delete-btn" title="Delete" onclick="return confirm('Are you sure you want to delete this exercise?');">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function filterExercises() {
        const input = document.getElementById("searchInput");
        const filter = input.value.toLowerCase();
        const rows = document.getElementById("exerciseTable").getElementsByTagName("tr");

        for (let i = 0; i < rows.length; i++) {
            const cells = rows[i].getElementsByTagName("td");
            let match = false;
            for (let j = 0; j < cells.length; j++) {
                const cell = cells[j];
                if (cell.textContent.toLowerCase().includes(filter)) {
                    match = true;
                    break;
                }
            }
            rows[i].style.display = match ? "" : "none";
        }
    }
</script>
</body>
</html>
