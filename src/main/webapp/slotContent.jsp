<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Slot, model.Exercise, dao.ScheduleDAO" %>
<%@ page import="java.util.List, java.time.format.DateTimeFormatter" %>

<%
    out.println("<script>console.log('QueryString = " + request.getQueryString() + "');</script>");
    String slotIdParam = request.getParameter("slotId");
    out.println("<script>console.log('slotIdParam = " + slotIdParam + "');</script>");
    if (slotIdParam == null || slotIdParam.trim().isEmpty()) {
        out.println("Lỗi: Không có slotId hợp lệ.");
        return;
    }

    int slotId = 1;
    try {
        slotId = Integer.parseInt(slotIdParam);
    } catch (NumberFormatException e) {
        out.println("Lỗi: slotId không hợp lệ.");
        return;
    }

    Slot slot = ScheduleDAO.getSlotById(slotId);
    if (slot == null) {
        out.println("Lỗi: Không tìm thấy slot.");
        return;
    }

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
%>
<style>
    .toggle-button {
        padding: 8px 16px;
        background: #4a90e2;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 14px;
    }

    .toggle-button:hover {
        background: #357ab7;
    }
    
</style>
<div class="slot-container">
    <h2>Chi tiết Slot </h2>
    <div class="slot-info">
        <p><strong>Ngày:</strong> <%= slot.getDay().format(dateFormatter) %></p>
        <p><strong>Thời gian:</strong> <%= slot.getStartTime().format(timeFormatter) %> - <%= slot.getEndTime().format(timeFormatter) %></p>
        <p><strong>Thời lượng:</strong> <%= slot.getDuration() %> phút</p>
    </div>
    <div class="exercise-list">
        <h3>
            <button id="toggleExerciseBtn" class="toggle-button">📋 Xem danh sách bài tập</button>
        </h3>
        <div id="exerciseDetails" style="display:none;">
            <%
                List<Exercise> exercises = slot.getExercises();
                if (exercises == null || exercises.isEmpty()) {
            %>
            <p>Chưa có bài tập nào được thêm.</p>
            <%
                } else {
                    for (Exercise ex : exercises) {
            %>
            <div class="exercise-item">
                <strong><%= ex.getExerciseName() %></strong><br>
                <span>Số sets: <%= ex.getSets() %> | Reps: <%= ex.getReps() %> | Nghỉ: <%= ex.getRestTimeSeconds() %>s</span><br>
                <i>Ghi chú: <%= ex.getNotes() != null ? ex.getNotes() : "Không có" %></i>
            </div>
            <%
                    }
                }
            %>
        </div>
        
    </div>
    <div class="progress-button-container" style="margin-top: 20px;">
        <form action="progress" method="get">
            <button type="submit" class="toggle-button">📈 Xem tiến độ tập luyện</button>
        </form>
    </div>


    <a href="Schedule.jsp" class="back-button">← Trở về Lịch</a>
</div>

