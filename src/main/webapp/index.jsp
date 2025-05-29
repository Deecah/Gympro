 <%-- 
    Document   : index
    Created on : May 26, 2025, 7:45:55 PM
    Author     : ACER
--%>

<%-- 
    Document   : index
    Created on : May 27, 2025, 6:52:19 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        user = new User();
        user.setId(1);
        user.setName("John Doe");
        user.setGender("Male");
        user.setEmail("john@example.com");
        user.setPhone("0123456789");
        user.setAddress("123 Street");
        user.setAvatarUrl("uploads/avatar.jpg");
        user.setPassword("123456");
        user.setRole("Customer");
        user.setStatus("Normal");
        session.setAttribute("user", user);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Home</title>
        <style>
            .avatar-container {
                position: relative;
                width: 150px;
            }
            .avatar {
                width: 150px;
                height: 150px;
                border-radius: 50%;
                cursor: pointer;
            }
            .edit-icon {
                position: absolute;
                bottom: 5px;
                right: 5px;
                background: #fff;
                border: 1px solid #ccc;
                border-radius: 50%;
                padding: 4px;
                cursor: pointer;
            }
            .avatar-menu {
                margin-top: 10px;
                text-align: left;
            }
            .avatar-menu a {
                display: inline-block;
                padding: 6px 12px;
                background-color: #007BFF;
                color: white;
                text-decoration: none;
                border-radius: 4px;
                font-size: 14px;
            }


            .avatar-container:hover .avatar-menu {
                display: block;
            }
        </style>
    </head>
    <body>
        <<div style="text-align: center;">
            <div class="avatar-container">
                <img src="<%= request.getContextPath() + "/" + user.getAvatarUrl()%>?v=<%= System.currentTimeMillis()%>" class="avatar" />
                <div class="edit-icon" onclick="document.getElementById('uploadForm').style.display = 'block'; event.stopPropagation();">
                    🖊️
                </div>
            </div>

            <!-- Đảm bảo nằm ngoài .avatar-container -->
            <div class="avatar-menu">
                <a href="profile.jsp">View Profile</a>
            </div>
        </div>

    </div>

    <div id="uploadForm" style="display:none;">
        <form action="UploadAvatarServlet" method="post" enctype="multipart/form-data">
            <input type="file" name="avatar" accept="image/*" required>
            <button type="submit">Upload</button>
        </form>
    </div>
</body>
</html>
