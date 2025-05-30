<%-- 
    Document   : header
    Created on : May 28, 2025, 3:08:09 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>

<%@ page import="model.User" %>
<%@ page import="dao.UserDAO" %>



<%@ page import="dao.UserDAO" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");

    if (user == null) {
        UserDAO dao = new UserDAO();
        user = dao.getUserByEmail("test2@gmail.com"); // Đổi email nếu cần

        if (user != null) {
            session.setAttribute("user", user);
        } else {
            out.println("<p style='color:red;'>User not found in database. Please check email.</p>");
            return; // Dừng hiển thị tiếp
        }
    }
%>



<!DOCTYPE html>
<html>
    <div style="display: flex; justify-content: flex-end; align-items: center; padding: 10px; background-color: #18191A;">
        <div style="position: relative;">
            <img src="<%= user.getAvatarUrl()%>" onclick="toggleMenu()" style="width: 40px; height: 40px; border-radius: 50%; cursor: pointer;">
            <div id="dropdownMenu" style="display: none; position: absolute; right: 0; background-color: white; border: 1px solid #ccc; border-radius: 5px; min-width: 160px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); font-family: sans-serif;">
                <a href="profile.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">
                    👤 Profile
                </a>
                <a href="change_password.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">
                    🔒 Change Password
                </a>
                <a href="LogOutServlet" style="display: block; padding: 10px; text-decoration: none; color: #333;">
                    🚪 Logout
                </a>
            </div>
        </div>
    </div>

    <script>
        function toggleMenu() {
            const menu = document.getElementById("dropdownMenu");
            menu.style.display = (menu.style.display === "block") ? "none" : "block";
        }

        // Tự động đóng menu khi click bên ngoài
        window.addEventListener("click", function (e) {
            const menu = document.getElementById("dropdownMenu");
            if (!e.target.closest('#dropdownMenu') && !e.target.matches('img')) {
                menu.style.display = "none";


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
        window.addEventListener("click", function (e) {
            if (!e.target.matches("img")) {
                document.getElementById("dropdownMenu").style.display = "none";

            }
        });
    </script>
</html>
