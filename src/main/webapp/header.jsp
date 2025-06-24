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
                    <li><a href="./about-us.jsp">About</a></li>
                    <li><a href="./classes.jsp">Classes</a></li>
                    <li><a href="./CustomerPackageServlet">Package</a></li>
                    <li><a href="./gallery.jsp">Gallery</a></li>
                    <li><a href="./contact.jsp">Contacts</a></li>
                </ul>
            </nav>
        </div>

        <div class="header-avatar" style="position: relative;">
            <img src="<%= (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() 
                : "img/default-avatar.jpg" %>"
                onclick="toggleMenu()" 
                class="avatar-img" 
                alt="Avatar">

            <div id="dropdownMenu" class="avatar-dropdown">
                <a href="profile.jsp"><i class="fa fa-user"></i> Profile</a>
                <a href="#"><i class="fa fa-cube"></i> Packages Purchased</a>
                <a href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out"></i> Logout</a>
            </div>
        </div>

    </div>
</header>
<!-- Header Section End -->