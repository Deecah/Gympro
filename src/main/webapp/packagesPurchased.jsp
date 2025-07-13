<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    class PackageItem {
        int packageId;
        String packageName;
        int trainerId;
        String trainerName;

        public PackageItem(int packageId, String packageName, int trainerId, String trainerName) {
            this.packageId = packageId;
            this.packageName = packageName;
            this.trainerId = trainerId;
            this.trainerName = trainerName;
        }
    }

    List<PackageItem> purchasedList = List.of(
        new PackageItem(1, "Yoga Beginner", 101, "Trainer Alice"),
        new PackageItem(2, "Strength Mastery", 102, "Trainer Bob")
    );
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Packages Purchased</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        body {
            margin: 0;
            padding-top: 120px; /* Adjust if header height changes */
            background-color: #f9f9f9;
            font-family: sans-serif;
        }

        .container {
            max-width: 1000px;
            margin: auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 14px;
            border: 1px solid #ccc;
            text-align: center;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 2000;
            top: 0; left: 0;
            width: 100%; height: 100%;
            background: rgba(0, 0, 0, 0.6);
        }

        .modal-content {
            background-color: #fff;
            margin: 10% auto;
            padding: 20px;
            width: 400px;
            border-radius: 10px;
            position: relative;
        }

        .close {
            position: absolute;
            top: 10px;
            right: 15px;
            font-size: 20px;
            cursor: pointer;
        }

        button {
            padding: 8px 14px;
            cursor: pointer;
            border: none;
            background-color: #f36100;
            color: white;
            border-radius: 5px;
        }

        button:hover {
            background-color: #d85000;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container">
    <h2 style="text-align: center;">Your Purchased Packages</h2>

    <table>
        <tr>
            <th>Package Name</th>
            <th>Trainer</th>
            <th>Feedback (Package)</th>
            <th>Feedback (Trainer)</th>
        </tr>
        <% for (PackageItem p : purchasedList) { %>
        <tr>
            <td><%= p.packageName %></td>
            <td><%= p.trainerName %></td>
            <td><button onclick="openFeedbackModal('package', <%= p.packageId %>)">⭐ Rate Package</button></td>
            <td><button onclick="openFeedbackModal('trainer', <%= p.trainerId %>)">🧑‍🏫 Rate Trainer</button></td>
        </tr>
        <% } %>
    </table>
</div>

<!-- Feedback Modal -->
<div id="feedbackModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h3 style="text-align: center;">Submit Feedback</h3>
        <form action="feedback" method="post">
            <input type="hidden" name="userId" value="<%= user.getUserID() %>">
            <input type="hidden" name="type" id="feedbackType">
            <input type="hidden" name="referenceId" id="referenceId">

            <label>Rating (1-5)*:</label><br>
            <input type="number" name="point" min="1" max="5" required><br><br>

            <label>Comment (optional):</label><br>
            <textarea name="content" rows="4" cols="40"></textarea><br><br>

            <button type="submit">Submit</button>
            <button type="button" onclick="closeModal()">Cancel</button>
        </form>
    </div>
</div>

<script>
    function openFeedbackModal(type, referenceId) {
        document.getElementById('feedbackType').value = type;
        document.getElementById('referenceId').value = referenceId;
        document.getElementById('feedbackModal').style.display = 'block';
    }

    function closeModal() {
        document.getElementById('feedbackModal').style.display = 'none';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('feedbackModal');
        if (event.target === modal) {
            closeModal();
        }
    }
</script>

</body>
</html>
