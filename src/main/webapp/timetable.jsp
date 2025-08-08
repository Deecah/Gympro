<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    // Đảm bảo weekStartDate có giá trị mặc định nếu không được truyền
    String weekStartDate = request.getParameter("weekStartDate");
    if (weekStartDate == null || weekStartDate.isEmpty()) {
        weekStartDate = java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY).toString();
    }
    pageContext.setAttribute("weekStartDate", weekStartDate);
    java.time.LocalDate now = java.time.LocalDate.now();
    pageContext.setAttribute("now", now);
%>
<!DOCTYPE html>
<html>
<head>
    <title>Timetable</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/package-trainer.css">
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #dee2e6; padding: 10px; text-align: center; }
        .time-slot { cursor: pointer; }
        .time-slot:hover { background-color: #f0f0f0; }
        .workout-box { background: #fff; border: 1px solid #dee2e6; border-radius: 5px; padding: 10px; margin: 5px 0; cursor: pointer;}
        .workout-box:hover { background: #f8f9fa; }
        .program-name { font-size: 0.9rem; color: #6c757d; }
        .badge { padding: 5px 10px; border-radius: 10px; }
        .modal-content { max-width: 600px; width: 90%; margin: 10% auto; }
        .exercise-item { margin-bottom: 15px; padding: 10px; border-bottom: 1px solid #eee; }
        .exercise-details { display: flex; gap: 20px; margin-top: 5px; }
        .video-container { margin: 10px 0; }
        .video-container iframe { width: 100%; height: 200px; }
        .modal-body { max-height: 60vh; overflow-y: auto; }
        .exercise-label {
            display: block;
            margin-bottom: 12px;
            padding: 10px 15px;
            background-color: #f9f9f9;
            border-left: 4px solid #0d6efd;
            border-radius: 5px;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            transition: background 0.2s;
        }

        .exercise-label:hover {
            background-color: #eef5ff;
        }

        .exercise-name {
            display: block;
            font-size: 1.1em;
            font-weight: bold;
            color: #0d6efd;
            margin-bottom: 4px;
        }

        .exercise-meta {
            display: block;
            font-size: 0.95em;
            color: #333;
        }

        .exercise-extra {
            display: block;
            font-size: 0.85em;
            color: #555;
            font-style: italic;
            margin-top: 3px;
        }

    </style>
</head>
<body>
    <div class="container">
            <h2>Weekly Timetable</h2>
            <c:if test="${not empty message}">
                <c:choose>
                    <c:when test="${message == 'success'}">
                        <div class="alert alert-success alert-dismissible fade show">
                            Workout added successfully!
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger alert-dismissible fade show">
                            Failed to add or update workout!
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <a class="mb-3 text-start btn btn-outline-primary" href="${pageContext.request.contextPath}/CustomerScheduleServlet?trainerId=${trainerId}">&larr; Back to list</a>

            <form action="TimetableCustomerServlet" method="get" id="weekForm" class="mb-4">
                <input type="hidden" name="customerProgramId" value="${customerProgramId}">
                <input type="hidden" name="programId" value="${programId}">
                <input type="hidden" name="trainerId" value="${trainerId}">
                <input type="hidden" name="startDate" value="${startDate}">
                <input type="hidden" name="endDate" value="${endDate}">
                <input type="hidden" name="scheduleId" value="${scheduleId}">
                <div class="row g-2 align-items-center">
                    <div class="col-auto">
                        <label for="weekRangeSelect" class="form-label"><strong>Choose Week:</strong></label>
                    </div>
                    <div class="col-auto">
                        <select id="weekRangeSelect" name="weekStartDate" class="form-select" onchange="this.form.submit()">
                            <c:forEach var="opt" items="${weekOptions}">
                                <option value="${opt}" ${opt == weekStartDate ? "selected" : ""}>${opt}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </form>

            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Monday</th>
                        <th>Tuesday</th>
                        <th>Wednesday</th>
                        <th>Thursday</th>
                        <th>Friday</th>
                        <th>Saturday</th>
                        <th>Sunday</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <c:forEach var="dayDate" items="${weekDates}" varStatus="status">
                            <td class="time-slot" key="${dayDate}">
                                <c:set var="hasExercise" value="false" />
                                <c:forEach var="schedule" items="${schedules}">
                                    <c:if test="${not empty schedule.date && schedule.date.toLocalDate() eq dayDate && not empty schedule.exercises && fn:length(schedule.exercises) > 0}">
                                        <c:set var="hasExercise" value="true" />
                                        <div class="workout-box">
                                            <strong>${schedule.programName}</strong><br/>
                                            <span class="program-name">👤 ${schedule.customerName}</span><br/>
                                                <ul class="list-group">
                                                    <c:forEach var="exercise" items="${schedule.exercises}">
                                                        <li class="list-group-item">
                                                            <h6>${exercise.exerciseName}</h6>
                                                            <div class="d-flex justify-content-between">
                                                                 <div class="d-flex flex-column">
                                                                    <small>Sets: ${exercise.sets}</small>
                                                                    <small>Reps: ${exercise.reps}</small>
                                                                    <small>Rest: ${exercise.restTimeSeconds}s</small>
                                                                 </div>
                                                                 <div class="d-flex flex-column">
                                                                    <small>Muscle: ${exercise.muscleGroup}</small>
                                                                    <small>Equipment: ${exercise.equipment}</small>
                                                                    <small>Date: ${schedule.date}</small>
                                                                 </div>
                                                            </div>
                                                            <small>${schedule.startTime} - ${schedule.endTime}</small>
                                                            <p>${now}</p>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            <c:choose>
                                                <c:when test="${schedule.status == 'completed'}">
                                                    <span class="badge bg-success">Completed &#10003;</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger">Not complete yet &#10007;</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </c:if>
                                </c:forEach>
                                            <c:if test="${not hasExercise}">
                                                <span class="font-bold text-secondary">Empty</span>
                                            </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </tbody>
            </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>