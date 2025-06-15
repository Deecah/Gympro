<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="model.User" %>
<%@ page import="dao.UserDAO" %>
<!-- Header Section Begin -->
<header class="header-section" style="position: relative;">
    <% if (user != null) { %>
    <div style="position: absolute; top: 10px; right: 20px; z-index: 1000;">
        <img src="<%= user.getAvatarUrl() %>" onclick="toggleMenu()"
             style="width: 40px; height: 40px; border-radius: 50%; cursor: pointer; border: 2px solid white;">
        <div id="dropdownMenu" style="display: none; position: absolute; right: 0; top: 50px; background-color: white; border: 1px solid #ccc; border-radius: 5px; min-width: 160px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); font-family: sans-serif;">
            <a href="profile.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">👤 Profile</a>
            <a href="confirmOldPass.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">🔒 Password</a>
            <a href="LogOutServlet" style="display: block; padding: 10px; text-decoration: none; color: #333;">🚪 Logout</a>
        </div>
    </div>
    <% } %>

    <!-- Main nav bar -->
    <div class="container">
        <div class="logo">
            <a href="index.jsp">
                <img src="img/logo.png" alt="Logo">
            </a>
        </div>
        <div class="nav-menu">
            <nav class="mainmenu mobile-menu">
                <ul>
                    <li class="active"><a href="index.jsp">Home</a></li>
                    <li><a href="about-us.jsp">About</a></li>
                    <li><a href="classes.jsp">Classes</a></li>
                    <li><a href="blog.jsp">Blog</a></li>
                    <li><a href="gallery.jsp">Gallery</a></li>
                    <li><a href="contact.jsp">Contacts</a></li>
                </ul>
            </nav>
            <a href="login.jsp" class="primary-btn signup-btn">Sign Up Today</a>
        </div>
        <div id="mobile-menu-wrap"></div>
    </div>
</header>
<!-- Header Section End -->

<!-- JavaScript for Avatar Dropdown -->
<script>
    function toggleMenu() {
        const menu = document.getElementById("dropdownMenu");
        menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }

    window.addEventListener("click", function (e) {
        const menu = document.getElementById("dropdownMenu");
