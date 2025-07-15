<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="model.Program, model.ProgramWeek, model.ProgramDay, model.ExerciseLibrary, model.Workout" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    Program program = (Program) request.getAttribute("program");
    List<ProgramWeek> weeks = (List<ProgramWeek>) request.getAttribute("weeks");
    Map<Integer, List<ProgramDay>> daysMap = (Map<Integer, List<ProgramDay>>) request.getAttribute("daysMap");
    Map<Integer, List<Workout>> dayWorkouts = (Map<Integer, List<Workout>>) request.getAttribute("dayWorkouts");
    List<ExerciseLibrary> exerciseList = (List<ExerciseLibrary>) request.getAttribute("exerciseList");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Program Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <style>
        body { margin: 0; }
        .sidebar { width: 240px; min-height: 100vh; }
        .day-cell { border: 1px solid #dee2e6; padding: 10px; min-height: 180px; background-color: white; position: relative; cursor: pointer; }
        .week-row { margin-bottom: 10px; }
        .week-label { background-color: #f1f1f1; font-weight: bold; }
        .day-header { font-size: 0.9rem; font-weight: 500; margin-bottom: 5px; }
        .edit-icon { position: absolute; top: 5px; right: 5px; color: #6c757d; cursor: pointer; }
    </style>
</head>
<body>
<div class="d-flex">
    <div class="sidebar bg-dark text-white">
        <jsp:include page="sidebar.jsp"/>
    </div>

        <div class="flex-grow-1 p-4 bg-light">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <a href="<%= request.getContextPath() %>/ProgramServlet" class="text-decoration-none">&larr; Back to Programs</a>
                <div class="d-flex gap-2">
                    <button class="btn btn-primary">
                        <i class="bi bi-send-fill me-1"></i> Assign to client
                    </button>
                    <button type="button" class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#editProgramModal">
                        <i class="bi bi-pencil-square me-1"></i> Edit Info
                    </button>
                </div>
            </div>

            <h3><%= program.getName() %></h3>
            <p class="text-muted"><%= program.getDescription() %></p>
        <hr/>

        <div id="workoutContainer">
            <% for (ProgramWeek week : weeks) { %>
                <div class="row week-row" data-week-id="<%= week.getWeekId() %>">
                    <% List<ProgramDay> days = daysMap.get(week.getWeekId()); %>
                    <% for (ProgramDay day : days) { %>
                        <div class="col day-cell" id="day-<%= day.getDayId() %>">
                            <div class="day-header">Week <%= week.getWeekNumber() %> - Day <%= day.getDayNumber() %></div>
                            <% 
                                List<Workout> workouts = dayWorkouts.get(day.getDayId()); 
                                if (workouts != null && !workouts.isEmpty()) {
                                    for (Workout w : workouts) {
                            %>
                                        <div><strong><%= w.getTitle() %></strong> (<%= w.getRounds() %>)</div>
                                        <div><%= w.getNotes() %></div>
                                        <button class="btn btn-sm btn-outline-success mt-1" onclick="openExerciseModal(<%= w.getWorkoutID() %>)">+ Add Exercise</button>
                            <%       }
                                } else { %>
                                    <div class="text-muted">Click to add workout</div>
                            <% } %>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>

        <div class="mt-3 d-flex gap-2">
            <button class="btn btn-outline-primary" onclick="addWeek()">+ Add week</button>
            <button class="btn btn-outline-danger" onclick="removeLastWeek()">🔝 Remove last week</button>
        </div>
    </div>
</div>

<!-- Add Workout Modal -->
<div class="modal fade" id="addWorkoutModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <form action="add-workout" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add Workout</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <input type="hidden" name="dayId" id="workout-dayId" />
        <div class="mb-2">
          <label class="form-label">Title</label>
          <input name="title" class="form-control" required />
        </div>
        <div class="mb-2">
          <label class="form-label">Rounds</label>
          <input name="rounds" class="form-control" />
        </div>
        <div class="mb-2">
          <label class="form-label">Notes</label>
          <textarea name="notes" class="form-control"></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary" type="submit">Create Workout</button>
      </div>
    </form>
  </div>
</div>

<!-- Add Exercise Modal -->
<div class="modal fade" id="addExerciseModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <form action="add-exercise" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add Exercise</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <input type="hidden" name="workoutId" id="exercise-workoutId" />
        <div class="mb-2">
          <label>Choose Exercise</label>
          <select name="exerciseId" class="form-select" onchange="previewExercise(this)">
            <c:forEach var="e" items="${exerciseList}">
              <option value="${e.exerciseID}" data-video="${e.videoURL}" data-description="${e.description}">
                ${e.name}
              </option>
            </c:forEach>
          </select>
        </div>
        <div class="mb-2">
          <label>Sets</label>
          <input type="number" name="sets" class="form-control" required />
        </div>
        <div class="mb-2">
          <label>Reps</label>
          <input type="number" name="reps" class="form-control" required />
        </div>
        <div class="mb-2">
          <label>Rest Time (sec)</label>
          <input type="number" name="restTime" class="form-control" />
        </div>
        <div class="mb-2">
          <label>Notes</label>
          <textarea name="notes" class="form-control"></textarea>
        </div>
        <div class="mb-2" id="preview">
          <iframe id="exercisePreview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
          <p id="exerciseDescription" class="text-muted mt-1"></p>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-success" type="submit">Add Exercise</button>
      </div>
    </form>
  </div>
</div>
              
<!-- Edit Program Modal -->
<div class="modal fade" id="editProgramModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog">
    <form action="<%= request.getContextPath() %>/ProgramDetailServlet" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Program Info</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <input type="hidden" name="action" value="edit" />
        <input type="hidden" name="programId" value="<%= program.getProgramId() %>" />

        <div class="mb-3">
            <label class="form-label">Program Name</label>
            <input type="text" name="name" class="form-control" value="<%= program.getName() %>" required />
        </div>

        <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea name="description" class="form-control"><%= program.getDescription() %></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">Package</label>
            <select name="packageId" class="form-select">
                <option value="0">None</option>
                <c:forEach var="pkg" items="${packageList}">
                    <option value="${pkg.packageID}"
                            ${pkg.packageID == program.packageId ? 'selected' : ''}>
                        ${pkg.name}
                    </option>
                </c:forEach>
            </select>
        </div>
      </div>
      <div class="modal-footer">
          <button type="submit" class="btn btn-success">Save Changes</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
      </div>
    </form>
  </div>
</div>

<script>
  let selectedDayId = null;
  let selectedWorkoutId = null;
  const contextPath = "<%= request.getContextPath() %>";
  const programId = <%= program.getProgramId() %>;
  let weekCount = <%= weeks.size() %>;
  const container = document.getElementById("workoutContainer");

  document.querySelectorAll('.day-cell').forEach(cell => {
      cell.addEventListener('click', function () {
          selectedDayId = this.id.split('-')[1];
          document.getElementById('workout-dayId').value = selectedDayId;
          new bootstrap.Modal(document.getElementById('addWorkoutModal')).show();
      });
  });
  
  // mở modal edit program
  document.querySelector(".btn-edit-program")?.addEventListener("click", () => {
    new bootstrap.Modal(document.getElementById("editProgramModal")).show();
  });

  function openExerciseModal(workoutId) {
    selectedWorkoutId = workoutId;
    document.getElementById('exercise-workoutId').value = workoutId;
    new bootstrap.Modal(document.getElementById('addExerciseModal')).show();
  }

  function previewExercise(el) {
    const opt = el.selectedOptions[0];
    const iframe = document.getElementById("exercisePreview");
    const descBox = document.getElementById("exerciseDescription");

    if (opt.dataset.video) {
      iframe.src = opt.dataset.video;
      iframe.style.display = "block";
    } else {
      iframe.style.display = "none";
      iframe.src = "";
    }
    descBox.textContent = opt.dataset.description || "No description.";
  }

  function addWeek() {
    fetch(contextPath + '/ProgramWeekServlet', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: new URLSearchParams({
        action: 'add',
        programId: programId,
        weekNumber: weekCount + 1
      })
    })
    .then(res => res.json())
    .then(data => {
      weekCount++;
      renderWeek(weekCount, data);
    });
  }

  function removeLastWeek() {
    if (weekCount === 0) return;

    const lastWeekRow = document.querySelectorAll('.week-row')[weekCount - 1];
    const weekId = lastWeekRow.getAttribute("data-week-id");

    fetch(contextPath + '/ProgramWeekServlet', {
      method: 'POST',
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      body: new URLSearchParams({
        action: 'delete',
        weekId: weekId
      })
    })
    .then(res => res.json())
    .then(data => {
      if (data.deleted) {
        lastWeekRow.remove();
        weekCount--;
      }
    });
  }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
