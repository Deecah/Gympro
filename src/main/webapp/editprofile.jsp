<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("profile.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Profile</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            max-width: 600px;
            margin: 50px auto;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .form-group i {
            margin-right: 10px;
            color: #007bff;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <h3 class="text-center mb-4">Edit Profile</h3>
            <form action="EditProfileServlet" method="post">
                <input type="hidden" name="id" value="<%= user.getId() %>" />

                <div class="form-group">
                    <label for="name"><i class="fa fa-user"></i> Name:</label>
                    <input type="text" class="form-control" id="name" name="name" value="<%= user.getName() %>" required />
                </div>

                <div class="form-group">
                    <label for="gender"><i class="fa fa-venus-mars"></i> Gender:</label>
                    <select class="form-control" name="gender" id="gender" required>
                        <option value="he/him" <%= "he/him".equals(user.getGender()) ? "selected" : "" %>>Male</option>
                        <option value="she/her" <%= "she/her".equals(user.getGender()) ? "selected" : "" %>>Female</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="email"><i class="fa fa-envelope"></i> Email:</label>
                    <input type="email" class="form-control" id="email" name="email" value="<%= user.getEmail() %>" required />
                </div>

                <div class="form-group">
                    <label for="phone"><i class="fa fa-phone"></i> Phone:</label>
                    <input type="text" class="form-control" id="phone" name="phone" value="<%= user.getPhone() %>" required />
                </div>

                <div class="form-group">
                    <label for="address"><i class="fa fa-map-marker-alt"></i> Address:</label>
                    <input type="text" class="form-control" id="address" name="address" value="<%= user.getAddress() %>" required />
                </div>

                <div class="form-group">
                    <label for="role"><i class="fa fa-user-tag"></i> Role:</label>
                    <input type="text" class="form-control" id="role" name="role" value="<%= user.getRole() %>" required />
                </div>

                <div class="form-group">
                    <label for="status"><i class="fa fa-info-circle"></i> Status:</label>
                    <input type="text" class="form-control" id="status" name="status" value="<%= user.getStatus() %>" required />
                </div>

                <div class="text-center">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="index.jsp" class="btn btn-secondary ml-2">Back</a>
                </div>
            </form>

            <footer class="text-center mt-4">
                <small>
                    Created with <i class="fa fa-heart text-danger"></i> by
                    <a target="_blank" href="https://florin-pop.com">Florin Pop</a> -
                    Learn more <a target="_blank" href="#">here</a>.
                </small>
            </footer>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
