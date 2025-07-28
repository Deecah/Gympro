<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="model.Exercise" %>
<%@ page import="model.Program, model.ProgramWeek, model.ProgramDay, model.ExerciseLibrary, model.Workout" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.User" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    Program program = (Program) request.getAttribute("program");
    List<ProgramWeek> weeks = (List<ProgramWeek>) request.getAttribute("weeks");
    Map<Integer, List<ProgramDay>> daysMap = (Map<Integer, List<ProgramDay>>) request.getAttribute("daysMap");
    Map<Integer, List<Workout>> dayWorkouts = (Map<Integer, List<Workout>>) request.getAttribute("dayWorkouts");
    Map<Integer, List<Exercise>> workoutExercises =(Map<Integer, List<Exercise>>) request.getAttribute("workoutExercises");
    List<ExerciseLibrary> exerciseList = (List<ExerciseLibrary>) request.getAttribute("exerciseList");
    ArrayList<User> customers = (ArrayList<User>) request.getAttribute("customers");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Program Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/program-detail.css" type="text/css">

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
                        <button class="btn btn-primary" onclick="openAssignModal(<%= program.getProgramId() %>)">
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
                        <div class="col day-cell"
                             id="day-<%= day.getDayId() %>"
                             data-day-id="<%= day.getDayId() %>"
                             data-has-workout="<%= (dayWorkouts.get(day.getDayId()) != null && !dayWorkouts.get(day.getDayId()).isEmpty()) %>"
                             onclick="handleDayClick(this)">
                            <div class="day-header mb-2">Week <%= week.getWeekNumber() %> - Day <%= day.getDayNumber() %></div>

                            <div class="d-flex flex-column gap-2">
                                <%
                                  List<Workout> workouts = dayWorkouts.get(day.getDayId());
                                  if (workouts != null && !workouts.isEmpty()) {
                                    for (Workout w : workouts) {
                                %>
                                <div class="card workout-card shadow-sm">
                                    <div class="card-body p-2">
                                        <!-- Title + 3 dots -->
                                        <div class="d-flex justify-content-between align-items-center mb-1">
                                            <span class="fw-bold">🏋️ <%= w.getTitle() %></span>
                                            <div class="dropdown">
                                                <button class="btn btn-sm btn-light"
                                                        type="button"
                                                        id="dropdownWorkout<%= w.getWorkoutID() %>"
                                                        data-bs-toggle="dropdown"
                                                        aria-expanded="false"
                                                        onclick="event.stopPropagation();">
                                                    <i class="fas fa-ellipsis-v"></i>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-end"
                                                    aria-labelledby="dropdownWorkout<%= w.getWorkoutID() %>"
                                                    onclick="event.stopPropagation();">
                                                    <li>
                                                        <button class="dropdown-item"
                                                                onclick="openEditWorkoutModal(<%= w.getWorkoutID() %>)">
                                                            <i class="fas fa-pen me-2"></i> Edit Workout
                                                        </button>
                                                    </li>
                                                    <li>
                                                        <button class="dropdown-item text-danger"
                                                                onclick="deleteWorkout(<%= w.getWorkoutID() %>)">
                                                            <i class="fas fa-trash me-2"></i> Delete Workout
                                                        </button>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>

                                        <!-- Time -->
                                        <div class="small text-muted text-center mb-1">
                                            ⏰ <%= w.getStartTime() %> - <%= w.getEndTime() %>
                                        </div>

                                        <!-- Notes -->
                                        <% if (w.getNotes() != null && !w.getNotes().isBlank()) { %>
                                        <div class="small text-body text-center">📝 <%= w.getNotes() %></div>
                                        <% } %>

                                        <!-- Exercises Section -->
                                        <hr class="my-2" />
                                        <%
                                            List<Exercise> exercises = workoutExercises.get(w.getWorkoutID());
                                            if (exercises != null && !exercises.isEmpty()) {
                                        %>
                                        <ul class="list-group list-group-flush small">
                                            <% for (Exercise ex : exercises) { %>
                                            <li class="list-group-item">
                                                <div class="fw-semibold">
                                                    <a href="#" class="text-decoration-none" onclick="event.stopPropagation(); openVideoModal('<%= ex.getVideoURL() %>', '<%= ex.getExerciseName() %>')">
                                                        📺 <%= ex.getExerciseName() %>
                                                    </a>
                                                </div>
                                                <div class="text-muted">
                                                    Sets: <%= ex.getSets() %>,
                                                    Reps: <%= ex.getReps() %>,
                                                    Rest: <%= ex.getRestTimeSeconds() %>s
                                                    <% if (ex.getNotes() != null && !ex.getNotes().isBlank()) { %>
                                                    <br>📝 <%= ex.getNotes() %>
                                                    <% } %>
                                                </div>
                                            </li>
                                            <% } %>
                                        </ul>
                                        <% } else { %>
                                        <div class="text-muted fst-italic small text-center">No exercises added yet</div>
                                        <% } %>

                                        <!-- Bottom action buttons -->
                                        <div class="workout-actions text-center mt-3">
                                            <button class="btn btn-sm btn-outline-success me-1"
                                                    title="Add Exercise"
                                                    onclick="event.stopPropagation(); openExerciseModal(<%= w.getWorkoutID() %>)">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-primary me-1"
                                                    title="Edit Exercise"
                                                    onclick="event.stopPropagation(); openEditExerciseModal(<%= exercises.isEmpty() ? 0 : exercises.get(0).getExerciseId() %>, <%= w.getWorkoutID() %>)"
                                                    <%= exercises.isEmpty() ? "disabled" : "" %>>
                                                <i class="fas fa-pen"></i>
                                            </button>
                                            <button class="btn btn-sm btn-outline-danger"
                                                    title="Delete Exercise"
                                                    onclick="event.stopPropagation(); deleteExercise(<%= exercises.isEmpty() ? 0 : exercises.get(0).getExerciseId() %>)"
                                                    <%= exercises.isEmpty() ? "disabled" : "" %>>
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <% } // end for workout %>
                                <% } else { %>
                                <div class="text-muted fst-italic">Click to add workout</div>
                                <% } %>
                            </div>
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
                <form action="AddWorkoutServlet" method="post" class="modal-content" id="addWorkoutForm">
                    <div class="modal-header">
                        <h5 class="modal-title">Add Workout</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="dayId" id="workout-dayId" />
                        
                        <!-- Alert for validation errors -->
                        <div id="workoutValidationAlert" class="alert alert-danger" style="display: none;">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <span id="workoutValidationMessage"></span>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Title</label>
                            <input name="title" class="form-control" required />
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Notes</label>
                            <textarea name="notes" class="form-control"></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Start Time</label>
                            <input type="time" name="startTime" class="form-control" required min="06:00" max="22:00" />
                            <small class="text-muted">Time must be between 06:00 and 22:00</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">End Time</label>
                            <input type="time" name="endTime" class="form-control" required min="06:00" max="22:00" />
                            <small class="text-muted">Time must be between 06:00 and 22:00</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary" type="submit">Create Workout</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Workout Modal -->
        <div class="modal fade" id="editWorkoutModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="EditWorkoutServlet" method="post" class="modal-content" id="editWorkoutForm">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Workout</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="workoutId" id="edit-workoutId" />
                        
                        <!-- Alert for validation errors -->
                        <div id="editWorkoutValidationAlert" class="alert alert-danger" style="display: none;">
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <span id="editWorkoutValidationMessage"></span>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Title</label>
                            <input type="text" name="title" id="edit-workoutTitle" class="form-control" required />
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Notes</label>
                            <textarea name="notes" id="edit-workoutNotes" class="form-control"></textarea>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Start Time</label>
                            <input type="time" name="startTime" id="edit-workoutStartTime" class="form-control" required min="06:00" max="22:00" />
                            <small class="text-muted">Time must be between 06:00 and 22:00</small>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">End Time</label>
                            <input type="time" name="endTime" id="edit-workoutEndTime" class="form-control" required min="06:00" max="22:00" />
                            <small class="text-muted">Time must be between 06:00 and 22:00</small>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" type="submit">Update Workout</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
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
                            <input type="number" name="sets" class="form-control" required min="1" />
                        </div>
                        <div class="mb-2">
                            <label>Reps</label>
                            <input type="number" name="reps" class="form-control" required min="1" />
                        </div>
                        <div class="mb-2">
                            <label>Rest Time (sec)</label>
                            <input type="number" name="restTime" class="form-control" min="0" />
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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Edit Exercise Modal -->
        <div class="modal fade" id="editExerciseModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="EditExerciseServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Exercise</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="exerciseId" id="edit-exerciseId" />
                        <input type="hidden" name="workoutId" id="edit-exerciseWorkoutId" />
                        <div class="mb-2">
                            <label>Exercise</label>
                            <select name="exerciseLibraryId" id="edit-exerciseLibraryId" class="form-select" onchange="previewEditExercise(this)">
                                <c:forEach var="e" items="${exerciseList}">
                                    <option value="${e.exerciseID}" data-video="${e.videoURL}" data-description="${e.description}">
                                        ${e.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-2">
                            <label>Sets</label>
                            <input type="number" name="sets" id="edit-exerciseSets" class="form-control" required min="1" />
                        </div>
                        <div class="mb-2">
                            <label>Reps</label>
                            <input type="number" name="reps" id="edit-exerciseReps" class="form-control" required min="1" />
                        </div>
                        <div class="mb-2">
                            <label>Rest Time (sec)</label>
                            <input type="number" name="restTime" id="edit-exerciseRestTime" class="form-control" min="0" />
                        </div>
                        <div class="mb-2">
                            <label>Notes</label>
                            <textarea name="notes" id="edit-exerciseNotes" class="form-control"></textarea>
                        </div>
                        <div class="mb-2" id="editPreview">
                            <iframe id="editExercisePreview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
                            <p id="editExerciseDescription" class="text-muted mt-1"></p>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-success" type="submit">Update Exercise</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
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
        <!-- Video Modal -->
        <div class="modal fade" id="videoModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="videoTitle">Exercise Video</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body text-center">
                        <iframe id="videoFrame" width="100%" height="400" frameborder="0"
                                allowfullscreen style="border-radius: 10px;"></iframe>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Assign Program Modal -->
        <div class="modal fade" id="assignProgramModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <form action="<%= request.getContextPath() %>/AssignProgramServlet" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Assign Program to Customer</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="programId" id="assignProgramId" />
                        
                        <div class="mb-3">
                            <label class="form-label">Program Information</label>
                            <div class="alert alert-info">
                                <strong id="programName">Program Name</strong><br>
                                <span id="programDescription">Program Description</span>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Select Customer</label>
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
                                <div class="alert alert-warning">
                                    <i class="fa fa-exclamation-triangle"></i>
                                    <strong>No customers found!</strong><br>
                                    You don't have any active contracts with customers yet. 
                                    Customers will appear here once they purchase your packages.
                                </div>
                                <select name="customerId" class="form-select" disabled>
                                    <option value="">No customers available</option>
                                </select>
                            <% } %>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Start Date</label>
                            <input type="date" name="startDate" class="form-control" required 
                                   min="<%= java.time.LocalDate.now() %>" />
                            <div class="form-text">
                                <i class="fa fa-info-circle"></i> Start date must be today or in the future. 
                                If you select a date that would cause workouts to fall in the past, 
                                the system will automatically adjust to the next available date.
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <% if (customers != null && !customers.isEmpty()) { %>
                            <button class="btn btn-success" type="submit">Assign Program</button>
                        <% } else { %>
                            <button class="btn btn-success" type="submit" disabled>Assign Program</button>
                        <% } %>
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

            // Validation for Add Workout Form
            document.getElementById("addWorkoutForm").addEventListener("submit", function (e) {
                e.preventDefault();
                const startTime = document.querySelector("#addWorkoutForm input[name='startTime']").value;
                const endTime = document.querySelector("#addWorkoutForm input[name='endTime']").value;
                
                if (!validateWorkoutTime(startTime, endTime, "workoutValidationAlert", "workoutValidationMessage")) {
                    return;
                }
                
                this.submit();
            });

            // Validation for Edit Workout Form
            document.getElementById("editWorkoutForm").addEventListener("submit", function (e) {
                e.preventDefault();
                const startTime = document.querySelector("#editWorkoutForm input[name='startTime']").value;
                const endTime = document.querySelector("#editWorkoutForm input[name='endTime']").value;
                
                if (!validateWorkoutTime(startTime, endTime, "editWorkoutValidationAlert", "editWorkoutValidationMessage")) {
                    return;
                }
                
                this.submit();
            });

            // Time validation function
            function validateWorkoutTime(startTime, endTime, alertId, messageId) {
                const alert = document.getElementById(alertId);
                const message = document.getElementById(messageId);
                
                // Check if times are within 6:00-22:00 range
                const startHour = parseInt(startTime.split(':')[0]);
                const endHour = parseInt(endTime.split(':')[0]);
                
                if (startHour < 6 || startHour > 22 || endHour < 6 || endHour > 22) {
                    message.textContent = "Start time and end time must be between 06:00 and 22:00.";
                    alert.style.display = "block";
                    return false;
                }
                
                // Check if end time is after start time
                if (endTime <= startTime) {
                    message.textContent = "End time must be after start time.";
                    alert.style.display = "block";
                    return false;
                }
                
                // Hide alert if validation passes
                alert.style.display = "none";
                return true;
            }

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

            function previewEditExercise(el) {
                const opt = el.selectedOptions[0];
                const iframe = document.getElementById("editExercisePreview");
                const descBox = document.getElementById("editExerciseDescription");

                if (opt.dataset.video) {
                    iframe.src = opt.dataset.video;
                    iframe.style.display = "block";
                } else {
                    iframe.style.display = "none";
                    iframe.src = "";
                }
                descBox.textContent = opt.dataset.description || "No description.";
            }

            function openEditWorkoutModal(workoutId) {
                console.log('Opening edit workout modal for workoutId:', workoutId);
                
                // Fetch workout data first, then show modal
                fetch(contextPath + '/GetWorkoutServlet?workoutId=' + workoutId)
                    .then(response => {
                        console.log('Response status:', response.status);
                        console.log('Response headers:', response.headers);
                        if (!response.ok) {
                            throw new Error('Network response was not ok: ' + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log('Workout data received:', data);
                        if (data.error) {
                            alert('Error: ' + data.error);
                            return;
                        }
                        
                        // Populate form fields
                        document.getElementById('edit-workoutId').value = data.workoutID || '';
                        document.getElementById('edit-workoutTitle').value = data.title || '';
                        document.getElementById('edit-workoutNotes').value = data.notes || '';
                        document.getElementById('edit-workoutStartTime').value = data.startStr || '';
                        document.getElementById('edit-workoutEndTime').value = data.endStr || '';
                        
                        console.log('Form populated successfully');
                        
                        // Show modal only after data is loaded successfully
                        const modal = new bootstrap.Modal(document.getElementById('editWorkoutModal'));
                        modal.show();
                    })
                    .catch(error => {
                        console.error('Error fetching workout data:', error);
                        alert('Error loading workout data: ' + error.message);
                    });
            }

            function openEditExerciseModal(exerciseId, workoutId) {
                // Fetch exercise data and populate modal
                fetch(contextPath + '/GetExerciseServlet?exerciseId=' + exerciseId)
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById('edit-exerciseId').value = data.exerciseId;
                        document.getElementById('edit-exerciseWorkoutId').value = workoutId;
                        document.getElementById('edit-exerciseLibraryId').value = data.exerciseId; // This is the exercise library ID
                        document.getElementById('edit-exerciseSets').value = data.sets;
                        document.getElementById('edit-exerciseReps').value = data.reps;
                        document.getElementById('edit-exerciseRestTime').value = data.restTimeSeconds || '';
                        document.getElementById('edit-exerciseNotes').value = data.notes || '';
                        
                        // Trigger preview
                        previewEditExercise(document.getElementById('edit-exerciseLibraryId'));
                        
                        new bootstrap.Modal(document.getElementById('editExerciseModal')).show();
                    })
                    .catch(error => {
                        console.error('Error fetching exercise data:', error);
                        alert('Error loading exercise data');
                    });
            }

            function deleteWorkout(workoutId) {
                if (confirm('Are you sure you want to delete this workout? This action cannot be undone.')) {
                    fetch(contextPath + '/DeleteWorkoutServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'workoutId=' + workoutId
                    })
                    .then(response => {
                        if (response.ok) {
                            location.reload();
                        } else {
                            alert('Error deleting workout');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Error deleting workout');
                    });
                }
            }

            function deleteExercise(exerciseId) {
                if (confirm('Are you sure you want to delete this exercise? This action cannot be undone.')) {
                    fetch(contextPath + '/DeleteExerciseServlet', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'exerciseId=' + exerciseId
                    })
                    .then(response => {
                        if (response.ok) {
                            location.reload();
                        } else {
                            alert('Error deleting exercise');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Error deleting exercise');
                    });
                }
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
                if (weekCount === 0)
                    return;

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

            function handleDayClick(cell) {
                const dayId = cell.dataset.dayId;
                const hasWorkout = cell.dataset.hasWorkout === "true";

                if (hasWorkout) {
                    openEditWorkoutModalByDay(dayId); // Bạn cần định nghĩa hàm này
                } else {
                    document.getElementById("workout-dayId").value = dayId;
                    const modal = new bootstrap.Modal(document.getElementById("addWorkoutModal"));
                    modal.show();
                }
            }

            // Tự động chặn sự kiện nổi từ dropdown, nút bên trong workout
            document.addEventListener('DOMContentLoaded', () => {
                document.querySelectorAll('.workout-card button, .workout-card form').forEach(el => {
                    el.addEventListener('click', e => e.stopPropagation());
                });
            });
            
            function openVideoModal(videoUrl, title) {
        const frame = document.getElementById("videoFrame");
        const modalTitle = document.getElementById("videoTitle");

        frame.src = videoUrl;
        modalTitle.textContent = title;

        const modal = new bootstrap.Modal(document.getElementById("videoModal"));
        modal.show();
    }

    // Dọn video khi đóng modal
    document.getElementById("videoModal").addEventListener("hidden.bs.modal", function () {
        document.getElementById("videoFrame").src = "";
    });
    
    function openAssignModal(programId) {
        document.getElementById('assignProgramId').value = programId;
        
        // Lấy thông tin chương trình để hiển thị
        var programName = '<%= program.getName() %>';
        var programDescription = '<%= program.getDescription() %>';
        
        // Cập nhật tiêu đề modal và thông tin chương trình
        document.querySelector('#assignProgramModal .modal-title').textContent = 
            'Assign Program: ' + programName;
        document.getElementById('programName').textContent = programName;
        document.getElementById('programDescription').textContent = programDescription;
        
        var modal = new bootstrap.Modal(document.getElementById('assignProgramModal'));
        modal.show();
    }
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
