<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.ExerciseLibrary" %>
<%
    ExerciseLibrary exercise = (ExerciseLibrary) request.getAttribute("exercise");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Exercise</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            margin: 0;
        }
        .sidebar {
            width: 240px;
            min-height: 100vh;
        }
    </style>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar bg-dark text-white">
            <jsp:include page="sidebar.jsp"/>
        </div>

        <!-- Main Content -->
        <div class="flex-grow-1 p-4 bg-light">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list" class="text-decoration-none">&larr; Back to Exercise Library</a>
            </div>

            <h3>Edit Exercise</h3>

            <form action="<%= request.getContextPath() %>/EditExerciseServlet" method="post" enctype="multipart/form-data">
                <!-- Hidden ID -->
                <input type="hidden" name="exerciseID" value="<%= exercise.getExerciseID() %>" />

                <div class="mb-3">
                    <label>Exercise Name</label>
                    <input type="text" name="name" class="form-control" value="<%= exercise.getName() %>" required/>
                </div>

                <div class="mb-3">
                    <label>Description</label>
                    <textarea name="description" class="form-control" rows="3"><%= exercise.getDescription() %></textarea>
                </div>

                    <div class="mb-3">
                        <label class="form-label">Upload New Video</label><br/>

                        <% if (exercise.getVideoURL() != null && !exercise.getVideoURL().isEmpty()) { %>
                        <div class="mb-2">Current video:</div>
                        <video width="480" height="300" controls>
                            <source src="<%= exercise.getVideoURL() %>" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                        <% } %>

                        <input type="file" id="editVideoUpload" name="videoFile" accept="video/*" class="form-control mt-2" onchange="previewEditVideo(event)"/>

                        <!-- New Preview Area -->
                        <video id="editVideoPreview" width="480" height="300" controls style="margin-top: 15px; display: none;">
                            Your browser does not support the video tag.
                        </video>
                    </div>

                <div class="mb-3">
                    <label>Muscle Group</label>
                    <input type="text" name="muscleGroup" class="form-control" value="<%= exercise.getMuscleGroup() %>" required/>
                </div>

                <div class="mb-3">
                    <label>Equipment</label>
                    <input type="text" name="equipment" class="form-control" value="<%= exercise.getEquipment() %>" required/>
                </div>

                <button type="submit" class="btn btn-primary">Update Exercise</button>
                <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </div>
            <script>
                function previewEditVideo(event) {
                    const file = event.target.files[0];
                    const video = document.getElementById('editVideoPreview');

                    if (file) {
                        const url = URL.createObjectURL(file);
                        video.src = url;
                        video.style.display = 'block';
                    } else {
                        video.src = '';
                        video.style.display = 'none';
                    }
                }
            </script>
</body>
</html>
