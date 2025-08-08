<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Trainer" %>
<%
    User user = (User) session.getAttribute("user");
    Trainer trainer = (Trainer) session.getAttribute("trainer");
    if (user == null || trainer == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    String avatarUrl = (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() : request.getContextPath() + "/img/default-avatar.jpg";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trainer Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/profile-trainer.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
</head>
<body>
<div class="profile-card mt-5">
    <div class="card shadow-sm profile-wrapper">
        <div class="row no-gutters">
            <!-- Left section -->
            <div class="col-md-4 left-section">
                <div class="text-center mb-3">
                    <img src="<%= avatarUrl %>" class="avatar-profile-page" id="avatarPreview" alt="Profile Avatar" />
                </div>
                <div class="trainer-name mt-3"><%= user.getUserName() %></div>
                <div class="trainer-role text-muted">Professional Trainer</div>
                <!-- Avatar Upload Form -->
                <form action="../ChangeAvatarServlet" method="post" enctype="multipart/form-data" id="avatarForm" class="mt-3">
                    <div class="avatar-upload">
                        <label for="avatarFile" class="upload-btn">
                            <i class="fas fa-camera"></i> Change Avatar
                        </label>
                        <input type="file" id="avatarFile" name="avatar" accept="image/*" style="display: none;" />
                    </div>
                </form>
            </div>
            <!-- Right section -->
            <div class="col-md-8 right-section">
                <h3 class="role-header">Trainer Profile</h3>
                <table class="table table-borderless">
                    <tr><td class="profile-label">Name:</td><td class="profile-value"><%= user.getUserName() %></td></tr>
                    <tr><td class="profile-label">Email:</td><td class="profile-value"><%= user.getEmail() %></td></tr>
                    <tr><td class="profile-label">Gender:</td><td class="profile-value"><%= user.getGender() %></td></tr>
                    <tr><td class="profile-label">Address:</td><td class="profile-value"><%= user.getAddress() %></td></tr>
                    <tr><td class="profile-label">Phone:</td><td class="profile-value"><%= user.getPhone() %></td></tr>
                </table>
                <h4 class="role-header mt-4">About Me</h4>
                <div class="mb-2 p-2 border rounded bg-light">
                    <strong>Description:</strong> <%= trainer.getDescription() %><br/>
                    <strong>Specialization:</strong> <%= trainer.getSpecialization() %><br/>
                    <strong>Experience:</strong> <%= trainer.getExperienceYears() %> years
                </div>
                <div class="btn-group mt-4">
                    <a href="edit-profile-trainer.jsp" class="btn btn-primary">Edit Profile</a>
                    <a href="${pageContext.request.contextPath}/ChangePasswordServlet" class="btn btn-warning ml-2">Change Password</a>
                    <a href="${pageContext.request.contextPath}/CustomerServlet" class="btn btn-secondary ml-2">← Back to Home</a>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
document.getElementById('avatarFile').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('avatarPreview').src = e.target.result;
        };
        reader.readAsDataURL(file);
        document.getElementById('avatarForm').submit();
    }
});
</script>
</body>
</html>
