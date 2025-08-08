<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý chương trình tập</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .day-cell { border:1px solid #ccc;padding:10px;min-height:80px;cursor:pointer;background:#f6f6f6;}
        .day-cell:hover { background:#e9ecef;}
        .workout-card {margin-top:10px;background:#fff;padding:10px;border-radius:5px;border:1px solid #e3e3e3;}
        .exercise-list li {margin-bottom:5px;}
    </style>
</head>
<body>
<div class="container mt-4">
    <h2>Quản lý chương trình tập</h2>
    <!-- Lịch các ngày trong tuần -->
    <div class="row mb-4">
        <h4>Lịch chương trình</h4>
        <div class="d-flex flex-row flex-wrap">
            <c:forEach var="day" items="${weekDays}">
                <div class="day-cell me-2 mb-2" data-day-id="${day.dayID}" onclick="handleDayClick(this)">
                    <strong>Ngày ${day.dayNumber}</strong><br/>
                    <c:forEach var="workout" items="${day.workouts}">
                        <div class="workout-card" onclick="openExerciseModal(${workout.workoutID});event.stopPropagation();">
                            <strong>Buổi: ${workout.workoutLibrary.title}</strong><br/>
                            Giờ: ${workout.startTime} - ${workout.endTime}
                            <div>
                                <ul class="exercise-list">
                                    <c:forEach var="ex" items="${workout.exercises}">
                                        <li><strong>${ex.exerciseName}</strong> - ${ex.sets} sets x ${ex.reps} reps</li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </c:forEach>
                    <c:if test="${empty day.workouts}">
                        <span style="color: #888">Chưa có buổi tập</span>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Modal tạo buổi tập -->
<div class="modal fade" id="addWorkoutModal" tabindex="-1">
  <div class="modal-dialog">
    <form method="post" action="WorkoutServlet">
      <input type="hidden" name="action" value="add">
      <input type="hidden" id="workout-dayId" name="dayId">
      <div class="modal-content">
        <div class="modal-header"><h5 class="modal-title">Tạo buổi tập mới</h5></div>
        <div class="modal-body">
          <label>Chọn buổi từ thư viện:</label>
          <select name="workoutLibraryID" class="form-control" required>
            <c:forEach var="lib" items="${workoutLibraries}">
              <option value="${lib.workoutLibraryID}">${lib.title}</option>
            </c:forEach>
          </select>
          <label>Thời gian bắt đầu:</label>
          <input type="time" name="startTime" required class="form-control">
          <label>Thời gian kết thúc:</label>
          <input type="time" name="endTime" required class="form-control">
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">Tạo buổi tập</button>
        </div>
      </div>
    </form>
  </div>
</div>

<!-- Modal thêm bài tập vào buổi tập -->
<div class="modal fade" id="addExerciseModal" tabindex="-1">
  <div class="modal-dialog">
    <form method="post" action="ExerciseServlet">
      <input type="hidden" name="action" value="add">
      <input type="hidden" id="exercise-workoutId" name="workoutId">
      <div class="modal-content">
        <div class="modal-header"><h5 class="modal-title">Thêm bài tập vào buổi tập</h5></div>
        <div class="modal-body">
          <label>Tên bài tập:</label>
          <input type="text" name="exerciseName" required class="form-control">
          <label>Số set:</label>
          <input type="number" name="sets" min="1" required class="form-control">
          <label>Số rep:</label>
          <input type="number" name="reps" min="1" required class="form-control">
          <label>Thời gian nghỉ (giây):</label>
          <input type="number" name="restTimeSeconds" min="0" required class="form-control">
          <label>Ghi chú:</label>
          <textarea name="notes" class="form-control"></textarea>
          <label>Video URL:</label>
          <input type="text" name="videoUrl" class="form-control">
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-success">Thêm bài tập</button>
        </div>
      </div>
    </form>
  </div>
</div>

<script>
function handleDayClick(cell) {
    const dayId = cell.getAttribute("data-day-id");
    document.getElementById("workout-dayId").value = dayId;
    new bootstrap.Modal(document.getElementById("addWorkoutModal")).show();
}
function openExerciseModal(workoutId) {
    document.getElementById('exercise-workoutId').value = workoutId;
    new bootstrap.Modal(document.getElementById('addExerciseModal')).show();
}
</script>
</body>
</html>