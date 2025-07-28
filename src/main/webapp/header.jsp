<%@page import="java.util.List"%>
<%@page import="model.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="model.User" %>
<%@ page import="dao.NotificationDAO" %>
<%@ page import="dao.UserDAO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
%>

<%
    User user = (User) session.getAttribute("user");
    System.out.println("HEADER user ID = " + (user != null ? user.getUserId() : "null"));
    pageContext.setAttribute("user", user);
    List<Notification> notifications = new ArrayList<>();

    if (user != null) {
        NotificationDAO notiDAO = new NotificationDAO();
        notifications = notiDAO.getNotificationsByUserId(user.getUserId());
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
    }
%>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
        
        <script>
            // Set current user ID for notification.js
            <% if (user != null) { %>
            var currentUserId = <%= user.getUserId() %>;
            <% } else { %>
            var currentUserId = null;
            <% } %>
        </script>
        <script src="${pageContext.request.contextPath}/js/notification.js"></script>
        

<!-- Font Awesome 5 -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- Header Section Begin -->
<header class="header-section">
    <div class="container d-flex align-items-center justify-content-between flex-wrap py-2">
        <div class="logo">
            <a href="./index.jsp">
                <img src="img/logo-web.png" class="gympro-logo" alt="">
            </a>
        </div>

        <div class="nav-menu" style="flex: 1; text-align: center;">
            <nav class="mainmenu mobile-menu">
                <ul style="display: inline-flex; gap: 20px;">
                    <li><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/ExpertTrainerServlet">Trainers</a></li>
                    <li><a href="${pageContext.request.contextPath}/BlogServlet">Blog</a></li>
                    <li><a href="${pageContext.request.contextPath}/CustomerPackageServlet">Package</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact.jsp">Contacts</a></li>
                </ul>
            </nav>
        </div>

        <div class="header-controls">
            <% if (user != null) {%>
            <div class="notification-bell" id="notificationBell">
                <i class="fas fa-bell"></i>
                <span class="notification-count" id="notificationCount"><%= notifications.size()%></span>
            </div>
            <% } %>

            <div class="notification-box" id="notificationBox">
                <div class="notification-header">
                    Notifications
                </div>
                <ul class="notification-list" id="notificationList">
                    <% if (notifications.isEmpty()) { %>
                    <li class="no-notifications">No new notifications!</li>
                        <% } else { %>
                        <% for (Notification noti : notifications) {%>
                    <li class="notification-item">
                        <p><%= noti.getContent()%></p>
                        <span class="notification-time"><%= noti.getTimeAgo()%></span>
                    </li>
                    <% } %>
                    <% }%>
                </ul>
            </div>
        </div>

        <div class="header-avatar" style="position: relative;">
            <% if (user != null) { %>
                <!-- User đã đăng nhập -->
                <img src="<%= (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() : "img/default-avatar.jpg"%>"
                     onclick="toggleMenu()" 
                     class="avatar-img" 
                     alt="Avatar">
                <div id="dropdownMenu" class="avatar-dropdown">
                    <a href="${pageContext.request.contextPath}/profile.jsp"><i class="fa fa-user"></i> Profile</a>
                    <a href="packagesPurchased"><i class="fa fa-cube"></i> Packages Purchased</a>
                    <a href="${pageContext.request.contextPath}/timetable"><i class="fa fa-calendar"></i> Schedule</a>
                    <a href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out"></i> Logout</a>
                </div>
            <% } else { %>
                <!-- User chưa đăng nhập (guest) -->
                <img src="img/default-avatar.jpg"
                     onclick="toggleGuestMenu()" 
                     class="avatar-img" 
                     alt="Avatar">

                <div id="guestDropdownMenu" class="avatar-dropdown">
                    <a href="${pageContext.request.contextPath}/login.jsp"><i class="fa fa-sign-in"></i> Login</a>
                    <a href="${pageContext.request.contextPath}/login.jsp#signup"><i class="fa fa-user-plus"></i> Register</a>
                </div>
            <% } %>
        </div>
    </div>
</header>

