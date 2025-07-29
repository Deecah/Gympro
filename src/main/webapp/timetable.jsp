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

 <!-- Modal -->
        <div id="requestModal" class="modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h2>📅 Request Change Schedule</h2>
                    <span class="close" onclick="closeModal()">&times;</span>
                </div>
                
                <div class="modal-body">
                    <form id="requestForm">
                        <!-- Week Selector -->
                        <div class="week-selector">
                            <label for="weekSelect" style="margin: 0; font-weight: 600; color: #333;">📅 Select Week:</label>
                            <select id="weekSelect" name="weekSelect" class="form-control" style="width: auto; margin: 0;">
                                <option value="">-- Select week --</option>
                                <c:forEach var="opt" items="${weekOptions}">
                                    <option value="${opt}">${opt}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="slotSelect">⏰ Select Slot to Change:</label>
                            <div class="slot-selector">
                                <div class="form-group">
                                    <label for="slotSelect">Choose slot:</label>
                                    <select id="slotSelect" name="slotSelect" class="form-control" required onchange="updateSlotInfo()">
                                        <option value="">-- Select slot --</option>
                                    </select>
                                </div>
                            </div>
                            <div id="slotInfo" class="slot-info" style="display: none; margin-top: 10px; padding: 10px; background: #f8f9fa; border-radius: 5px; border-left: 4px solid #007bff;">
                                <strong>Selected Slot Info:</strong><br>
                                <span id="slotTitle"></span><br>
                                <span id="slotTime"></span><br>
                                <span id="slotTrainer"></span><br>
                                <span id="slotProgram"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="newDay">🔄 Desired Change Day:</label>
                            <div class="day-selector">
                                <div class="form-group">
                                    <label for="newDay">Select new day:</label>
                                    <select id="newDay" name="newDay" class="form-control" required>
                                        <option value="">-- Select day --</option>
                                        <option value="monday">Monday</option>
                                        <option value="tuesday">Tuesday</option>
                                        <option value="wednesday">Wednesday</option>
                                        <option value="thursday">Thursday</option>
                                        <option value="friday">Friday</option>
                                        <option value="saturday">Saturday</option>
                                        <option value="sunday">Sunday</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="newTime">New time:</label>
                                    <input type="time" id="newTime" name="newTime" class="form-control" required>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="reason">📝 Reason for Change:</label>
                            <textarea id="reason" name="reason" class="form-control reason-textarea" 
                                      placeholder="Please explain the reason for your schedule change request..." required></textarea>
                        </div>
                    </form>
                </div>
                
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">❌ Cancel</button>
                    <button type="button" class="btn btn-primary" onclick="submitRequest()">✅ Submit Request</button>
                </div>
            </div>
        </div>
        
  <script>
            // Global variable to store slot data
            let slotData = [];
            
            // Load slot data from the page
            function loadSlotData() {
                const slotSelect = document.getElementById('slotSelect');
                slotSelect.innerHTML = '<option value="">-- Select slot --</option>';
                
                // Get all workout boxes from the timetable
                const workoutBoxes = document.querySelectorAll('.workout-box');
                let slotIndex = 0;
                
                workoutBoxes.forEach((box, index) => {
                    const title = box.querySelector('strong').textContent;
                    const timeSpan = box.querySelector('span').textContent;
                    const trainerSpan = box.querySelectorAll('.program-name')[0].textContent;
                    const programSpan = box.querySelectorAll('.program-name')[1].textContent;
                    
                    // Get the parent td to find slot ID
                    const parentTd = box.closest('td');
                    const slotId = parentTd.getAttribute('data-slot-id');
                    
                    // Create slot data object
                    const slotInfo = {
                        id: slotId,
                        title: title,
                        time: timeSpan,
                        trainer: trainerSpan,
                        program: programSpan,
                        index: slotIndex++
                    };
                    
                    slotData.push(slotInfo);
                    
                    // Create option for dropdown
                    const option = document.createElement('option');
                    option.value = slotIndex - 1;
                    option.textContent = `${title} - ${timeSpan}`;
                    slotSelect.appendChild(option);
                });
            }
            
            // Update slot info display
            function updateSlotInfo() {
                const slotSelect = document.getElementById('slotSelect');
                const slotInfo = document.getElementById('slotInfo');
                const slotTitle = document.getElementById('slotTitle');
                const slotTime = document.getElementById('slotTime');
                const slotTrainer = document.getElementById('slotTrainer');
                const slotProgram = document.getElementById('slotProgram');
                
                if (slotSelect.value && slotSelect.value !== '') {
                    const selectedSlot = slotData[parseInt(slotSelect.value)];
                    if (selectedSlot) {
                        slotTitle.textContent = `📋 ${selectedSlot.title}`;
                        slotTime.textContent = `⏰ ${selectedSlot.time}`;
                        slotTrainer.textContent = selectedSlot.trainer;
                        slotProgram.textContent = selectedSlot.program;
                        slotInfo.style.display = 'block';
                    }
                } else {
                    slotInfo.style.display = 'none';
                }
            }
            
            // Modal functions
            function openModal() {
                document.getElementById('requestModal').style.display = 'block';
                
                // Load slot data when modal opens
                loadSlotData();
                
                // Set current week as default (if available)
                const currentWeekSelect = document.getElementById('weekRangeSelect');
                if (currentWeekSelect && currentWeekSelect.value) {
                    document.getElementById('weekSelect').value = currentWeekSelect.value;
                }
            }

            function closeModal() {
                document.getElementById('requestModal').style.display = 'none';
                // Reset form
                document.getElementById('requestForm').reset();
                // Reset slot data
                slotData = [];
                // Hide slot info
                document.getElementById('slotInfo').style.display = 'none';
            }

            function submitRequest() {
                const form = document.getElementById('requestForm');
                const formData = new FormData(form);
                
                // Validate form
                if (!form.checkValidity()) {
                    alert('Please fill in all required information!');
                    return;
                }
                
                // Get form values
                const weekSelect = formData.get('weekSelect');
                const slotSelect = formData.get('slotSelect');
                const newDay = formData.get('newDay');
                const newTime = formData.get('newTime');
                const reason = formData.get('reason');
                
                // Validate slot selection
                if (!slotSelect || slotSelect === '') {
                    alert('Please select a slot to change!');
                    return;
                }
                
                // Get selected slot data
                const selectedSlot = slotData[parseInt(slotSelect)];
                if (!selectedSlot) {
                    alert('Invalid slot selection!');
                    return;
                }
                
                // Validate that new day/time is different from current slot
                const currentSlotDay = getDayFromSlotId(selectedSlot.id);
                const currentSlotTime = selectedSlot.time;
                
                if (newDay === currentSlotDay && newTime === currentSlotTime.split(' - ')[0]) {
                    alert('New day or time must be different from current slot!');
                    return;
                }
                
                // Here you would typically send the data to your server
                console.log('Schedule change request:');
                console.log('Week:', weekSelect);
                console.log('Current slot:', selectedSlot);
                console.log('New day:', newDay);
                console.log('New time:', newTime);
                console.log('Reason:', reason);
                
                // Show success message
                alert('✅ Your request has been submitted successfully!');
                closeModal();
            }
            
            // Helper function to get day from slot ID
            function getDayFromSlotId(slotId) {
                const day = Math.floor(slotId / 10);
                const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'];
                return days[day];
            }

            // Close modal when clicking outside of it
            window.onclick = function(event) {
                const modal = document.getElementById('requestModal');
                if (event.target === modal) {
                    closeModal();
                }
            };

            // Close modal with Escape key
            document.addEventListener('keydown', function(event) {
                if (event.key === 'Escape') {
                    closeModal();
                }
            });
        </script>
        
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
            };
        </script>
    </body>
</html>