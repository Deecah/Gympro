<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Trainer" %>
<%
    User user = (User) session.getAttribute("user");
    Trainer trainer = (Trainer) session.getAttribute("trainer");

    if (user == null || trainer == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Trainer Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/profile-trainer.css" />
    
    <script>
        // Set current user ID for notification.js
        <% if (session.getAttribute("user") != null) { %>
        var currentUserId = <%= ((model.User)session.getAttribute("user")).getUserId() %>;
        <% } else { %>
        var currentUserId = null;
        <% } %>
    </script>
    <script src="../js/notification.js"></script>
</head>
<body>
<div class="profile-card mt-5">
    <div class="card shadow-sm profile-wrapper">
        <div class="row no-gutters">
            <!-- Left section -->
            <div class="col-md-4 left-section">
                <img src="${pageContext.request.contextPath}/${user.avatarUrl}" class="img-thumbnail rounded-circle profile-avatar mb-3" />
                <div class="trainer-name"><%= user.getUserName() %></div>
            </div>

            <!-- Right section -->
            <div class="col-md-8 right-section">
                <h3 class="role-header">Edit Profile</h3>
                <form action="../TrainerProfileServlet" method="post">
                    <div class="form-group">
                        <label>Name:</label>
                        <input type="text" name="name" value="<%= user.getUserName() %>" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label>Gender:</label>
                        <select name="gender" class="form-control">
                            <option value="Male" <%= "Male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>
                            <option value="Female" <%= "Female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>
                            <option value="" <%= user.getGender() == null || user.getGender().isEmpty() ? "selected" : "" %>>Other</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Phone:</label>
                        <input type="text" name="phone" value="<%= user.getPhone() %>" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Address:</label>
                        <input type="text" name="address" value="<%= user.getAddress() %>" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Description:</label>
                        <textarea name="description" class="form-control"><%= trainer.getDescription() %></textarea>
                    </div>
                    <div class="form-group">
                        <label>Specialization:</label>
                        <input type="text" name="specialization" value="<%= trainer.getSpecialization() %>" class="form-control" />
                    </div>
                    <div class="form-group">
                        <label>Experience (years):</label>
                        <input type="number" name="experience" value="<%= trainer.getExperienceYears() %>" class="form-control" />
                    </div>
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="profile-trainer.jsp" class="btn btn-secondary ml-2">Cancel</a>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
