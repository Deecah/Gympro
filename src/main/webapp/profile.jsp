<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Customer, model.Trainer" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
<<<<<<< HEAD

    String role = user.getRole();
    Customer customer = (Customer) session.getAttribute("customer");
    Trainer trainer = (Trainer) session.getAttribute("trainer");
=======
    String role = user.getRole();

    Customer customer = null;
    Trainer trainer = null;

    if ("Customer".equalsIgnoreCase(role)) {
        customer = (Customer) session.getAttribute("customer");
    } else if ("Trainer".equalsIgnoreCase(role)) {
        trainer = (Trainer) session.getAttribute("trainer");
    }
>>>>>>> 6e8f4d57b22266e152bba8aae60389a2c10e5fc1
%>

<!DOCTYPE html>
<html>
<head>
<<<<<<< HEAD
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="container mt-5">
        <!-- Profile Basic Info -->
        <div class="card p-4 shadow-sm">
            <div class="row">
                <!-- Avatar Section -->
                <div class="col-md-4 text-center">
                    <img src="<%= user.getAvatarUrl() %>" class="img-thumbnail rounded-circle" style="width:150px; height:150px; object-fit:cover;">
                    <form action="ChangeAvatarServlet" method="post" enctype="multipart/form-data" class="mt-3">
                        <div class="custom-file mb-2">
                            <input type="file" name="avatar" class="custom-file-input" id="avatarInput" required>
                            <label class="custom-file-label" for="avatarInput">Choose avatar...</label>
                        </div>
                        <button class="btn btn-primary btn-sm" type="submit">Change Avatar</button>
                    </form>
                </div>

                <!-- Basic Info -->
                <div class="col-md-8">
                    <h3>Profile Details</h3>
                    <table class="table table-borderless">
                        <tr><th>ID:</th><td><%= user.getUserId() %></td></tr>
                        <tr><th>Name:</th><td><%= user.getUserName() %></td></tr>
                        <tr><th>Gender:</th><td><%= user.getGender() %></td></tr>
                        <tr><th>Address:</th><td><%= user.getAddress() %></td></tr>
                        <tr><th>Role:</th><td><%= user.getRole() %></td></tr>
                    </table>

                    <a href="editprofile.jsp" class="btn btn-primary">Edit</a>
                    <a href="index.jsp" class="btn btn-secondary ml-2">← Back</a>
                </div>
            </div>
        </div>

        <!-- Role-Specific Info -->
        <div class="col-md-8">
        <% if ("Customer".equalsIgnoreCase(role) && customer != null) { %>
            <div class="card p-4 mt-4 shadow-sm">
                <h4>🏋️‍♂️ Customer Details</h4>
                <table class="table table-borderless">
                    <tr><th>Weight:</th><td><%= customer.getWeight() %> kg</td></tr>
                    <tr><th>Height:</th><td><%= customer.getHeight() %> cm</td></tr>
                    <tr><th>Goal:</th><td><%= customer.getGoal() %></td></tr>
                    <tr><th>Medical Conditions:</th><td><%= customer.getMedicalConditions() %></td></tr>
                </table>
            </div>
        <% } else if ("Trainer".equalsIgnoreCase(role) && trainer != null) { %>
            <div class="card p-4 mt-4 shadow-sm">
                <h4>💪 Trainer Details</h4>
                <table class="table table-borderless">
                    <tr><th>Experience (years):</th><td><%= trainer.getExperienceYears() %></td></tr>
                    <tr><th>Specialization:</th><td><%= trainer.getSpecialization() %></td></tr>
                    <tr><th>Description:</th><td><%= trainer.getDescription() %></td></tr>
                </table>
            </div>
        <% } %>
        </div>
    </div>

    <!-- Bootstrap Scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script>
        $(".custom-file-input").on("change", function () {
            var fileName = $(this).val().split("\\").pop();
            $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
        });
    </script>
