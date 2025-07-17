<%@page import="java.util.List"%>
<%@page import="model.Notification"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="model.User" %>
<%@ page import="dao.NotificationDAO" %>
<%@ page import="dao.UserDAO" %>
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
                    <li><a href="${pageContext.request.contextPath}/about-us.jsp">About</a></li>
                    <li><a href="${pageContext.request.contextPath}/classes.jsp">Classes</a></li>
                    <li><a href="${pageContext.request.contextPath}/CustomerPackageServlet">Package</a></li>
                    <li><a href="${pageContext.request.contextPath}/gallery.jsp">Gallery</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact.jsp">Contacts</a></li>
                </ul>
            </nav>
        </div>
        
        <div class="header-controls">
            <% if (user != null) { %>
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
                    <li class="no-notifications">B?n không có thông báo m?i.</li>
                    <% } else { %>
                        <% for (Notification noti : notifications) {%>
                    <li class="notification-item">
                        <p><%= noti.getContent()%></p>
                        <span class="notification-time"><%= noti.getTimeAgo()%></span>
                    </li>
                        <% } %>
                    <% } %>
                </ul>
            </div>
        </div>

        <div class="header-avatar" style="position: relative;">
            <img src="<%= (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() 
                : "img/default-avatar.jpg" %>"
                onclick="toggleMenu()" 
                class="avatar-img" 
                alt="Avatar">

            <div id="dropdownMenu" class="avatar-dropdown">
                <a href="${pageContext.request.contextPath}/profile.jsp"><i class="fa fa-user"></i> Profile</a>
                <a href="#"><i class="fa fa-cube"></i> Packages Purchased</a>
                <a href="${pageContext.request.contextPath}/timetable"><i class="fa fa-calendar"></i> Schedule</a>
                <a href="${pageContext.request.contextPath}/logout"><i class="fa fa-sign-out"></i> Logout</a>
            </div>
        </div>
   

    </div>
</header>
<!-- Header Section End -->