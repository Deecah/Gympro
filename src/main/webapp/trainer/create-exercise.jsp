<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="model.ExerciseLibrary" %>
<%
    List<ExerciseLibrary> exerciseLibraries = (List<ExerciseLibrary>) request.getAttribute("exerciseLibraries");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Exercise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
            background-color: #f8f9fa;
        }
        .sidebar {
            width: 240px;
            min-height: 100vh;
            background-color: #343a40;
        }
        .form-container {
            max-width: 600px;
            margin: auto;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .video-preview {
            max-width: 100%;
            margin-top: 15px;
            border-radius: 5px;
        }
        .form-label {
            font-weight: 500;
        }
    </style>
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
            <div class="d-flex justify-content-between align-items-center mb-4">
                <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list" class="text-decoration-none">
                    <i class="fas fa-arrow-left me-2"></i>Back to Exercise Library
                </a>
            </div>

            <div class="form-container">
                <h3 class="mb-4"><i class="fas fa-dumbbell me-2"></i>Create New Exercise</h3>

                <form action="<%= request.getContextPath() %>/CreateExerciseServlet" method="post" enctype="multipart/form-data" onsubmit="return validateForm()">
                    <input type="hidden" name="workoutId" value="<%= request.getParameter("workoutId") != null ? request.getParameter("workoutId") : "" %>"/>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-list me-2"></i>Select from Exercise Library (Optional)</label>
                        <select name="exerciseLibraryId" class="form-select" onchange="populateFields(this)">
                            <option value="">Create new exercise</option>
                            <c:forEach var="exercise" items="${exerciseLibraries}">
                                <option value="${exercise.exerciseLibraryID}"
                                        data-name="${exercise.exerciseName}"
                                        data-description="${exercise.description}"
                                        data-muscle="${exercise.muscleGroup}"
                                        data-equipment="${exercise.equipment}"
                                        data-sets="${exercise.sets}"
                                        data-reps="${exercise.reps}"
                                        data-rest="${exercise.restTimeSeconds}"
                                        data-video="${exercise.videoUrl}">
                                    ${exercise.exerciseName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-file-alt me-2"></i>Exercise Name</label>
                        <input type="text" name="exerciseName" class="form-control" placeholder="Enter exercise name..." required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-info-circle me-2"></i>Description</label>
                        <textarea name="description" class="form-control" placeholder="Enter exercise description..." rows="3"></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-dumbbell me-2"></i>Muscle Group</label>
                        <input type="text" name="muscleGroup" class="form-control" placeholder="e.g., Chest, Back" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-tools me-2"></i>Equipment</label>
                        <input type="text" name="equipment" class="form-control" placeholder="e.g., Dumbbells, Barbell" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-redo me-2"></i>Sets</label>
                        <input type="number" name="sets" class="form-control" placeholder="Number of sets" min="1" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-repeat me-2"></i>Reps</label>
                        <input type="number" name="reps" class="form-control" placeholder="Number of reps" min="1" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-clock me-2"></i>Rest Time (Seconds)</label>
                        <input type="number" name="restTimeSeconds" class="form-control" placeholder="Rest time in seconds" min="0" required/>
                    </div>

                    <div class="mb-3">
                        <label class="form-label"><i class="fas fa-video me-2"></i>Upload Exercise Video</label>
                        <input type="file" id="videoUpload" name="videoFile" accept="video/*" class="form-control" onchange="previewVideo(event)"/>
                        <video id="videoPreview" class="video-preview" controls style="display: none;">
                            Your browser does not support the video tag.
                        </video>
                    </div>

                    <button type="submit" class="btn btn-success"><i class="fas fa-check me-2"></i>Save Exercise</button>
                    <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list" class="btn btn-secondary">
                        <i class="fas fa-times me-2"></i>Cancel
                    </a>
                </form>
            </div>
        </div>
    </div>

    <script>
        function previewVideo(event) {
            const file = event.target.files[0];
            const video = document.getElementById('videoPreview');
            if (file) {
                const url = URL.createObjectURL(file);
                video.src = url;
                video.style.display = 'block';
            } else {
                video.src = '';
                video.style.display = 'none';
            }
        }

        function populateFields(select) {
            const nameInput = document.querySelector('input[name="exerciseName"]');
            const descInput = document.querySelector('textarea[name="description"]');
            const muscleInput = document.querySelector('input[name="muscleGroup"]');
            const equipInput = document.querySelector('input[name="equipment"]');
            const setsInput = document.querySelector('input[name="sets"]');
            const repsInput = document.querySelector('input[name="reps"]');
            const restInput = document.querySelector('input[name="restTimeSeconds"]');
            const videoInput = document.getElementById('videoUpload');
            const videoPreview = document.getElementById('videoPreview');

            if (select.value) {
                nameInput.value = select.options[select.selectedIndex].dataset.name || '';
                descInput.value = select.options[select.selectedIndex].dataset.description || '';
                muscleInput.value = select.options[select.selectedIndex].dataset.muscle || '';
                equipInput.value = select.options[select.selectedIndex].dataset.equipment || '';
                setsInput.value = select.options[select.selectedIndex].dataset.sets || '';
                repsInput.value = select.options[select.selectedIndex].dataset.reps || '';
                restInput.value = select.options[select.selectedIndex].dataset.rest || '';
                if (select.options[select.selectedIndex].dataset.video) {
                    videoPreview.src = select.options[select.selectedIndex].dataset.video;
                    videoPreview.style.display = 'block';
                    videoInput.value = '';
                }
            } else {
                nameInput.value = '';
                descInput.value = '';
                muscleInput.value = '';
                equipInput.value = '';
                setsInput.value = '';
                repsInput.value = '';
                restInput.value = '';
                videoPreview.src = '';
                videoPreview.style.display = 'none';
                videoInput.value = '';
            }
        }

        function validateForm() {
            const sets = document.querySelector('input[name="sets"]').value;
            const reps = document.querySelector('input[name="reps"]').value;
            const rest = document.querySelector('input[name="restTimeSeconds"]').value;
            if (sets < 1 || reps < 1 || rest < 0) {
                alert('Sets and Reps must be at least 1, and Rest Time must be non-negative.');
                return false;
            }
            return true;
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>