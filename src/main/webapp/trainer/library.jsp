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
    </style>
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
                <button class="btn btn-outline-secondary">Manage Categories</button>
            </div>
            <button class="btn btn-outline-dark">Export</button>
        </div>

        <input class="form-control mb-3" placeholder="Search exercises..."/>

        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark text-center">
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Muscle Group</th>
                    <th>Equipment</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="ex" items="${exercises}">
                    <tr onclick="window.location.href='${pageContext.request.contextPath}/EditExerciseServlet?id=${ex.exerciseID}'" style="cursor:pointer;">
                        <td>${ex.name}</td>
                        <td>${ex.description}</td>
                        <td>${ex.muscleGroup}</td>
                        <td>${ex.equipment}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
