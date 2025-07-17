<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Packages Purchased</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: #f9f9f9;
            padding: 30px;
            font-family: 'Segoe UI', sans-serif;
        }
        .table-container {
            background: #fff;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
        h2 {
            font-weight: bold;
            margin-bottom: 25px;
        }
        .btn-feedback {
            font-size: 0.9rem;
        }
        .modal-content {
            border-radius: 12px;
        }
    </style>
</head>
<body>
    <div class="container table-container text-center">
        <h2>📦 Packages You've Purchased</h2>

        <table class="table table-striped">
            <thead class="table-light">
                <tr>
                    <th class="text-center">Package Name</th>
                    <th class="text-center">Trainer Name</th>
                    <th class="text-center">Start Date</th>
                    <th class="text-center">End Date</th>
                    <th class="text-center">Status</th>
                    <th class="text-center">Feedback</th>
                    <th class="text-center">Schedule</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty purchasedList}">
                        <c:forEach var="row" items="${purchasedList}">
                            <tr>
                                <td>${row[0]}</td>
                                <td>${row[1]}</td>
                                <td>${row[2]}</td>
                                <td>${row[3]}</td>
                                <td>${row[4]}</td>
                                <td>
                                    <button class="btn btn-sm btn-outline-primary btn-feedback"
                                            onclick="openFeedbackModal('package', ${row[5]})">
                                        Give Feedback
                                    </button>
                                </td>
                                <td> <%-- ✅ New Schedule Button --%>
                                    <form action="schedule.jsp" method="get" style="margin: 0;">
                                        <input type="hidden" name="packageId" value="${row[5]}" />
                                        <button type="submit" class="btn btn-sm btn-outline-secondary">
                                            Schedule
                                        </button>
                                    </form>
                        </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="text-center">You have not purchased any packages yet.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <!-- Feedback Modal -->
    <div class="modal fade" id="feedbackModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <form action="feedback" method="post">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Give Feedback</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="userId" value="${user.userId}" />
                        <input type="hidden" name="type" id="feedbackType" />
                        <input type="hidden" name="referenceId" id="referenceId" />

                        <label>Rating:</label><br/>
                        <select name="point" class="form-select mb-3">
                            <c:forEach begin="1" end="5" var="i">
                                <option value="${i}">${i} Star(s)</option>
                            </c:forEach>
                        </select>

                        <label>Comment (optional):</label>
                        <textarea name="content" class="form-control" rows="3"></textarea>
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Submit Feedback</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- JS Dependencies -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function openFeedbackModal(type, referenceId) {
            document.getElementById("feedbackType").value = type;
            document.getElementById("referenceId").value = referenceId;
            let modal = new bootstrap.Modal(document.getElementById('feedbackModal'));
            modal.show();
        }
    </script>
</body>
</html>
