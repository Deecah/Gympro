<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Schedule</title>
     <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
     <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
     <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/package-trainer.css">
    <style>
        /* Override or add schedule-specific styles if needed */
        .schedule-table {
            width: 100%;
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(44,62,80,0.07);
            overflow: hidden;
        }
        .schedule-table th, .schedule-table td {
            border: none;
            padding: 16px 12px;
            vertical-align: middle;
        }
        .schedule-table th {
            background: #f8f9fa;
            color: #2c3e50;
            font-weight: 600;
            font-size: 1rem;
        }
        .schedule-table tr {
            border-bottom: 1px solid #f0f0f0;
        }
        .schedule-table tr:last-child {
            border-bottom: none;
        }
        .week-form {
            margin-bottom: 1.5rem;
            background: #f8f9fa;
            padding: 1rem 1.5rem;
            border-radius: 8px;
            box-shadow: 0 1px 4px rgba(44,62,80,0.04);
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .week-form label {
            margin-bottom: 0;
            font-weight: 500;
            color: #34495e;
        }
        .week-form input[type="date"] {
            border-radius: 6px;
            border: 1px solid #ced4da;
            padding: 6px 10px;
        }
        .week-form button {
            background: #3498db;
            color: #fff;
            border: none;
            border-radius: 6px;
            padding: 8px 18px;
            font-weight: 500;
            transition: background 0.2s;
        }
        .week-form button:hover {
            background: #2980b9;
        }
        .action-link {
            color: #3498db;
            font-weight: 500;
            text-decoration: none;
            transition: color 0.2s;
        }
        .action-link:hover {
            color: #217dbb;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="d-flex">
        <div class="sidebar bg-dark text-white">
            <jsp:include page="sidebar.jsp" />
        </div>
        <div class="flex-grow-1 p-4 bg-light">
            <h2>Customer Programs for Trainer</h2>
            <table class="schedule-table">
                <thead>
                    <tr>
                        <th>Customer Name</th>
                        <th>Program Name</th>
                        <th>Assigned At</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="customerProgram" items="${customerPrograms}">
                        <tr>
                            <td>${customerProgram.customerName}</td>
                            <td>${customerProgram.programName}</td>
                            <td>${customerProgram.assignedAt}</td>
                            <td>
                                <a class="action-link" href="${pageContext.request.contextPath}/TimetableServlet?customerProgramId=${customerProgram.id}&programId=${customerProgram.programId}&startDate=${customerProgram.startDate}&endDate=${customerProgram.endDate}&trainerId=${trainerId}&scheduleId=${customerProgram.scheduleId}">View Schedule</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <script>
        document.getElementById('weekForm').addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            window.location.href = `/ScheduleServlet?trainerId=${formData.get('trainerId')}&weekStartDate=${formData.get('weekStartDate')}`;
        });
    </script>
</body>
</html>