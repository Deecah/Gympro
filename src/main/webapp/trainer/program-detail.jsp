<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    Program program = (Program) request.getAttribute("program");
    List<ProgramWeek> weeks = (List<ProgramWeek>) request.getAttribute("weeks");
    Map<Integer, List<ProgramDay>> daysMap = (Map<Integer, List<ProgramDay>>) request.getAttribute("daysMap");
    Map<Integer, List<Workout>> dayWorkouts = (Map<Integer, List<Workout>>) request.getAttribute("dayWorkouts");
    Map<Integer, List<ExerciseLibrary>> workoutExercises = (Map<Integer, List<ExerciseLibrary>>) request.getAttribute("workoutExercises");
    List<ExerciseLibrary> exerciseList = (List<ExerciseLibrary>) request.getAttribute("exerciseList");
    List<User> customers = (List<User>) request.getAttribute("customers");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Program Details - <%= program.getName() %></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/programs.css">
    <style>
        .week-container { margin-bottom: 20px; }
        .day-container { margin-left: 20px; border-left: 2px solid #ddd; padding-left: 10px; }
        .workout-container { margin-left: 20px; }
        .exercise-table { margin-top: 10px; }
        .video-link { cursor: pointer; color: #007bff; }
        .video-link:hover { text-decoration: underline; }
    </style>
    <script src="../js/notification.js"></script>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar bg-dark text-white">
            <jsp:include page="sidebar.jsp" />
        </div>

        <!-- Main Content -->
        <div class="flex-grow-1 p-4">
            <h1 class="mb-3"><i class="fas fa-dumbbell me-2"></i><%= program.getName() %></h1>
            <p class="text-muted mb-4"><%= program.getDescription() != null ? program.getDescription() : "No description" %></p>

            <!-- Program Exercises (from ExerciseProgram) -->
            <div class="mb-4">
                <h3>Program Exercises</h3>
                <% if (exerciseList != null && !exerciseList.isEmpty()) { %>
                    <table class="table table-striped exercise-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Sets</th>
                                <th>Reps</th>
                                <th>Rest (s)</th>
                                <th>Description</th>
                                <th>Video</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (ExerciseLibrary exercise : exerciseList) { %>
                                <tr>
                                    <td><%= exercise.getName() %></td>
                                    <td><%= exercise.getSets() %></td>
                                    <td><%= exercise.getReps() %></td>
                                    <td><%= exercise.getRestTimeSeconds() %></td>
                                    <td><%= exercise.getDescription() != null ? exercise.getDescription() : "No description" %></td>
                                    <td>
                                        <% if (exercise.getVideoURL() != null && !exercise.getVideoURL().isEmpty()) { %>
                                            <a href="#" class="video-link" onclick="openVideoModal('<%= exercise.getVideoURL() %>', '<%= exercise.getName() %>')">View Video</a>
                                        <% } else { %>
                                            No video
                                        <% } %>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } else { %>
                    <p>No exercises added to this program.</p>
                <% } %>
                <button class="btn btn-primary" onclick="openAddExerciseModal(<%= program.getProgramId() %>)">
                    <i class="fas fa-dumbbell me-2"></i>Add Exercise
                </button>
            </div>

            <!-- Weekly Schedule -->
            <h3>Weekly Schedule</h3>
            <% if (weeks != null && !weeks.isEmpty()) { %>
                <% for (ProgramWeek week : weeks) { %>
                    <div class="week-container">
                        <h4>Week <%= week.getWeekNumber() %></h4>
                        <% List<ProgramDay> days = daysMap.get(week.getWeekId());
                           if (days != null) {
                               for (ProgramDay day : days) { %>
                                   <div class="day-container">
                                       <h5>Day <%= day.getDayNumber() %> (<% out.print(java.time.DayOfWeek.of(day.getDayNumber()).name()); %>)</h5>
                                       <% List<Workout> workouts = dayWorkouts.get(day.getDayId());
                                          if (workouts != null) {
                                              for (Workout workout : workouts) { %>
                                                  <div class="workout-container">
                                                      <h6><%= workout.getTitle() %></h6>
                                                      <p>
                                                          <% if (workout.getStartTime() != null) { %>
                                                              <strong>Time:</strong> <%= workout.getStartTime() %> - <%= workout.getEndTime() != null ? workout.getEndTime() : "N/A" %><br>
                                                          <% } %>
                                                          <strong>Notes:</strong> <%= workout.getNotes() != null ? workout.getNotes() : "No notes" %>
                                                      </p>
                                                      <% List<ExerciseLibrary> exercises = workoutExercises.get(workout.getWorkoutId());
                                                         if (exercises != null && !exercises.isEmpty()) { %>
                                                             <table class="table table-striped exercise-table">
                                                                 <thead>
                                                                     <tr>
                                                                         <th>Name</th>
                                                                         <th>Sets</th>
                                                                         <th>Reps</th>
                                                                         <th>Rest (s)</th>
                                                                         <th>Description</th>
                                                                         <th>Video</th>
                                                                     </tr>
                                                                 </thead>
                                                                 <tbody>
                                                                     <% for (ExerciseLibrary exercise : exercises) { %>
                                                                         <tr>
                                                                             <td><%= exercise.getName() %></td>
                                                                             <td><%= exercise.getSets() %></td>
                                                                             <td><%= exercise.getReps() %></td>
                                                                             <td><%= exercise.getRestTimeSeconds() %></td>
                                                                             <td><%= exercise.getDescription() != null ? exercise.getDescription() : "No description" %></td>
                                                                             <td>
                                                                                 <% if (exercise.getVideoURL() != null && !exercise.getVideoURL().isEmpty()) { %>
                                                                                     <a href="#" class="video-link" onclick="openVideoModal('<%= exercise.getVideoURL() %>', '<%= exercise.getName() %>')">View Video</a>
                                                                                 <% } else { %>
                                                                                     No video
                                                                                 <% } %>
                                                                             </td>
                                                                         </tr>
                                                                     <% } %>
                                                                 </tbody>
                                                             </table>
                                                         <% } else { %>
                                                             <p>No exercises scheduled for this workout.</p>
                                                         <% } %>
                                                  </div>
                                              <% } %>
                                          <% } else { %>
                                              <p>No workouts scheduled for this day.</p>
                                          <% } %>
                                   </div>
                               <% } %>
                           <% } else { %>
                               <p>No days scheduled for this week.</p>
                           <% } %>
                    </div>
                <% } %>
            <% } else { %>
                <p>No weeks scheduled for this program.</p>
            <% } %>

            <!-- Assign Program -->
            <div class="mt-4">
                <button class="btn btn-success" onclick="openAssignModal(<%= program.getProgramId() %>, '<%= program.getName() %>', '<%= program.getDescription() != null ? program.getDescription() : "" %>')">
                    <i class="fas fa-paper-plane me-2"></i>Assign Program
                </button>
            </div>
        </div>
    </div>

    <!-- Add Exercise Modal -->
    <div class="modal fade" id="addExerciseModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <form id="addExerciseForm" action="<%= request.getContextPath() %>/AddExerciseToProgramServlet" method="post" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-dumbbell me-2"></i>Add Exercises to Program</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="programId" id="addExerciseProgramId" />
                    <div id="exerciseList">
                        <div class="exercise-entry">
                            <div class="mb-2">
                                <label class="form-label">Exercise</label>
                                <select name="exerciseLibraryIds" class="form-select exercise-select" required onchange="previewExercise(this)">
                                    <option value="">Select an exercise...</option>
                                </select>
                            </div>
                            <div class="mb-2 preview">
                                <iframe class="exercise-preview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
                                <p class="exercise-description text-muted mt-1"></p>
                                <p class="exercise-details text-muted mt-1"></p>
                            </div>
                            <div class="mb-2">
                                <button type="button" class="btn btn-danger remove-btn" onclick="removeExerciseEntry(this)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <button type="button" class="btn btn-outline-primary mt-2" onclick="addExerciseEntry()">
                        <i class="fas fa-plus me-2"></i>Add Another Exercise
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-check me-2"></i>Add Exercises
                    </button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Assign Program Modal -->
    <div class="modal fade" id="assignProgramModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <form action="<%= request.getContextPath() %>/AssignProgramServlet" method="post" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title"><i class="fas fa-paper-plane me-2"></i>Assign Program</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="programId" id="assignProgramId" />
                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-info-circle me-2"></i>Program Information</label>
                        <div class="alert program-info-alert">
                            <strong id="programName"></strong><br>
                            <span id="programDescription"></span>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-users me-2"></i>Select Customer</label>
                        <% if (customers != null && !customers.isEmpty()) { %>
                            <select name="customerId" class="form-select" required>
                                <option value="">Choose a customer...</option>
                                <% for (User customer : customers) { %>
                                    <option value="<%= customer.getUserId() %>">
                                        <%= customer.getUserName() %> (<%= customer.getEmail() %>)
                                    </option>
                                <% } %>
                            </select>
                        <% } else { %>
                            <div class="alert customer-warning-alert">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <strong>No customers found!</strong><br>
                                You don't have any active contracts with customers yet.
                            </div>
                            <select name="customerId" class="form-select" disabled>
                                <option value="">No customers available</option>
                            </select>
                        <% } %>
                    </div>
                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-calendar me-2"></i>Start Date</label>
                        <input type="date" name="startDate" class="form-control" required
                               min="<%= java.time.LocalDate.now() %>" />
                    </div>
                </div>
                <div class="modal-footer">
                    <% if (customers != null && !customers.isEmpty()) { %>
                        <button class="btn btn-success" type="submit">
                            <i class="fas fa-check me-2"></i>Assign Program
                        </button>
                    <% } else { %>
                        <button class="btn btn-success" type="submit" disabled>
                            <i class="fas fa-check me-2"></i>Assign Program
                        </button>
                    <% } %>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openAddExerciseModal(programId) {
            document.getElementById('addExerciseProgramId').value = programId;
            fetch('<%= request.getContextPath() %>/GetAvailableExercisesServlet?programId=' + programId)
                .then(response => response.json())
                .then(data => {
                    var select = document.querySelector('#addExerciseModal .exercise-select');
                    select.innerHTML = '<option value="">Select an exercise...</option>';
                    data.forEach(exercise => {
                        var option = document.createElement('option');
                        option.value = exercise.exerciseID;
                        option.textContent = exercise.name;
                        option.dataset.video = exercise.videoURL || '';
                        option.dataset.description = exercise.description || 'No description.';
                        option.dataset.sets = exercise.sets;
                        option.dataset.reps = exercise.reps;
                        option.dataset.rest = exercise.restTimeSeconds;
                        select.appendChild(option);
                    });
                    document.getElementById('exerciseList').innerHTML = document.querySelector('.exercise-entry').outerHTML;
                    new bootstrap.Modal(document.getElementById('addExerciseModal')).show();
                })
                .catch(error => {
                    console.error('Error fetching exercises:', error);
                    showNotification('Error loading exercises', 'error');
                });
        }

        function addExerciseEntry() {
            var exerciseList = document.getElementById('exerciseList');
            var entry = document.createElement('div');
            entry.className = 'exercise-entry';
            entry.innerHTML = `
                <div class="mb-2">
                    <label class="form-label">Exercise</label>
                    <select name="exerciseLibraryIds" class="form-select exercise-select" required onchange="previewExercise(this)">
                        <option value="">Select an exercise...</option>
                    </select>
                </div>
                <div class="mb-2 preview">
                    <iframe class="exercise-preview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
                    <p class="exercise-description text-muted mt-1"></p>
                    <p class="exercise-details text-muted mt-1"></p>
                </div>
                <div class="mb-2">
                    <button type="button" class="btn btn-danger remove-btn" onclick="removeExerciseEntry(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            `;
            exerciseList.appendChild(entry);
            var firstSelect = document.querySelector('.exercise-select');
            var newSelect = entry.querySelector('.exercise-select');
            newSelect.innerHTML = firstSelect.innerHTML;
        }

        function removeExerciseEntry(button) {
            if (document.querySelectorAll('.exercise-entry').length > 1) {
                button.closest('.exercise-entry').remove();
            } else {
                alert('At least one exercise is required.');
            }
        }

        function previewExercise(select) {
            var entry = select.closest('.exercise-entry');
            var iframe = entry.querySelector('.exercise-preview');
            var desc = entry.querySelector('.exercise-description');
            var details = entry.querySelector('.exercise-details');
            var option = select.selectedOptions[0];
            if (option.dataset.video) {
                iframe.src = option.dataset.video;
                iframe.style.display = 'block';
            } else {
                iframe.src = '';
                iframe.style.display = 'none';
            }
            desc.textContent = option.dataset.description || 'No description.';
            details.textContent = 'Sets: ' + (option.dataset.sets || 'N/A') +
                                 ', Reps: ' + (option.dataset.reps || 'N/A') +
                                 ', Rest: ' + (option.dataset.rest || '0') + 's';
        }

        function openVideoModal(videoUrl, title) {
            var frame = document.createElement('iframe');
            frame.src = videoUrl;
            frame.width = '100%';
            frame.height = '400';
            frame.frameborder = '0';
            frame.allowFullscreen = true;
            frame.style.borderRadius = '10px';
            var modal = new bootstrap.Modal(document.createElement('div'));
            modal._element.innerHTML = `
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body text-center">
                            ${frame.outerHTML}
                        </div>
                    </div>
                </div>
            `;
            document.body.appendChild(modal._element);
            modal.show();
            modal._element.addEventListener('hidden.bs.modal', function () {
                modal._element.remove();
            });
        }

        function openAssignModal(programId, name, description) {
            document.getElementById('assignProgramId').value = programId;
            document.querySelector('#assignProgramModal .modal-title').innerHTML =
                '<i class="fas fa-paper-plane me-2"></i>Assign Program: ' + name;
            document.getElementById('programName').textContent = name;
            document.getElementById('programDescription').textContent = description || 'No description';
            new bootstrap.Modal(document.getElementById('assignProgramModal')).show();
        }

        <% if (request.getParameter("error") != null) { %>
            showNotification('Error: <%= request.getParameter("error") %>', 'error');
        <% } %>
        <% if (request.getParameter("success") != null) { %>
            showNotification('Success: <%= request.getParameter("success") %>', 'success');
        <% } %>
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>