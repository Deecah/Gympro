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
                body {
                    background: linear-gradient(135deg, #f9f9f9 60%, #e3e6f3 100%);
                    padding: 30px;
                    font-family: 'Segoe UI', 'Roboto', Arial, sans-serif;
                }
                .table-container {
                    background: #fff;
                    padding: 30px 35px 35px 35px;
                    border-radius: 18px;
                    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.10);
                    margin-top: 30px;
                }
                h2 {
                    font-weight: bold;
                    margin-bottom: 25px;
                    color: #2d3a4b;
                    letter-spacing: 1px;
                }
                .btn-feedback {
                    font-size: 0.95rem;
                    border-radius: 20px;
                    padding: 4px 18px;
                    transition: background 0.2s, color 0.2s;
                }
                .btn-feedback:hover {
                    background: #0d6efd;
                    color: #fff;
                }
                .btn-outline-secondary {
                    border-radius: 20px;
                    padding: 4px 18px;
                }
                .modal-content {
                    border-radius: 14px;
                    box-shadow: 0 4px 24px 0 rgba(31, 38, 135, 0.10);
                }
                .star {
                    font-size: 2.1rem;
                    color: #e0e0e0;
                    cursor: pointer;
                    transition: color 0.2s;
                }
                .star.hovered,
                .star.selected {
                    color: #ffc107;
                }
                .table th, .table td {
                    vertical-align: middle !important;
                    text-align: center;
                }
                .table-striped > tbody > tr:nth-of-type(odd) {
                    background-color: #f6f8fa;
                }
                .table-light th {
                    background: #e3e6f3;
                    color: #2d3a4b;
                    font-weight: 600;
                }
                .toast-container {
                    z-index: 1100;
                }
                /* Custom scrollbar */
                ::-webkit-scrollbar {
                    width: 8px;
                    background: #f1f1f1;
                }
                ::-webkit-scrollbar-thumb {
                    background: #cfd8dc;
                    border-radius: 8px;
                }
                /* Responsive */
                @media (max-width: 768px) {
                    .table-container {
                        padding: 10px 2px;
                    }
                    h2 {
                        font-size: 1.2rem;
                    }
                    .table th, .table td {
                        font-size: 0.95rem;
                    }
                }
                /* New: Avatar and status badge for trainer */
                .trainer-info {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    justify-content: center;
                }
                .trainer-avatar {
                    width: 38px;
                    height: 38px;
                    border-radius: 50%;
                    object-fit: cover;
                    border: 2px solid #e3e6f3;
                }
                .status-badge {
                    display: inline-block;
                    padding: 2px 12px;
                    border-radius: 12px;
                    font-size: 0.95em;
                    font-weight: 500;
                    color: #fff;
                }
                .status-active {
                    background: #37b24d;
                }
                .status-expired {
                    background: #f03e3e;
                }
                .status-pending {
                    background: #fab005;
                    color: #333;
                }
            </style>
</head>
<body>
    <div class="container table-container text-center">
        <div class="mb-3 text-start">
            <a href="index.jsp" class="btn btn-outline-primary">
                &larr; Back to Home
            </a>
        </div>
            <h2>Your programs</h2>
            <table class="table table-striped">
                <thead class="table-light">
                    <tr>
                        <th class="text-center">Program Name</th>
                        <th class="text-center">Assigned At</th>
                        <th class="text-center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="customerProgram" items="${customerPrograms}">
                        <tr>
                            <td>${customerProgram.programName}</td>
                            <td>${customerProgram.assignedAt}</td>
                            <td>
                                <a class="action-link" href="${pageContext.request.contextPath}/TimetableCustomerServlet?customerProgramId=${customerProgram.id}&programId=${customerProgram.programId}&startDate=${customerProgram.startDate}&endDate=${customerProgram.endDate}&trainerId=${trainerId}&scheduleId=${customerProgram.scheduleId}">View Schedule</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
    </div>
</body>
</html>