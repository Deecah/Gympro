<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <% // Đảm bảo weekStartDate có giá trị mặc định nếu không được truyền String
                weekStartDate=request.getParameter("weekStartDate"); if (weekStartDate==null || weekStartDate.isEmpty())
                { weekStartDate=java.time.LocalDate.now().with(java.time.DayOfWeek.MONDAY).toString(); }
                pageContext.setAttribute("weekStartDate", weekStartDate); java.time.LocalDate
                now=java.time.LocalDate.now(); pageContext.setAttribute("now", now); %>
                <!DOCTYPE html>
                <html>

                <head>
                    <title>Timetable</title>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
                        rel="stylesheet">
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                        rel="stylesheet">
                    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/package-trainer.css">
                    <style>
                        table {
                            border-collapse: collapse;
                            width: 100%;
                        }

                        th,
                        td {
                            border: 1px solid #dee2e6;
                            padding: 10px;
                            text-align: center;
                        }

                        .time-slot {
                            cursor: pointer;
                        }

                        .time-slot:hover {
                            background-color: #f0f0f0;
                        }

                        .workout-box {
                            background: #fff;
                            border: 1px solid #dee2e6;
                            border-radius: 5px;
                            padding: 10px;
                            margin: 5px 0;
                            cursor: pointer;
                        }

                        .workout-box:hover {
                            background: #f8f9fa;
                        }

                        .program-name {
                            font-size: 0.9rem;
                            color: #6c757d;
                        }

                        .badge {
                            padding: 5px 10px;
                            border-radius: 10px;
                        }

                        .modal-content {
                            max-width: 600px;
                            width: 90%;
                            margin: 10% auto;
                        }

                        .exercise-item {
                            margin-bottom: 15px;
                            padding: 10px;
                            border-bottom: 1px solid #eee;
                        }

                        .exercise-details {
                            display: flex;
                            gap: 20px;
                            margin-top: 5px;
                        }

                        .video-container {
                            margin: 10px 0;
                        }

                        .video-container iframe {
                            width: 100%;
                            height: 200px;
                        }

                        .modal-body {
                            max-height: 60vh;
                            overflow-y: auto;
                        }

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
                    <div class="d-flex">
                        <div class="sidebar bg-dark text-white">
                            <jsp:include page="sidebar.jsp" />
                        </div>
                        <div class="flex-grow-1 p-4 bg-light">
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
                            <c:if test="${empty schedules}">
                                <div class="alert alert-info">No workouts scheduled for this week. Click "Add" to create
                                    a new workout.</div>
                            </c:if>

                            <a class="btn btn-success mb-4"
                                href="${pageContext.request.contextPath}/ScheduleServlet?trainerId=${trainerId}">Back to
                                list</a>

                            <form action="TimetableServlet" method="get" id="weekForm" class="mb-4">
                                <input type="hidden" name="customerProgramId" value="${param.customerProgramId}">
                                <input type="hidden" name="programId" value="${programId}">
                                <input type="hidden" name="trainerId" value="${trainerId}">
                                <input type="hidden" name="startDate" value="${startDate}">
                                <input type="hidden" name="endDate" value="${endDate}">
                                <input type="hidden" name="scheduleId" value="${scheduleId}">
                                <div class="row g-2 align-items-center">
                                    <div class="col-auto">
                                        <label for="weekRangeSelect" class="form-label"><strong>Choose
                                                Week:</strong></label>
                                    </div>
                                    <div class="col-auto">
                                        <select id="weekRangeSelect" name="weekStartDate" class="form-select"
                                            onchange="this.form.submit()">
                                            <c:forEach var="opt" items="${weekOptions}">
                                                <option value="${opt}" ${opt==weekStartDate ? "selected" : "" }>${opt}
                                                </option>
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
                                                    <c:if
                                                        test="${not empty schedule.date && schedule.date.toLocalDate() eq dayDate && not empty schedule.exercises && fn:length(schedule.exercises) > 0}">
                                                        <c:set var="hasExercise" value="true" />
                                                        <div class="workout-box">
                                                            <strong>${schedule.programName}</strong><br />
                                                            <span class="program-name">👤
                                                                ${schedule.customerName}</span><br />
                                                            <ul class="list-group">
                                                                <c:forEach var="exercise" items="${schedule.exercises}">
                                                                    <li class="list-group-item">
                                                                        <h6>${exercise.exerciseName}</h6>
                                                                        <div class="d-flex justify-content-between">
                                                                            <div class="d-flex flex-column">
                                                                                <small>Sets: ${exercise.sets}</small>
                                                                                <small>Reps: ${exercise.reps}</small>
                                                                                <small>Rest:
                                                                                    ${exercise.restTimeSeconds}s</small>
                                                                            </div>
                                                                            <div class="d-flex flex-column">
                                                                                <small>Muscle:
                                                                                    ${exercise.muscleGroup}</small>
                                                                                <small>Equipment:
                                                                                    ${exercise.equipment}</small>
                                                                                <small>Date: ${schedule.date}</small>
                                                                            </div>
                                                                        </div>
                                                                        <small>${schedule.startTime} -
                                                                            ${schedule.endTime}</small>
                                                                        <p>${now}</p>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                            <c:choose>
                                                                <c:when test="${schedule.status == 'completed'}">
                                                                    <span class="badge bg-success">Completed
                                                                        &#10003;</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:choose>
                                                                        <c:when
                                                                            test="${now.toString() gt schedule.date.toString()}">
                                                                            <span class="badge bg-danger">Expired
                                                                                &#10007;</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <form
                                                                                action='${pageContext.request.contextPath}/TimetableServlet'
                                                                                method="post">
                                                                                <input type="hidden" name="action"
                                                                                    id="action" value="updateStatus">
                                                                                <input type="hidden"
                                                                                    name="customerProgramId"
                                                                                    value="${customerProgramId}">
                                                                                <input type="hidden" name="programId"
                                                                                    value="${programId}">
                                                                                <input type="hidden" name="trainerId"
                                                                                    value="${trainerId}">
                                                                                <input type="hidden" name="scheduleId"
                                                                                    value="${scheduleId}">
                                                                                <input type="hidden" name="startDate"
                                                                                    value="${startDate}">
                                                                                <input type="hidden" name="endDate"
                                                                                    value="${endDate}">
                                                                                <input type="hidden"
                                                                                    name="weekStartDate"
                                                                                    value="${weekStartDate}">
                                                                                <input type="hidden" name="workoutId"
                                                                                    value="${schedule.workoutId}">
                                                                                <input type="hidden" name="status"
                                                                                    value="completed">
                                                                                <button type="submit"
                                                                                    class="btn btn-sm btn-outline-primary">Mark
                                                                                    completed</button>
                                                                            </form>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </c:if>
                                                </c:forEach>
                                                <c:choose>
                                                    <c:when test="${now.toString() le dayDate}">
                                                        <span class="badge bg-primary font-bold"
                                                            onclick="showWorkoutModal('${dayDate}')">Add &#43;</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${not hasExercise}">
                                                            <span class="font-bold text-secondary">Empty</span>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:forEach>
                                    </tr>
                                </tbody>
                            </table>

                            <!-- Add Workout Modal -->
                            <div class="modal fade" id="workoutModal" tabindex="-1" aria-labelledby="workoutModalLabel"
                                aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="workoutModalLabel">Add Workout</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <form id="workoutForm"
                                                action='${pageContext.request.contextPath}/TimetableServlet'
                                                method="post">
                                                <input type="hidden" name="scheduledDate" id="scheduledDate" value="">
                                                <input type="hidden" name="action" id="action" value="addWorkout">
                                                <input type="hidden" name="customerProgramId"
                                                    value="${customerProgramId}">
                                                <input type="hidden" name="programId" value="${programId}">
                                                <input type="hidden" name="trainerId" value="${trainerId}">
                                                <input type="hidden" name="scheduleId" value="${scheduleId}">
                                                <input type="hidden" name="startDate" value="${startDate}">
                                                <input type="hidden" name="endDate" value="${endDate}">
                                                <input type="hidden" name="weekStartDate" value="${weekStartDate}">
                                                <div class="mb-3">
                                                    <label for="startTime" class="form-label">Start Time</label>
                                                    <input type="time" class="form-control" name="startTime"
                                                        id="startTime" required>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="endTime" class="form-label">End Time</label>
                                                    <input type="time" class="form-control" name="endTime" id="endTime"
                                                        required>
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Exercises</label>
                                                    <c:if test="${empty exercisePrograms}">
                                                        <div class="alert alert-warning">No exercises available for this
                                                            program.</div>
                                                    </c:if>
                                                    <c:forEach var="exerciseProgram" items="${exercisePrograms}">
                                                        <div class="form-check">
                                                            <input class="form-check-input" type="checkbox"
                                                                name="exerciseProgramIds"
                                                                value="${exerciseProgram.exerciseProgramID}"
                                                                id="exercise_${exerciseProgram.exerciseProgramID}">
                                                            <label
                                                                class="form-check-label d-flex flex-column border border-1 border-primary rounded-2 py-2 px-3"
                                                                for="exercise_${exerciseProgram.exerciseProgramID}">
                                                                <span class="exercise-meta">
                                                                    <strong>${exerciseProgram.exerciseName}</strong>
                                                                </span>
                                                                <div class="d-flex justify-content-between m-0">
                                                                    <span class="exercise-meta">
                                                                        🏋️ Sets:
                                                                        <strong>${exerciseProgram.sets}</strong><br>
                                                                        🔁 Reps:
                                                                        <strong>${exerciseProgram.reps}</strong><br>
                                                                        ⏱️ Rest:
                                                                        <strong>${exerciseProgram.restTimeSeconds}s</strong>
                                                                    </span>
                                                                    <span class="exercise-extra">
                                                                        💪 Muscle:
                                                                        <em>${exerciseProgram.muscleGroup}</em><br>
                                                                        🧰 Equipment:
                                                                        <em>${exerciseProgram.equipment}</em>
                                                                    </span>
                                                                </div>
                                                            </label>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                        data-bs-dismiss="modal">Close</button>
                                                    <button type="submit" class="btn btn-primary">Save</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Workout Detail Modal -->
                            <div class="modal fade" id="workoutDetailModal" tabindex="-1"
                                aria-labelledby="workoutDetailModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="workoutDetailModalLabel">Workout Details</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body" id="modalContent">
                                            <!-- Content will be loaded here -->
                                        </div>
                                        <div class="modal-footer" id="modalCompleteButton">
                                            <!-- Complete button will be loaded here -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                    <script>
                        function showWorkoutModal(dayDate) {
                            document.getElementById('scheduledDate').value = dayDate;
                            console.log(document.getElementById('scheduledDate').value);
                            const modal = new bootstrap.Modal(document.getElementById('workoutModal'));
                            modal.show();
                        }

                        function addWorkout() {
                            const form = document.getElementById('workoutForm');
                            form.action = '${pageContext.request.contextPath}/TimetableServlet';
                            const formData = new FormData(form);
                            formData.set('action', 'addWorkout');

                            if (!form.checkValidity()) {
                                form.reportValidity();
                                return;
                            }

                            fetch(form.action, {
                                method: 'POST',
                                body: formData
                            })
                                .then(response => {
                                    response.text();
                                })
                                .then(data => {
                                    bootstrap.Modal.getInstance(document.getElementById('workoutModal')).hide();
                                    location.reload();
                                })
                                .catch(error => {
                                    alert('Failed to add workout');
                                });
                        }

                        function showWorkoutDetail(workoutId) {
                            if (workoutId <= 0) return;

                            fetch('workoutDetail?workoutId=' + workoutId)
                                .then(response => {
                                    if (!response.ok) throw new Error('Network response was not ok');
                                    return response.json();
                                })
                                .then(data => {
                                    displayWorkoutModal(data);
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert('An error occurred while loading workout information');
                                });
                        }

                        function displayWorkoutModal(data) {
                            const modal = document.getElementById('workoutDetailModal');
                            const content = document.getElementById('modalContent');
                            const completeButton = document.getElementById('modalCompleteButton');

                            const workout = data.workout;
                            const exercises = data.exercises;

                            completeButton.innerHTML = workout.status !== 'completed' ?
                                '<button type="button" class="btn btn-success" onclick="markCompleted(' + workout.workoutId + ', ${param.customerProgramId}); bootstrap.Modal.getInstance(document.getElementById(\'workoutDetailModal\')).hide();">Mark as Completed</button>' :
                                '';

                            let html = '<div class="workout-info">';
                            html += '<h2>' + workout.programName + '</h2>';
                            html += '<p><strong>Time:</strong> ' + workout.startTime + ' - ' + workout.endTime + '</p>';
                            html += '<p><strong>Program:</strong> ' + (workout.programName || 'N/A') + '</p>';
                            html += '<p><strong>Customer:</strong> ' + (workout.customerName || 'N/A') + '</p>';
                            html += '</div>';

                            if (exercises && exercises.length > 0) {
                                html += '<h3>Exercise List:</h3>';
                                exercises.forEach(function (exercise) {
                                    html += '<div class="exercise-item">';
                                    html += '<div class="exercise-name">' + exercise.exerciseName + '</div>';
                                    html += '<div class="exercise-details">';
                                    html += '<div class="exercise-detail"><strong>Sets:</strong> ' + exercise.sets + '</div>';
                                    html += '<div class="exercise-detail"><strong>Reps:</strong> ' + exercise.reps + '</div>';
                                    html += '<div class="exercise-detail"><strong>Rest:</strong> ' + exercise.restTimeSeconds + 's</div>';
                                    html += '<div class="exercise-detail"><strong>Muscle:</strong> ' + exercise.muscleGroup + '</div>';
                                    html += '<div class="exercise-detail"><strong>Equipment:</strong> ' + exercise.equipment + '</div>';
                                    html += '</div>';
                                    if (exercise.description) {
                                        html += '<p><strong>Description:</strong> ' + exercise.description + '</p>';
                                    }
                                    if (exercise.videoUrl) {
                                        html += '<div class="video-container">';
                                        html += '<strong>Tutorial Video:</strong>';
                                        html += '<iframe src="' + exercise.videoUrl + '" allowfullscreen></iframe>';
                                        html += '</div>';
                                    }
                                    html += '</div>';
                                });
                            } else {
                                html += '<p>No exercises in this workout.</p>';
                            }

                            content.innerHTML = html;
                            const detailModal = new bootstrap.Modal(document.getElementById('workoutDetailModal'));
                            detailModal.show();
                        }
                        function markCompleted(workoutId, customerProgramId) {
                            const formData = new FormData();
                            formData.append('action', 'updateStatus');
                            formData.append('workoutId', workoutId);
                            formData.append('status', 'completed');
                            formData.append('customerProgramId', customerProgramId);
                            formData.append('programId', '${programId}');
                            formData.append('trainerId', '${trainerId}');
                            formData.append('startDate', '${startDate}');
                            formData.append('endDate', '${endDate}');
                            formData.append('scheduleId', '${scheduleId}');
                            const path = '${pageContext.request.contextPath}/TimetableServlet';
                            fetch(path, {
                                method: 'POST',
                                body: formData
                            })
                                .then(response => response.text())
                                .then(data => {
                                    alert(data);
                                })
                                .catch(error => {
                                    console.error('Error:', error);
                                    alert('Failed to mark workout as completed');
                                });
                        }
                    </script>
                </body>

                </html>