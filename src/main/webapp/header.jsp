<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("user");
%>

<!-- Header Section Begin -->
<header class="header-section">
    <div class="container" style="display: flex; align-items: center; justify-content: space-between;">
        <div class="logo">
            <a href="./index.jsp">
                <img src="img/logo-web.png" class="gympro-logo" alt="">
            </a>
        </div>

        <div class="nav-menu" style="flex: 1; text-align: center;">
            <nav class="mainmenu mobile-menu">
                <ul style="display: inline-flex; gap: 20px;">
                    <li><a href="./index.jsp">Home</a></li>
                    <li><a href="./about-us.html">About</a></li>
                    <li><a href="./classes.html">Classes</a></li>
                    <li><a href="./CustomerPackageServlet">Package</a></li>
                    <li><a href="./gallery.html">Gallery</a></li>
                    <li><a href="./contact.html">Contacts</a></li>
                </ul>
            </nav>
        </div>

        <div class="header-avatar" style="position: relative;">
            <img src="<%= (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() : "images/default-avatar.png" %>" 
                 onclick="toggleMenu()" 
                 style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%; border: 2px solid white; cursor: pointer;">
            <div id="dropdownMenu" style="display: none; position: absolute; right: 0; top: 50px; background-color: white; border: 1px solid #ccc; border-radius: 5px; min-width: 160px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); font-family: sans-serif;">
                <a href="profile.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">? Profile</a>
                <a href="LogOutServlet" style="display: block; padding: 10px; text-decoration: none; color: #333;">? Logout</a>
            </div>
        </div>
    </div>
</header>
<!-- Header Section End -->
