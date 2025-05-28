<%-- 
    Document   : header
    Created on : May 28, 2025, 3:08:09 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
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
        user.setAvatarURL("uploads/avatar.jpg");
        user.setPassword("123456");
        user.setRole("Customer");
        user.setStatus("Normal");
        session.setAttribute("user", user);
    }
%>
<!DOCTYPE html>
<html>
    <a href="confirmOldPass.jsp">Change Password</a>

    <div style="display: flex; justify-content: flex-end; align-items: center; padding: 10px; background-color: #18191A;">
        <div style="position: relative;">
            <img src="<%= user.getAvatarURL() %>" onclick="toggleMenu()" style="width: 40px; height: 40px; border-radius: 50%; cursor: pointer;">
            <div class="edit-icon" onclick="document.getElementById('uploadForm').style.display = 'block'; event.stopPropagation();">
                    🖊️
                </div>
        </div>
    </div>
    <script>
        function toggleMenu() {
            var menu = document.getElementById("dropdownMenu");
            menu.style.display = (menu.style.display === "block") ? "none" : "block";
        }

        // Tự động tắt menu khi click ra ngoài
        window.addEventListener("click", function (e) {
            if (!e.target.matches("img")) {
                document.getElementById("dropdownMenu").style.display = "none";
            }
        });
    </script>
</html>
