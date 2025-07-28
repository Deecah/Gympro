<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.WorkoutSlotDTO" %>
<%@ page import="model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Weekly Timetable</title>
        <link rel="stylesheet" href="stylecss/schedule.css">
    </head>
    <body>
        <div class="layout">
            <div class="sidebar">
                <div class="sidebar-top">
                    <a href="index.jsp" class="sidebar-home">Home</a>
                </div>

                <div class="sidebar-menu">
                    <button>My Schedule</button>
                    <button>Requests</button>
                </div>
            </div>

                    <div class="timetable-wrapper">
            <% if (request.getAttribute("message") != null) { %>
                <% if ("completed".equals(request.getAttribute("message"))) { %>
                    <div class="alert alert-success" style="background-color: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                        Workout đã được đánh dấu hoàn thành thành công!
                        <button onclick="this.parentElement.style.display='none'" style="float: right; background: none; border: none; font-size: 20px; cursor: pointer;">&times;</button>
                    </div>
                <% } else if ("error".equals(request.getAttribute("message"))) { %>
                    <div class="alert alert-danger" style="background-color: #f8d7da; color: #721c24; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                        Có lỗi xảy ra khi đánh dấu workout hoàn thành!
                        <button onclick="this.parentElement.style.display='none'" style="float: right; background: none; border: none; font-size: 20px; cursor: pointer;">&times;</button>
                    </div>
                <% } %>
            <% } %>
            
            <form action="timetable" method="get" style="margin-bottom: 15px;">
                    <label for="weekRangeSelect"><strong>Chọn tuần:</strong></label>
                    <select id="weekRangeSelect" name="weekRange" onchange="this.form.submit()">
                        <c:forEach var="opt" items="${weekOptions}">
                            <option value="${opt}" ${opt == currentWeekRange ? "selected" : ""}>${opt}</option>
                        </c:forEach>
                    </select>
                </form>

                <table class="timetable">
                    <thead>
                        <tr>
                            <th>THỨ 2</th>
                            <th>THỨ 3</th>
                            <th>THỨ 4</th>
                            <th>THỨ 5</th>
                            <th>THỨ 6</th>
                            <th>THỨ 7</th>
                            <th>CHỦ NHẬT</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Map<Integer, List<WorkoutSlotDTO>> slotMap = (Map<Integer, List<WorkoutSlotDTO>>) request.getAttribute("slotMap");
                            for (int block = 1; block <= 7; block++) {
                        %>
                        <tr>
                            <%
                                for (int day = 0; day < 7; day++) {
                                    int slotId = day * 10 + block;
                                    List<WorkoutSlotDTO> slots = slotMap.get(slotId);
                            %>
                            <td data-slot-id="<%=slotId%>">
                                <%
                                    if (slots != null) {
                                        for (WorkoutSlotDTO slot : slots) {
                                %>
                                <div class="workout-box" onclick="showWorkoutDetail(<%= slot.getWorkoutId() %>)" style="cursor: pointer;">
                                    <strong><%= slot.getTitle() %></strong>
                                    <span><%= slot.getStartStr() %> - <%= slot.getEndStr() %></span><br/>
                                    <span class="program-name">👤 <%= slot.getDisplayName() %></span><br/>
                                    <span class="program-name">📘 <%= slot.getProgramName() %></span>
                                    <br/>
                                    <% if (slot.isCompleted()) { %>
                                        <span class="badge bg-success">Đã hoàn thành &#10003;</span>
                                    <% } else if (slot.getWorkoutId() > 0) { %>
                                        <button type="button" class="btn btn-sm btn-outline-primary" onclick="event.stopPropagation(); markCompleted(<%= slot.getWorkoutId() %>)">Hoàn thành</button>
                                    <% } %>
                                </div>
                                <%
                                        }
                                    }
                                %>
                            </td>
                            <%
                                }
                            %>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>

                </table>
            </div>

            <button class="request-button" title="New Request">+</button>
        </div>

        <!-- Workout Detail Modal -->
        <div id="workoutModal" class="modal" style="display: none;">
            <div class="modal-content">
                <span class="close" onclick="closeModal()">&times;</span>
                <div id="modalContent">
                    <!-- Content will be loaded here -->
                </div>
                <div id="modalCompleteButton" style="text-align: center; margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee;">
                    <!-- Complete button will be loaded here -->
                </div>
            </div>
        </div>

        <!-- Notes Modal -->
        <div id="notesModal" class="modal" style="display: none;">
            <div class="modal-content" style="max-width: 500px;">
                <span class="close" onclick="closeNotesModal()">&times;</span>
                <h3>Add Notes (Optional)</h3>
                <div style="margin: 20px 0;">
                    <label for="workoutNotes" style="display: block; margin-bottom: 10px; font-weight: bold;">Notes:</label>
                    <textarea id="workoutNotes" rows="4" cols="50" placeholder="Enter your notes about this workout..." style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; resize: vertical;"></textarea>
                </div>
                <div style="text-align: center;">
                    <button type="button" class="btn btn-success" onclick="submitWithNotes()">Mark as Completed</button>
                    <button type="button" class="btn btn-secondary" onclick="closeNotesModal()" style="margin-left: 10px;">Cancel</button>
                </div>
            </div>
        </div>


        <script>
            // Set current user ID for notification.js
            <% if (user != null) { %>
            var currentUserId = <%= user.getUserId() %>;
            <% } else { %>
            var currentUserId = null;
            <% } %>
        </script>
        <script src="${pageContext.request.contextPath}/js/notification.js"></script>
        
        <script>
            function showWorkoutDetail(workoutId) {
                if (workoutId <= 0) return;
                
                fetch('workoutDetail?workoutId=' + workoutId)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
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
                const modal = document.getElementById('workoutModal');
                const content = document.getElementById('modalContent');
                const completeButton = document.getElementById('modalCompleteButton');
                
                const workout = data.workout;
                const exercises = data.exercises;
                
                // Add complete button
                completeButton.innerHTML = '<button type="button" class="btn btn-success btn-large" onclick="markCompleted(' + workout.workoutId + '); closeModal();">Mark as Completed</button>';
                
                let html = '<div class="workout-info">';
                html += '<h2>' + workout.title + '</h2>';
                html += '<p><strong>Time:</strong> ' + workout.startTime + ' - ' + workout.endTime + '</p>';
                html += '<p><strong>Program:</strong> ' + (workout.programName || 'N/A') + '</p>';
                if (workout.notes) {
                    html += '<p><strong>Notes:</strong> ' + workout.notes + '</p>';
                }
                html += '</div>';
                
                if (exercises && exercises.length > 0) {
                    html += '<h3>Exercise List:</h3>';
                    exercises.forEach(function(exercise) {
                        html += '<div class="exercise-item">';
                        html += '<div class="exercise-name">' + exercise.exerciseName + '</div>';
                        html += '<div class="exercise-details">';
                        html += '<div class="exercise-detail"><strong>Sets:</strong> ' + exercise.sets + '</div>';
                        html += '<div class="exercise-detail"><strong>Reps:</strong> ' + exercise.reps + '</div>';
                        html += '<div class="exercise-detail"><strong>Rest:</strong> ' + exercise.restTimeSeconds + 's</div>';
                        html += '</div>';
                        
                        if (exercise.notes) {
                            html += '<p><strong>Notes:</strong> ' + exercise.notes + '</p>';
                        }
                        
                        if (exercise.videoURL) {
                            html += '<div class="video-container">';
                            html += '<strong>Tutorial Video:</strong>';
                            html += '<iframe src="' + exercise.videoURL + '" allowfullscreen></iframe>';
                            html += '</div>';
                        }
                        
                        if (exercise.description) {
                            html += '<p><strong>Description:</strong> ' + exercise.description + '</p>';
                        }
                        
                        html += '</div>';
                    });
                } else {
                    html += '<p>No exercises in this workout.</p>';
                }
                
                content.innerHTML = html;
                modal.style.display = 'block';
            }

            function closeModal() {
                document.getElementById('workoutModal').style.display = 'none';
            }

            let currentWorkoutId = null;

            function markCompleted(workoutId) {
                currentWorkoutId = workoutId;
                document.getElementById('notesModal').style.display = 'block';
                document.getElementById('workoutNotes').value = '';
            }

            function closeNotesModal() {
                document.getElementById('notesModal').style.display = 'none';
                currentWorkoutId = null;
            }

            function submitWithNotes() {
                if (currentWorkoutId) {
                    const notes = document.getElementById('workoutNotes').value;
                    const form = document.createElement('form');
                    form.method = 'POST';
                    form.action = 'markCompleted';
                    
                    const workoutInput = document.createElement('input');
                    workoutInput.type = 'hidden';
                    workoutInput.name = 'workoutId';
                    workoutInput.value = currentWorkoutId;
                    
                    const notesInput = document.createElement('input');
                    notesInput.type = 'hidden';
                    notesInput.name = 'notes';
                    notesInput.value = notes;
                    
                    form.appendChild(workoutInput);
                    form.appendChild(notesInput);
                    document.body.appendChild(form);
                    form.submit();
                }
            }

            // Đóng modal khi click bên ngoài
            window.onclick = function(event) {
                const modal = document.getElementById('workoutModal');
                const notesModal = document.getElementById('notesModal');
                if (event.target === modal) {
                    modal.style.display = 'none';
                }
                if (event.target === notesModal) {
                    notesModal.style.display = 'none';
                }
            }
        </script>
    </body>
</html>
