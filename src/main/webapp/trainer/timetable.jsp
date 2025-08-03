<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Timetable</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid black; padding: 10px; text-align: center; }
        .time-slot { cursor: pointer; }
        .time-slot:hover { background-color: #f0f0f0; }
        #workoutModal { display: none; position: fixed; top: 20%; left: 30%; background: white; padding: 20px; border: 1px solid black; z-index: 1000; }
        .modal-backdrop { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999; }
    </style>
</head>
<body>
    <h2>Weekly Timetable</h2>
    <form id="weekForm">
        <label>Customer Program ID: <input type="number" name="customerProgramId" required></label>
        <label>Program ID: <input type="number" name="programId" required></label>
        <label>Week Start Date: <input type="date" name="weekStartDate" required></label>
        <button type="submit">Load Timetable</button>
    </form>

    <table>
        <thead>
            <tr>
                <th>Time</th>
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
            <c:forEach var="timeSlot" items="${['08:00-12:00', '13:00-17:00', '18:00-22:00']}">
                <tr>
                    <td>${timeSlot}</td>
                    <c:forEach var="day" items="${['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']}">
                        <td class="time-slot" onclick="showModal('${day}', '${timeSlot}')">
                            <c:forEach var="schedule" items="${schedules}">
                                <c:if test="${schedule.scheduledDate.dayOfWeek.name() == day.toUpperCase()}">
                                    ${schedule.startTime} - ${schedule.endTime} (${schedule.status})
                                </c:if>
                            </c:forEach>
                            Add
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- Modal Backdrop -->
    <div class="modal-backdrop" id="modalBackdrop"></div>

    <!-- Modal -->
    <div id="workoutModal">
        <h3>Add Workout</h3>
        <form id="workoutForm">
            <input type="hidden" name="dayId" id="dayId">
            <input type="hidden" name="programDayId" id="programDayId">
            <input type="hidden" name="scheduledDate" id="scheduledDate">
            <input type="hidden" name="customerProgramId" value="${param.customerProgramId}">
            <input type="hidden" name="programId" value="${param.programId}">
            <input type="hidden" name="trainerId" value="1"> <!-- Giả định trainerId -->
            <label>Start Time: <input type="time" name="startTime" required></label><br>
            <label>End Time: <input type="time" name="endTime" required></label><br>
            <label>Title: <input type="text" name="title" required></label><br>
            <label>Notes: <textarea name="notes"></textarea></label><br>
            <label>Exercises:</label><br>
            <c:forEach var="exerciseProgram" items="${exercisePrograms}">
                <input type="checkbox" name="exerciseProgramIds" value="${exerciseProgram.exerciseProgramID}">
                Exercise ${exerciseProgram.exerciseLibraryID} (ProgramID: ${exerciseProgram.programID})<br>
            </c:forEach>
            <button type="button" onclick="submitWorkout()">Save</button>
            <button type="button" onclick="closeModal()">Close</button>
        </form>
    </div>

    <script>
        document.getElementById('weekForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            window.location.href = `/TimetableServlet?customerProgramId=${formData.get('customerProgramId')}&programId=${formData.get('programId')}&weekStartDate=${formData.get('weekStartDate')}`;
        });

        function showModal(day, timeSlot) {
            document.getElementById('dayId').value = getDayId(day);
            document.getElementById('programDayId').value = getProgramDayId(day);
            document.getElementById('scheduledDate').value = getScheduledDate(day);
            document.getElementById('workoutModal').style.display = 'block';
            document.getElementById('modalBackdrop').style.display = 'block';
        }

        function closeModal() {
            document.getElementById('workoutModal').style.display = 'none';
            document.getElementById('modalBackdrop').style.display = 'none';
        }

        function submitWorkout() {
            const form = document.getElementById('workoutForm');
            const formData = new FormData(form);
            formData.append('action', 'addWorkout');

            fetch('/TimetableServlet', {
                method: 'POST',
                body: formData
            })
            .then(response => response.text())
            .then(data => {
                alert(data);
                closeModal();
                location.reload();
            })
            .catch(error => console.error('Error:', error));
        }

        function getDayId(day) {
            const days = { 'Monday': 1, 'Tuesday': 2, 'Wednesday': 3, 'Thursday': 4, 'Friday': 5, 'Saturday': 6, 'Sunday': 7 };
            return days[day] || 1;
        }

        function getProgramDayId(day) {
            return getDayId(day); // Giả định ánh xạ tương tự
        }

        function getScheduledDate(day) {
            const weekStart = new Date(document.getElementById('weekForm').weekStartDate.value);
            const days = { 'Monday': 0, 'Tuesday': 1, 'Wednesday': 2, 'Thursday': 3, 'Friday': 4, 'Saturday': 5, 'Sunday': 6 };
            weekStart.setDate(weekStart.getDate() + days[day]);
            return weekStart.toISOString().split('T')[0];
        }
    </script>
</body>
</html>