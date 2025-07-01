<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Trainer" %>
<%@ taglib uri="jakarta.tags.core" prefix="c"  %>
<%@ taglib uri="jakarta.tags.fmt"  prefix="fmt"%>  
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Trainer trainer = (Trainer) session.getAttribute("trainer");
    if (trainer == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String avatarUrl = user.getAvatarUrl();
    if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
        String gender = user.getGender();
        if ("Male".equalsIgnoreCase(gender)) {
            avatarUrl = "img/avatar/avatar1.jpg";
        } else if ("Female".equalsIgnoreCase(gender)) {
            avatarUrl = "img/avatar/avatar4.jpg";
        } else {
            avatarUrl = "images/default-avatar.png";
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Trainer Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/profile-trainer.css" />
</head>

<body>
<div class="profile-card mt-5">
    <div class="card shadow-sm profile-wrapper">
        <div class="row no-gutters">
            <!-- Left section -->
            <div class="col-md-4 left-section">
                <img src="${pageContext.request.contextPath}/${user.avatarUrl}" class="img-thumbnail rounded-circle profile-avatar mb-3" />
                <div class="trainer-name"><%= user.getUserName() %></div>

                <form action="${pageContext.request.contextPath}/ChangeAvatarServlet" method="post" enctype="multipart/form-data">
                    <div class="avatar-upload-row">
                        <label for="avatarInput" class="upload-icon-btn" title="Choose file">
                            <i class="fas fa-folder-open"></i>
                        </label>
                        <button type="submit" class="confirm-icon-btn" title="Change Avatar">
                            <i class="fas fa-sync-alt"></i>
                        </button>
                        <input type="file" name="avatar" id="avatarInput" required />
                    </div>
                </form>

                <div class="about-me mt-4 w-100">
                    <h5 class="text-white mb-3">About Me</h5>
                    <p><strong>Description:</strong> <%= trainer.getDescription() %></p>
                    <p><strong>Specialization:</strong> <%= trainer.getSpecialization() %></p>
                    <p><strong>Experience:</strong> <%= trainer.getExperienceYears() %> years</p>
                </div>
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

                <h4 class="role-header mt-4">🎓 Certifications</h4>
                <c:forEach var="cert" items="${certifications}">
                    <div class="mb-2 p-2 border rounded bg-light">
                        <strong>${cert.name}</strong><br/>
                        <span>${cert.description}</span><br/>
                        <small>Expires <fmt:formatDate value="${cert.expireDate}" pattern="dd/MM/yyyy"/></small>
                    </div>
                </c:forEach>

                <div class="btn-group mt-4">
                    <a href="${pageContext.request.contextPath}/ProfileServlet" class="btn btn-primary">Edit Profile</a>
                    <a href="${pageContext.request.contextPath}/EditCertificationServlet" class="btn btn-brown ml-2">Edit Certification</a>
                    <a href="../confirmOldPass.jsp" class="btn btn-warning ml-2">Change Password</a>
                    <a href="${pageContext.request.contextPath}/Navigate?target=backtohome" class="btn btn-secondary ml-2">← Back to Home</a>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script>
    $(".custom-file-input").on("change", function () {
        var fileName = $(this).val().split("\\").pop();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
    });
</script>
</body>
</html>