=======
    <meta charset="UTF-8" />
    <title>Profile</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <style>
        .profile-card { max-width: 900px; margin: auto;}
        .profile-avatar { width: 180px; height: 180px; object-fit: cover;}
        .profile-section { margin-top: 20px; }
        .profile-label { font-weight: 600; width: 130px; }
        .profile-value { color: #555; }
        .role-header { font-weight: 700; color: #2c3e50; margin-bottom: 15px; border-bottom: 2px solid #3498db; padding-bottom: 5px; }
        .btn-group-custom { margin-top: 20px;}
    </style>
</head>
<body>
<div class="container profile-card mt-5">
    <div class="card shadow-sm p-4">
        <div class="row">
            <div class="col-md-4 text-center">
                <img src="<%= (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() : "images/default-avatar.png" %>"
                     class="img-thumbnail rounded-circle profile-avatar" alt="Avatar" />
                <form action="ChangeAvatarServlet" method="post" enctype="multipart/form-data" class="mt-3">
                    <div class="custom-file">
                        <input type="file" name="avatar" class="custom-file-input" id="avatarInput" required />
                        <label class="custom-file-label" for="avatarInput">Choose avatar....</label>
                    </div>
                    <button class="btn btn-primary btn-sm btn-block mt-3">Change Avatar</button>
                </form>
            </div>

            <div class="col-md-8">
                <h3 class="role-header">Profile Details</h3>
                <table class="table table-borderless">
                    <tbody>
                    <tr>
                        <td class="profile-label">Name:</td>
                        <td class="profile-value"><%= user.getUserName() %></td>
                    </tr>
                    <tr>
                        <td class="profile-label">Gender:</td>
                        <td class="profile-value"><%= user.getGender() %></td>
                    </tr>
                    <tr>
                        <td class="profile-label">Address:</td>
                        <td class="profile-value"><%= user.getAddress() %></td>
                    </tr>
                    <tr>
                        <td class="profile-label">Phone:</td>
                        <td class="profile-value"><%= user.getPhone() %></td>
                    </tr>
                    </tbody>
                </table>

                <% if ("Customer".equalsIgnoreCase(role) && customer != null) { %>
                <div class="profile-section">
                    <h4 class="role-header">🏋️‍♂️ Customer Details</h4>
                    <table class="table table-borderless">
                        <tbody>
                        <tr><td class="profile-label">Weight:</td><td class="profile-value"><%= customer.getWeight() %> kg</td></tr>
                        <tr><td class="profile-label">Height:</td><td class="profile-value"><%= customer.getHeight() %> cm</td></tr>
                        <tr><td class="profile-label">Goal:</td><td class="profile-value"><%= customer.getGoal() %></td></tr>
                        <tr><td class="profile-label">Medical Conditions:</td><td class="profile-value"><%= customer.getMedicalConditions() %></td></tr>
                        </tbody>
                    </table>
                </div>
                <% } else if ("Trainer".equalsIgnoreCase(role) && trainer != null) { %>
                <div class="profile-section">
                    <h4 class="role-header">💪 Trainer Details</h4>
                    <table class="table table-borderless">
                        <tbody>
                        <tr><td class="profile-label">Experience (years):</td><td class="profile-value"><%= trainer.getExperienceYears() %></td></tr>
                        <tr><td class="profile-label">Specialization:</td><td class="profile-value"><%= trainer.getSpecialization() %></td></tr>
                        <tr><td class="profile-label">Description:</td><td class="profile-value"><%= trainer.getDescription() %></td></tr>
                        </tbody>
                    </table>
                </div>
                <% } %>

                <div class="btn-group-custom">
                    <a href="editprofile.jsp" class="btn btn-primary">Edit Profile</a>
                    <a href="confirmOldPass.jsp" class="btn btn-warning ml-2">Change Password</a>
                    <a href="LogoutServlet" class="btn btn-danger ml-2">Logout</a>
                    <a href="index.jsp" class="btn btn-secondary ml-2">← Back to Home</a>
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
>>>>>>> 6e8f4d57b22266e152bba8aae60389a2c10e5fc1
</body>
</html>
