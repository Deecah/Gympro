<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.User, model.Customer, model.Trainer" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String role = user.getRole();
    Customer customer = (Customer) session.getAttribute("customer");
    Trainer trainer = (Trainer) session.getAttribute("trainer");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .card {
            max-width: 750px;
            margin: 50px auto;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .form-group i { margin-right: 10px; color: #007bff; }
        .role-section h5 { margin-top: 30px; color: #343a40; }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <h3 class="text-center mb-4">Edit Profile</h3>
            <form action="EditProfileServlet" method="post">
                <input type="hidden" name="id" value="<%= user.getUserId() %>" />

                <div class="form-group">
                    <label><i class="fa fa-user"></i> Name:</label>
                    <input type="text" class="form-control" name="username" value="<%= user.getUserName() %>" required />
                </div>

                <div class="form-group">
                    <label><i class="fa fa-venus-mars"></i> Gender:</label>
                    <select class="form-control" name="gender" required>
                        <option value="he/him" <%= "he/him".equals(user.getGender()) ? "selected" : "" %>>Male</option>
                        <option value="she/her" <%= "she/her".equals(user.getGender()) ? "selected" : "" %>>Female</option>
                        <option value="they/them" <%= "they/them".equals(user.getGender()) ? "selected" : "" %>>Other</option>
                    </select>
                </div>

                <div class="form-group">
                    <label><i class="fa fa-map-marker-alt"></i> Address:</label>
                    <input type="text" class="form-control" name="address" value="<%= user.getAddress() %>" required />
                </div>

                <div class="form-group">
                    <label><i class="fa fa-user-tag"></i> Role:</label>
                    <select class="form-control" name="role" id="role" onchange="toggleRoleFields()" required>
                        <option value="Customer" <%= "Customer".equalsIgnoreCase(role) ? "selected" : "" %>>Customer</option>
                        <option value="Trainer" <%= "Trainer".equalsIgnoreCase(role) ? "selected" : "" %>>Trainer</option>
                        <option value="Admin" <%= "Admin".equalsIgnoreCase(role) ? "selected" : "" %>>Admin</option>
                    </select>
                </div>

                <!-- Customer Section -->
                <div id="customerFields" class="role-section" style="display: none;">
                    <h5>Customer Details</h5>
                    <div class="form-group">
                        <label><i class="fa fa-weight"></i> Weight (kg):</label>
                        <input type="number" step="0.1" class="form-control" name="weight" value="<%= customer != null ? customer.getWeight() : "" %>">
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-ruler-vertical"></i> Height (cm):</label>
                        <input type="number" step="0.1" class="form-control" name="height" value="<%= customer != null ? customer.getHeight() : "" %>">
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-bullseye"></i> Goals:</label>
                        <textarea class="form-control" name="goal" rows="3"><%= customer != null ? customer.getGoal() : "" %></textarea>
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-notes-medical"></i> Medical Conditions:</label>
                        <textarea class="form-control" name="medicalConditions" rows="3"><%= customer != null ? customer.getMedicalConditions() : "" %></textarea>
                    </div>
                </div>

                <!-- Trainer Section -->
                <div id="trainerFields" class="role-section" style="display: none;">
                    <h5>Trainer Details</h5>
                    <div class="form-group">
                        <label><i class="fa fa-calendar-alt"></i> Years of Experience:</label>
                        <input type="number" class="form-control" name="experienceYears" value="<%= trainer != null ? trainer.getExperienceYears() : "" %>">
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-star"></i> Specialization:</label>
                        <input type="text" class="form-control" name="specialization" value="<%= trainer != null ? trainer.getSpecialization() : "" %>">
                    </div>
                    <div class="form-group">
                        <label><i class="fa fa-align-left"></i> Description:</label>
                        <textarea class="form-control" name="description" rows="4"><%= trainer != null ? trainer.getDescription() : "" %></textarea>
                    </div>
                </div>

                <div class="text-center mt-4">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="profile.jsp" class="btn btn-secondary ml-2">Back</a>
                </div>
            </form>
        </div>
    </div>

    <script>
        function toggleRoleFields() {
            const role = document.getElementById("role").value;
            document.getElementById("customerFields").style.display = role === "Customer" ? "block" : "none";
            document.getElementById("trainerFields").style.display = role === "Trainer" ? "block" : "none";
        }
        window.onload = toggleRoleFields;
    </script>
</body>
</html>
