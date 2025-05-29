<%-- 
    Document   : profile
    Created on : May 26, 2025, 8:11:13 PM
    Author     : ACER
--%>

<%-- 
    Document   : profile
    Created on : May 27, 2025, 6:55:52 PM
    Author     : Admin
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profile</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="login.css" type="text/css">
</head>
<body>
    <div class="container mt-5">
        <div class="text-center">
            <img src="<%= request.getContextPath() + "/" + user.getAvatarUrl() %>" class="rounded-circle" width="150" height="150" />
            <h2>Profile</h2>
        </div>
        <div class="card mt-4">
            <div class="card-body">
                <h5 class="card-title">User Information</h5>
                <ul class="list-group">
                    <li class="list-group-item">ID: <%= user.getId() %></li>
                    <li class="list-group-item">Name: <%= user.getName() %></li>
                    <li class="list-group-item">Gender: <%= user.getGender() %></li>
                    <li class="list-group-item">Email: <%= user.getEmail() %></li>
                    <li class="list-group-item">Phone: <%= user.getPhone() %></li>
                    <li class="list-group-item">Address: <%= user.getAddress() %></li>
                    <li class="list-group-item">Role: <%= user.getRole() %></li>
                    <li class="list-group-item">Status: <%= user.getStatus() %></li>
                </ul>
                <div class="mt-4">
                    <a href="editprofile.jsp" class="btn btn-primary">Edit</a>
                    <a href="index.jsp" class="btn btn-secondary">Back</a>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.3/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>