<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%@ page import="model.Program, model.ProgramWeek, model.ProgramDay" %>
<%
    Program program = (Program) request.getAttribute("program");
    List<ProgramWeek> weeks = (List<ProgramWeek>) request.getAttribute("weeks");
    Map<Integer, List<ProgramDay>> daysMap = (Map<Integer, List<ProgramDay>>) request.getAttribute("daysMap");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Program Detail</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
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
    <!-- Sidebar -->
    <div class="sidebar bg-dark text-white">
        <jsp:include page="sidebar.jsp"/>
    </div>

    <!-- Main Content -->
    <div class="flex-grow-1 p-4 bg-light">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <a href="<%= request.getContextPath() %>/ProgramServlet" class="text-decoration-none">&larr; Back to Programs</a>
            <button class="btn btn-primary">Assign to client</button>
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
                            <div class="text-muted">Click to add workout</div>
                        </div>
                    <% } %>
                </div>
            <% } %>
        </div>

        <div class="mt-3 d-flex gap-2">
            <button class="btn btn-outline-primary" onclick="addWeek()">+ Add week</button>
            <button class="btn btn-outline-danger" onclick="removeLastWeek()">🗑️ Remove last week</button>
        </div>
    </div>
</div>

<!-- Workout Modal (giữ nguyên như cũ nếu có) -->

<script>
    const contextPath = "<%= request.getContextPath() %>";
    const programId = <%= program.getProgramId() %>;
    let weekCount = <%= weeks.size() %>;
    const container = document.getElementById("workoutContainer");

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

    function renderWeek(weekNumber, data) {
        const weekRow = document.createElement("div");
        weekRow.className = "row week-row";
        weekRow.setAttribute("data-week-id", data.weekId);

        for (let i = 0; i < 7; i++) {
            const col = document.createElement("div");
            col.className = "col day-cell";
            col.id = `day-${data.dayIds[i]}`;

            col.innerHTML = `
                <div class="day-header">Week ${weekNumber} - Day ${i + 1}</div>
                <div class="text-muted">Click to add workout</div>
            `;
            weekRow.appendChild(col);
        }
        container.appendChild(weekRow);
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
