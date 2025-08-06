<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ExerciseLibrary" %>
<%@ page import="model.Workout" %>
<%
    List<ExerciseLibrary> exerciseLibraries = (List<ExerciseLibrary>) request.getAttribute("exerciseLibraries");
    List<Workout> workouts = (List<Workout>) request.getAttribute("workouts");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Schedule Workout</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css" rel="stylesheet">
    <style>
        .sidebar {
            width: 240px;
            min-height: 100vh;
            background-color: #343a40;
        }
        .calendar-container {
            max-width: 900px;
            margin: 20px auto;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        #calendar {
            max-width: 100%;
        }
    </style>
    <script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js"></script>
    <script>
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
            <jsp:include page="sidebar.jsp"/>
        </div>

        <!-- Main Content -->
        <div class="flex-grow-1 p-4">
            <div class="calendar-container">
                <h3 class="mb-4"><i class="fas fa-calendar-alt me-2"></i>Schedule Workouts</h3>
                <div id="calendar"></div>
            </div>

            <!-- Modal for Adding Workout -->
            <div class="modal fade" id="scheduleWorkoutModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <form action="<%= request.getContextPath() %>/AddWorkoutToScheduleServlet" method="post" class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="fas fa-calendar-plus me-2"></i>Schedule Workout</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="programId" value="<%= request.getParameter("programId") %>"/>
                            <div class="mb-3">
                                <label class="form-label"><i class="fas fa-dumbbell me-2"></i>Workout Title</label>
                                <input type="text" name="title" class="form-control" required/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label"><i class="fas fa-list me-2"></i>Select Exercise from Library</label>
                                <select name="workoutLibraryId" class="form-select" required>
                                    <c:forEach var="exercise" items="${exerciseLibraries}">
                                        <option value="${exercise.exerciseID}">${exercise.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label"><i class="fas fa-calendar-alt me-2"></i>Workout Date</label>
                                <input type="date" name="workoutDate" class="form-control" required readonly/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label"><i class="fas fa-clock me-2"></i>Start Time</label>
                                <input type="time" name="startTime" class="form-control" required/>
                            </div>
                            <div class="mb-3">
                                <label class="form-label"><i class="fas fa-clock me-2"></i>End Time</label>
                                <input type="time" name="endTime" class="form-control" required/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="submit" class="btn btn-success"><i class="fas fa-check me-2"></i>Schedule Workout</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                <i class="fas fa-times me-2"></i>Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var calendarEl = document.getElementById('calendar');
            var calendar = new FullCalendar.Calendar(calendarEl, {
                initialView: 'dayGridMonth',
                events: [
                    <c:forEach var="workout" items="${workouts}">
                        {
                            title: '${workout.title}',
                            start: '${workout.startTime}',
                            end: '${workout.endTime}',
                            extendedProps: {
                                workoutId: ${workout.workoutID}
                            }
                        },
                    </c:forEach>
                ],
                dateClick: function(info) {
                    document.querySelector('input[name="workoutDate"]').value = info.dateStr;
                    var modal = new bootstrap.Modal(document.getElementById('scheduleWorkoutModal'));
                    modal.show();
                },
                eventClick: function(info) {
                    // Handle event click if needed
                    alert('Workout: ' + info.event.title);
                }
            });
            calendar.render();
        });
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>