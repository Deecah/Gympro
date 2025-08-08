<%-- 
    Document   : editprofile
    Created on : Jun 4, 2025, 1:01:48 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User, model.Customer, model.Trainer" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();

    Customer customer = null;
    Trainer trainer = null;

    if ("Customer".equalsIgnoreCase(role)) {
        customer = (Customer) session.getAttribute("customer");
    } else if ("Trainer".equalsIgnoreCase(role)) {
        trainer = (Trainer) session.getAttribute("trainer");
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Profile</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"/>
        <style>
            .profile-card {
                max-width: 900px;
                margin: auto;
            }
            .role-header {
                font-weight: 700;
                color: #2c3e50;
                margin-bottom: 15px;
                border-bottom: 2px solid #3498db;
                padding-bottom: 5px;
            }
            .form-label {
                font-weight: 600;
            }
        </style>
    </head>
    <body>
        <div class="container profile-card mt-5">
            <div class="card shadow-sm p-4">
                <h3 class="role-header">Edit Profile</h3>
                <form action="ProfileServlet" method="post">
                    <!-- Basic Info -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="form-label">Name</label>
                            <input type="text" name="name" class="form-control" value="<%= user.getUserName() %>">
                        </div>
                        <div class="form-group col-md-6">
                            <label class="form-label">Gender</label>
                            <select name="gender" class="form-control" required>
                                <option value="Male" <%= "Male".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Male</option>
                                <option value="Female" <%= "Female".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Female</option>
                                <option value="Other" <%= "Other".equalsIgnoreCase(user.getGender()) ? "selected" : "" %>>Other</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label class="form-label">Phone</label>
                            <input type="text" name="phone" class="form-control" value="<%= user.getPhone() %>" >
                        </div>
                        <div class="form-group col-md-6">
                            <label class="form-label">Address</label>
                            <input type="text" name="address" class="form-control" value="<%= user.getAddress() %>" >
                        </div>
                    </div>

                    <!-- Role-Specific Info -->
                    <% if ("Customer".equalsIgnoreCase(role) && customer != null) { %>
                    <h4 class="role-header mt-4">🏋️‍♂️ Customer Details</h4>
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label class="form-label">Weight (kg)</label>
                            <input type="number" name="weight" step="0.1" class="form-control" value="<%= customer.getWeight() %>" required>
                        </div>
                        <div class="form-group col-md-4">
                            <label class="form-label">Height (cm)</label>
                            <input type="number" name="height" step="0.1" class="form-control" value="<%= customer.getHeight() %>" required>
                        </div>
                        <div class="form-group col-md-4">
                            <label class="form-label">Medical Conditions</label>
                            <input type="text" name="medicalConditions" class="form-control" value="<%= customer.getMedicalConditions() %>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Goal</label>
                        <textarea name="goal" class="form-control" rows="2"><%= customer.getGoal() %></textarea>
                    </div>
                    <% } else if ("Trainer".equalsIgnoreCase(role) && trainer != null) { %>
                    <h4 class="role-header mt-4">💪 Trainer Details</h4>
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label class="form-label">Experience (years)</label>
                            <input type="number" name="experienceYears" class="form-control" value="<%= trainer.getExperienceYears() %>" required>
                        </div>
                        <div class="form-group col-md-8">
                            <label class="form-label">Specialization</label>
                            <input type="text" name="specialization" class="form-control" value="<%= trainer.getSpecialization() %>">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Description</label>
                        <textarea name="description" class="form-control" rows="2"><%= trainer.getDescription() %></textarea>
                    </div>
                    <% } %>

                    <div class="form-group mt-4">
                        <button type="submit" class="btn btn-success">Save Changes</button>
                        <a href="profile.jsp" class="btn btn-secondary ml-2">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>