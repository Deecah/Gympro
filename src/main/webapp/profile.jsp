<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("header.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap + FontAwesome -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <div class="container mt-5">
        <div class="card p-4 shadow-sm">
            <div class="row">
                <!-- Avatar + Upload -->
                <div class="col-md-4 text-center">
                    <img src="<%= user.getAvatarUrl()%>" class="img-thumbnail rounded-circle" style="width: 150px; height: 150px; object-fit: cover;">
                    <form action="ChangeAvatarServlet" method="post" enctype="multipart/form-data" class="mt-3">
                        <div class="custom-file mb-2">
                            <input type="file" name="avatar" class="custom-file-input" id="avatarInput" required>
                            <label class="custom-file-label" for="avatarInput">Choose avatar...</label>
                        </div>
                        <button class="btn btn-primary btn-sm" type="submit">Change avatar</button>
                    </form>
                </div>

                <!-- Thông tin cá nhân -->
                <div class="col-md-8">
                    <h3>Thông tin cá nhân</h3>
                    <table class="table table-borderless">
                        <tr><th>ID:</th><td><%= user.getId() %></td></tr>
                        <tr><th>Name:</th><td><%= user.getName() %></td></tr>
                        <tr><th>Gender:</th><td><%= user.getGender() %></td></tr>
                        <tr><th>Email:</th><td><%= user.getEmail() %></td></tr>
                        <tr><th>Number:</th><td><%= user.getPhone() %></td></tr>
                        <tr><th>Address:</th><td><%= user.getAddress() %></td></tr>
                        <tr><th>Role:</th><td><%= user.getRole() %></td></tr>
                        <tr><th>Status:</th><td><%= user.getStatus() %></td></tr>
                    </table>
                    <a href="header.jsp" class="btn btn-link">← Back to page</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap scripts -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script>
        // Cập nhật tên file khi chọn ảnh
        $(".custom-file-input").on("change", function () {
            var fileName = $(this).val().split("\\").pop();
            $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
        });
    </script>
</body>
</html>
