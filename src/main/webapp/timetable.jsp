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
        <style>
            td {
                min-height: 90px;
                vertical-align: top;
                padding: 6px;
            }
            .workout-box {
                background: #e9f9e9;
                border-left: 4px solid #28a745;
                padding: 4px 8px;
                margin-bottom: 6px;
                border-radius: 4px;
            }
            .workout-box strong {
                display: block;
                font-weight: bold;
            }
            .program-name {
                font-size: 12px;
                color: #555;
                font-style: italic;
            }
            
            /* Modal Styles */
            .modal {
                display: none;
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
            }
            
            .modal-content {
                background-color: #fefefe;
                margin: 5% auto;
                padding: 0;
                border: none;
                border-radius: 10px;
                width: 900px;
                height: 600px;
                box-shadow: 0 4px 20px rgba(0,0,0,0.3);
                position: relative;
            }
            
            .modal-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 20px 30px;
                border-radius: 10px 10px 0 0;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            
            .modal-header h2 {
                margin: 0;
                font-size: 24px;
                font-weight: 600;
            }
            
            .close {
                color: white;
                font-size: 28px;
                font-weight: bold;
                cursor: pointer;
                transition: opacity 0.3s;
            }
            
            .close:hover {
                opacity: 0.7;
            }
            
            .modal-body {
                padding: 30px;
                height: calc(100% - 140px);
                overflow-y: auto;
            }
            
            .form-group {
                margin-bottom: 25px;
            }
            
            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 600;
                color: #333;
                font-size: 16px;
            }
            
            .form-control {
                width: 100%;
                padding: 12px 15px;
                border: 2px solid #e1e5e9;
                border-radius: 8px;
                font-size: 16px;
                transition: border-color 0.3s, box-shadow 0.3s;
                box-sizing: border-box;
            }
            
            .form-control:focus {
                outline: none;
                border-color: #667eea;
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            }
            
            .time-inputs {
                display: flex;
                gap: 20px;
                align-items: center;
            }
            
            .time-inputs .form-group {
                flex: 1;
            }
            
            .time-inputs .form-control {
                text-align: center;
                font-size: 18px;
                font-weight: 500;
            }
            
            .week-selector {
                display: flex;
                gap: 15px;
                align-items: center;
                margin-bottom: 20px;
                padding: 15px;
                background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
                border-radius: 8px;
                border: 2px solid #dee2e6;
            }
            
            .day-selector {
                display: flex;
                gap: 15px;
                align-items: center;
            }
            
            .day-selector .form-group {
                flex: 1;
            }
            
            .day-selector .form-control {
                text-align: center;
                font-size: 16px;
                font-weight: 500;
            }
            
            .slot-selector {
                display: flex;
                gap: 15px;
                align-items: center;
            }
            
            .slot-selector .form-group {
                flex: 1;
            }
            
            .slot-selector .form-control {
                text-align: left;
                font-size: 16px;
                font-weight: 500;
            }
            
            .slot-info {
                font-size: 14px;
                line-height: 1.4;
            }
            
            .slot-info span {
                display: block;
                margin-bottom: 2px;
            }
            
            .reason-textarea {
                min-height: 120px;
                resize: vertical;
            }
            
            .modal-footer {
                padding: 20px 30px;
                background-color: #f8f9fa;
                border-radius: 0 0 10px 10px;
                display: flex;
                justify-content: flex-end;
                gap: 15px;
            }
            
            .btn {
                padding: 12px 24px;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                font-weight: 500;
                cursor: pointer;
                transition: all 0.3s;
            }
            
            .btn-primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
            }
            
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
            }
            
            .btn-secondary {
                background-color: #6c757d;
                color: white;
            }
            
            .btn-secondary:hover {
                background-color: #5a6268;
                transform: translateY(-2px);
            }
            
            /* Request Button */
            .request-button {
                position: fixed;
                bottom: 30px;
                right: 30px;
                width: 60px;
                height: 60px;
                border-radius: 50%;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border: none;
                font-size: 24px;
                font-weight: bold;
                cursor: pointer;
                box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
                transition: all 0.3s;
                z-index: 100;
            }
            
            .request-button:hover {
                transform: scale(1.1);
                box-shadow: 0 6px 25px rgba(102, 126, 234, 0.6);
            }
        </style>
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
                                <div class="workout-box">
                                    <strong><%= slot.getTitle() %></strong>
                                    <span><%= slot.getStartStr() %> - <%= slot.getEndStr() %></span><br/>
                                    <span class="program-name">👤 <%= slot.getDisplayName() %></span><br/>
                                    <span class="program-name">📘 <%= slot.getProgramName() %></span>
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

            <button class="request-button" title="New Request" onclick="openModal()">+</button>
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
            }

            // Close modal with Escape key
            document.addEventListener('keydown', function(event) {
                if (event.key === 'Escape') {
                    closeModal();
                }
            });
        </script>
    </body>
</html>
