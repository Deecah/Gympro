<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ page import="model.ExerciseLibrary" %>
        <% ExerciseLibrary exercise=(ExerciseLibrary) request.getAttribute("exercise"); %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Edit Exercise</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
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
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
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
                        <jsp:include page="sidebar.jsp" />
                    </div>

                    <!-- Main Content -->
                    <div class="flex-grow-1 p-4">
                        <div class="form-container">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list"
                                    class="text-decoration-none">
                                    <i class="fas fa-arrow-left me-2"></i>Back to Exercise Library
                                </a>
                            </div>

                            <h3 class="mb-4"><i class="fas fa-dumbbell me-2"></i>Edit Exercise</h3>

                            <form action="<%= request.getContextPath() %>/EditExerciseServlet" method="post"
                                enctype="multipart/form-data">
                                <!-- Hidden ID -->
                                <input type="hidden" name="exerciseID" value="<%= exercise.getExerciseID() %>" />

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-file-alt me-2"></i>Exercise Name</label>
                                    <input type="text" name="name" class="form-control"
                                        value="<%= exercise.getName() %>" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-info-circle me-2"></i>Description</label>
                                    <textarea name="description" class="form-control"
                                        rows="3"><%= exercise.getDescription() != null ? exercise.getDescription() : "" %></textarea>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-dumbbell me-2"></i>Muscle Group</label>
                                    <input type="text" name="muscleGroup" class="form-control"
                                        value="<%= exercise.getMuscleGroup() != null ? exercise.getMuscleGroup() : "" %>"
                                        required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-tools me-2"></i>Equipment</label>
                                    <input type="text" name="equipment" class="form-control"
                                        value="<%= exercise.getEquipment() != null ? exercise.getEquipment() : "" %>"
                                        required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-repeat me-2"></i>Sets</label>
                                    <input type="number" name="sets" class="form-control"
                                        value="<%= exercise.getSets() %>" min="1" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-redo me-2"></i>Reps</label>
                                    <input type="number" name="reps" class="form-control"
                                        value="<%= exercise.getReps() %>" min="1" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-clock me-2"></i>Rest Time
                                        (seconds)</label>
                                    <input type="number" name="restTimeSeconds" class="form-control"
                                        value="<%= exercise.getRestTimeSeconds() %>" min="0" required />
                                </div>

                                <div class="mb-3">
                                    <label class="form-label"><i class="fas fa-video me-2"></i>Video</label>
                                    <% if (exercise.getVideoURL() !=null && !exercise.getVideoURL().isEmpty()) { %>
                                        <div class="mb-2">Current video:</div>
                                        <video class="video-preview" controls>
                                            <source src="<%= exercise.getVideoURL() %>" type="video/mp4">
                                            Your browser does not support the video tag.
                                        </video>
                                        <% } else { %>
                                            <div class="mb-2">No video currently uploaded.</div>
                                            <% } %>
                                                <input type="file" id="editVideoUpload" name="videoFile"
                                                    accept="video/*" class="form-control mt-2"
                                                    onchange="previewEditVideo(event)" />
                                                <video id="editVideoPreview" class="video-preview" controls
                                                    style="display: none;">
                                                    Your browser does not support the video tag.
                                                </video>
                                </div>

                                <button type="submit" class="btn btn-primary"><i class="fas fa-check me-2"></i>Update
                                    Exercise</button>
                                <a href="<%= request.getContextPath() %>/trainer/library.jsp?action=list"
                                    class="btn btn-secondary">
                                    <i class="fas fa-times me-2"></i>Cancel
                                </a>
                            </form>
                        </div>
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
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>