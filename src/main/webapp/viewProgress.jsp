<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*, model.Progress, model.User" %>

<html>
<head>
    <title>Progress</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: center;
        }
        th {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>
    <h2>Progress Tracking</h2>

    <%
        List<Progress> progressList = (List<Progress>) request.getAttribute("progressList");
        User user = (User) request.getAttribute("user");
        boolean isTrainer = (user != null && "Trainer".equalsIgnoreCase(user.getRole()));
    %>
    <div class="debug-log">
        <strong>Debug Log:</strong><br/>
        User: <%= (user != null ? user.getUserName() + " (Role: " + user.getRole() + ")" : "null") %><br/>
        Progress list size: <%= (progressList != null ? progressList.size() : 0) %>
    </div>
    <table>
        <tr>
            <th>Date</th>
            <th>Weight (kg)</th>
            <th>Body Fat (%)</th>
            <th>Muscle Mass (kg)</th>
            <th>Notes</th>
            <% if (isTrainer) { %>
                <th>Action</th>
            <% } %>
        </tr>

        <%
            if (progressList != null && !progressList.isEmpty()) {
                for (Progress p : progressList) {
                
        %>
        <tr>
            <td><%= p.getRecordedAt() %></td>
            <td><%= p.getWeight() %></td>
            <td><%= p.getBodyFatPercent() %></td>
            <td><%= p.getMuscleMass() %></td>
            <td><%= p.getNotes() %></td>
            <% if (isTrainer) { %>
                <td><a href="editProgress?id=<%= p.getProgressID() %>">Edit</a></td>
            <% } %>
        </tr>
        <%
                }
            } else {
        %>
        <tr>
            <td colspan="<%= isTrainer ? 6 : 5 %>">No progress data found.</td>
        </tr>
        <%
            }
        %>
    </table>

</body>
</html>
