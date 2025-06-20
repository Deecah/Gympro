<%@page import="dao.UserDAO"%>
<%@page import="java.sql.SQLException"%>
<%@page import="dao.NotificationDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.Notification" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Gympro Template">
        <meta name="keywords" content="Gympro, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Gympro</title>

        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap"
              rel="stylesheet">

        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

        <style>
            /* CSS cho biểu tượng chuông và thông báo */
            .header-controls {
                display: flex;
                align-items: center;
                gap: 15px; /* Khoảng cách giữa avatar và chuông */
                position: relative; /* Quan trọng để định vị hộp thông báo */
            }

            .notification-bell {
                position: relative;
                cursor: pointer;
                font-size: 22px; /* Kích thước icon chuông */
                color: #555; /* Màu icon */
                padding: 5px;
                /* background-color: #f0f0f0; */ /* Có thể bỏ background nếu không muốn */
                border-radius: 50%;
                transition: color 0.3s ease;
            }

            .notification-bell:hover {
                color: #007bff; /* Màu khi hover */
            }

            .notification-count {
                position: absolute;
                top: 0px; /* Điều chỉnh vị trí của badge */
                right: 0px; /* Điều chỉnh vị trí của badge */
                background-color: red;
                color: white;
                border-radius: 50%;
                padding: 2px 6px;
                font-size: 11px;
                line-height: 1;
                display: flex; /* Dùng flex để căn giữa số */
                justify-content: center;
                align-items: center;
                min-width: 18px; /* Đảm bảo hình tròn ngay cả với 1 chữ số */
                height: 18px;
                transform: translate(50%, -50%); /* Dịch chuyển để badge nằm đúng góc trên phải của chuông */
                z-index: 10; /* Đảm bảo badge nằm trên chuông */
            }

            .notification-box {
                display: none; /* Ban đầu ẩn */
                position: absolute; /* Quan trọng: relative với .header-controls */
                top: calc(100% + 10px); /* Dưới chuông một chút */
                right: 0;
                width: 350px; /* Chiều rộng của bảng thông báo */
                max-height: 400px; /* Chiều cao tối đa, có thể cuộn */
                overflow-y: auto; /* Cho phép cuộn nếu nhiều thông báo */
                background-color: white;
                border: 1px solid #ddd;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15); /* Shadow nổi bật hơn */
                border-radius: 8px;
                z-index: 1000; /* Đảm bảo nó nằm trên các nội dung khác */
                animation: fadeInScale 0.2s ease-out; /* Hiệu ứng nhỏ khi xuất hiện */
            }

            @keyframes fadeInScale {
                from {
                    opacity: 0;
                    transform: scale(0.9);
                }
                to {
                    opacity: 1;
                    transform: scale(1);
                }
            }

            .notification-header {
                padding: 12px 15px;
                border-bottom: 1px solid #eee;
                font-weight: bold;
                background-color: #f9f9f9;
                font-size: 16px;
                color: #333;
            }

            .notification-list {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .notification-item {
                padding: 12px 15px;
                border-bottom: 1px solid #eee;
                transition: background-color 0.2s ease;
            }

            .notification-item:last-child {
                border-bottom: none;
            }

            .notification-item:hover {
                background-color: #f5f5f5;
            }

            .notification-item a {
                text-decoration: none;
                color: #333;
                display: block;
            }

            .notification-item p {
                margin: 0;
                font-size: 14px;
                line-height: 1.4;
                color: #444;
            }

            .notification-time {
                font-size: 12px;
                color: #888;
                margin-top: 5px;
                display: block;
            }

            .no-notifications {
                padding: 20px;
                text-align: center;
                color: #777;
                font-style: italic;
                font-size: 14px;
            }

            /* Điều chỉnh header-avatar để có khoảng trống cho nút chuông */
            .header-avatar {
                /* Giữ các style đã có của bạn */
                position: relative; /* Đảm bảo dropdown của avatar hoạt động đúng */
            }
            /* Đảm bảo dropdown menu của avatar không bị che bởi notification box */
            #dropdownMenu {
                z-index: 1001; /* Lớn hơn notification-box */
            }

        </style>
    </head>

    <body>
        <div id="preloder">
            <div class="loader"></div>
        </div>

        <header class="header-section">
            <div class="container" style="display: flex; align-items: center; justify-content: space-between;">

                <div class="logo">
                    <a href="./index.html">
                        <img src="img/logo.png" alt="">
                    </a>
                </div>

                <div class="nav-menu" style="flex: 1; text-align: center;">
                    <nav class="mainmenu mobile-menu">
                        <ul style="display: inline-flex; gap: 20px;">
                            <li class="active"><a href="./index.jsp">Home</a></li>
                            <li><a href="./about-us.html">About</a></li>
                            <li><a href="./classes.html">Classes</a></li>
                            <li><a href="./blog.html">Blog</a></li>
                            <li><a href="./gallery.html">Gallery</a></li>
                            <li><a href="./contact.html">Contacts</a></li>
                        </ul>
                    </nav>
                </div>
                <%  //User user = (User) session.getAttribute("user");
                    //List<Notification> notifications = (ArrayList) session.getAttribute("notifications");

                    String email = (String) session.getAttribute("email");
                    User user = null; // Khởi tạo user là null
                    List<Notification> notifications = new ArrayList<>();
                    if (email != null) {
                        UserDAO userDAO = new UserDAO();
                        user = userDAO.getUserByEmail(email);
                        if (user != null) {
                            NotificationDAO notiDAO = new NotificationDAO();
                            notifications = notiDAO.getNotificationsByUserId(user.getUserId());
                            if (notifications == null) {
                                notifications = new ArrayList<>();
                            }
                        }
                    }
                %>
                <div class="header-controls">
                    <% if (user != null) {

                    %>
                    <div class="notification-bell" id="notificationBell">
                        <i class="fas fa-bell"></i> <span class="notification-count" id="notificationCount"><%= notifications.size()%></span>
                        <a href="NotificationServlet"></a>
                    </div>
                    <% }%>

                    <div class="header-avatar" style="position: relative;">
                        <img src="<%= (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) ? user.getAvatarUrl() : "img/default-avatar.png"%>"
                             onclick="toggleAvatarMenu()"
                             style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%; border: 2px solid white; cursor: pointer;">
                        <div id="dropdownMenu" style="display: none; position: absolute; right: 0; top: 50px; background-color: white; border: 1px solid #ccc; border-radius: 5px; min-width: 160px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); font-family: sans-serif;">
                            <% if (user != null) { %>
                            <a href="profile.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">👤 Profile</a>
                            <a href="confirmOldPass.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">🔒 Change Password</a>
                            <a href="LogOutServlet" style="display: block; padding: 10px; text-decoration: none; color: #333;">🚪 Logout</a>
                            <% } else { %>
                            <a href="login.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">🔑 Login</a>
                            <a href="register.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">📝 Register</a>
                            <% } %>
                        </div>
                    </div>
                </div>

                <% if (user != null) {

                %>
                <div class="notification-box" id="notificationBox">

                    <div class="notification-header">
                        Notifications
                    </div>
                    <ul class="notification-list" id="notificationList">
                        <% if (notifications.isEmpty()) { %>
                        <li class="no-notifications">Bạn không có thông báo mới.</li>
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
                <% }%>
            </div>
        </header>
        <section class="hero-section set-bg" data-setbg="img/hero-bg.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="hero-text">
                            <span>FITNESS ELEMENTS</span>
                            <h1>BMI CALCULATOR</h1>
                            <p>Gympro comes packed with the user-friendly BMI Calculator<br /> shortcode which lets</p>
                            <a href="#" class="primary-btn">Read More</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="about-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="about-pic">
                            <img src="img/about-pic.jpg" alt="">
                            <a href="https://www.youtube.com/watch?v=SlPhMPnQ58k" class="play-btn video-popup">
                                <img src="img/play.png" alt="">
                            </a>
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="about-text">
                            <h2>Story About Us</h2>
                            <p class="first-para">Lorem ipsum proin gravida nibh vel velit auctor aliquet. Aenean pretium
                                sollicitudin, nascetur auci elit consequat ipsutissem niuis sed odio sit amet nibh vulputate
                                cursus a amet.</p>
                            <p class="second-para">Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, gravida
                                quam semper libero sit amet. Etiam rhoncus. Maecenas tempus, tellus eget condimentum
                                rhoncus, gravida quam semper libero sit amet.</p>
                            <a href="#" class="primary-btn">Read More</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="services-section">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="services-pic">
                            <img src="img/services/service-pic.jpg" alt="">
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="service-items">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="services-item bg-gray">
                                        <img src="img/services/service-icon-1.png" alt="">
                                        <h4>Strategies</h4>
                                        <p>Aenean massa. Cum sociis Theme et natoque penatibus et magnis dis part urient
                                            montes.</p>
                                    </div>
                                    <div class="services-item bg-gray pd-b">
                                        <img src="img/services/service-icon-3.png" alt="">
                                        <h4>Workout</h4>
                                        <p>Aenean massa. Cum sociis Theme et natoque penatibus et magnis dis part urient
                                            montes.</p>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="services-item">
                                        <img src="img/services/service-icon-2.png" alt="">
                                        <h4>Yoga</h4>
                                        <p>Aenean massa. Cum sociis Theme et natoque penatibus et magnis dis part urient
                                            montes.</p>
                                    </div>
                                    <div class="services-item pd-b">
                                        <img src="img/services/service-icon-4.png" alt="">
                                        <h4>Weight Loss</h4>
                                        <p>Aenean massa. Cum sociis Theme et natoque penatibus et magnis dis part urient
                                            montes.</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="classes-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>UNLIMITED CLASSES</h2>
                        </div>
                    </div>
                </div>
                <div class="row classes-slider owl-carousel">
                    <div class="col-lg-4">
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-1.jpg">
                            <div class="si-text">
                                <h4>Yoga</h4>
                                <span><i class="fa fa-user"></i> Ryan Knight</span>
                            </div>
                        </div>
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-4.jpg">
                            <div class="si-text">
                                <h4>Karate</h4>
                                <span><i class="fa fa-user"></i> Kevin McCormick</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-2.jpg">
                            <div class="si-text">
                                <h4>Running</h4>
                                <span><i class="fa fa-user"></i> Randy Rivera</span>
                            </div>
                        </div>
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-5.jpg">
                            <div class="si-text">
                                <h4>Dance</h4>
                                <span><i class="fa fa-user"></i> Russell Lane</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-3.jpg">
                            <div class="si-text">
                                <h4>Personal Training</h4>
                                <span><i class="fa fa-user"></i> Cole Robertson</span>
                            </div>
                        </div>
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-6.jpg">
                            <div class="si-text">
                                <h4>Weight Loss</h4>
                                <span><i class="fa fa-user"></i> Ryan Scott</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-7.jpg">
                            <div class="si-text">
                                <h4>Personal Training</h4>
                                <span><i class="fa fa-user"></i> Cole Robertson</span>
                            </div>
                        </div>
                        <div class="single-class-item set-bg" data-setbg="img/classes/classes-8.jpg">
                            <div class="si-text">
                                <h4>Weight Loss</h4>
                                <span><i class="fa fa-user"></i> Ryan Scott</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="testimonial-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>success stories</h2>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-10 offset-lg-1">
                        <div class="testimonial-slider owl-carousel">
                            <div class="testimonial-item">
                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor
                                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
                                    exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. </p>
                                <div class="ti-pic">
                                    <img src="img/testimonial/testimonial-1.jpg" alt="">
                                    <div class="quote">
                                        <img src="img/testimonial/quote-left.png" alt="">
                                    </div>
                                </div>
                                <div class="ti-author">
                                    <h4>Patrick Cortez</h4>
                                    <span>Leader</span>
                                </div>
                            </div>
                            <div class="testimonial-item">
                                <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor
                                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
                                    exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. </p>
                                <div class="ti-pic">
                                    <img src="img/testimonial/testimonial-1.jpg" alt="">
                                    <div class="quote">
                                        <img src="img/testimonial/quote-left.png" alt="">
                                    </div>
                                </div>
                                <div class="ti-author">
                                    <h4>Patrick Cortez</h4>
                                    <span>Leader</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <section class="banner-section set-bg" data-setbg="img/banner-bg.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="banner-text">
                            <h2>Get training today</h2>
                            <p>Gimply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
                                industry’s standard.</p>
                            <a href="#" class="primary-btn banner-btn">Contact Now</a>
                        </div>
                    </div>
                    <div class="col-lg-5">
                        <img src="img/banner-person.png" alt="">
                    </div>
                </div>
            </div>
        </section>
        <section class="membership-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>MEMBERSHIP PLANS</h2>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4">
                        <div class="membership-item">
                            <div class="mi-title">
                                <h4>Basic</h4>
                                <div class="triangle"></div>
                            </div>
                            <h2 class="mi-price">$17<span>/01 mo</span></h2>
                            <ul>
                                <li>
                                    <p>Duration</p>
                                    <span>12 months</span>
                                </li>
                                <li>
                                    <p>Personal trainer</p>
                                    <span>00 person</span>
                                </li>
                                <li>
                                    <p>Amount of people</p>
                                    <span>01 person</span>
                                </li>
                                <li>
                                    <p>Number of visits</p>
                                    <span>Unlimited</span>
                                </li>
                            </ul>
                            <a href="#" class="primary-btn membership-btn">Start Now</a>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="membership-item">
                            <div class="mi-title">
                                <h4>Standard</h4>
                                <div class="triangle"></div>
                            </div>
                            <h2 class="mi-price">$57<span>/01 mo</span></h2>
                            <ul>
                                <li>
                                    <p>Duration</p>
                                    <span>12 months</span>
                                </li>
                                <li>
                                    <p>Personal trainer</p>
                                    <span>01 person</span>
                                </li>
                                <li>
                                    <p>Amount of people</p>
                                    <span>01 person</span>
                                </li>
                                <li>
                                    <p>Number of visits</p>
                                    <span>Unlimited</span>
                                </li>
                            </ul>
                            <a href="#" class="primary-btn membership-btn">Start Now</a>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="membership-item">
                            <div class="mi-title">
                                <h4>Premium</h4>
                                <div class="triangle"></div>
                            </div>
                            <h2 class="mi-price">$98<span>/01 mo</span></h2>
                            <ul>
                                <li>
                                    <p>Duration</p>
                                    <span>12 months</span>
                                </li>
                                <li>
                                    <p>Personal trainer</p>
                                    <span>01 person</span>
                                </li>
                                <li>
                                    <p>Amount of people</p>
                                    <span>01 person</span>
                                </li>
                                <li>
                                    <p>Number of visits</p>
                                    <span>Unlimited</span>
                                </li>
                            </ul>
                            <a href="#" class="primary-btn membership-btn">Start Now</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <footer class="footer-section">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <div class="contact-option">
                            <span>Phone</span>
                            <p>(123) 118 9999 - (123) 118 9999</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="contact-option">
                            <span>Address</span>
                            <p>72 Kangnam, 45 Opal Point Suite 391</p>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="contact-option">
                            <span>Email</span>
                            <p>contactcompany@Gutim.com</p>
                        </div>
                    </div>
                </div>
                <div class="copyright-text">
                    <ul>
                        <li><a href="#">Term&Use</a></li>
                        <li><a href="#">Privacy Policy</a></li>
                    </ul>
                    <p>©<p> Copyright ©
                    <div class="footer-social">
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-twitter"></i></a>
                        <a href="#"><i class="fa fa-instagram"></i></a>
                        <a href="#"><i class="fa fa-dribbble"></i></a>
                    </div>
                </div>
            </div>
        </footer>
        <script>
            // JavaScript cho Dropdown Menu của Avatar
            function toggleAvatarMenu() {
                const avatarMenu = document.getElementById("dropdownMenu");
                const notificationBox = document.getElementById("notificationBox");
                if (avatarMenu) { // Kiểm tra menu có tồn tại không
                    avatarMenu.style.display = (avatarMenu.style.display === "block") ? "none" : "block";
                    // Đảm bảo hộp thông báo đóng khi mở menu avatar
                    if (notificationBox) { // Kiểm tra hộp thông báo có tồn tại không
                        notificationBox.style.display = 'none';
                    }
                }
            }

            // JavaScript cho Nút chuông thông báo
            const notificationBell = document.getElementById('notificationBell');
            const notificationBox = document.getElementById('notificationBox');
            const notificationCount = document.getElementById('notificationCount');
            const notificationList = document.getElementById('notificationList');

            if (notificationBell && notificationBox) { // Đảm bảo các phần tử tồn tại trước khi thêm listener
                notificationBell.addEventListener('click', function (event) {
                    event.stopPropagation(); // Ngăn chặn sự kiện click lan ra ngoài
                    if (notificationBox.style.display === 'block') {
                        notificationBox.style.display = 'none';
                    } else {
                        notificationBox.style.display = 'block';
                        // Đóng menu avatar nếu nó đang mở
                        const avatarMenu = document.getElementById("dropdownMenu");
                        if (avatarMenu) {
                            avatarMenu.style.display = 'none';
                        }
                        // Khi mở hộp thông báo, có thể reset số lượng thông báo về 0
                        if (notificationCount) {
                            notificationCount.style.display = 'none';
                            notificationCount.textContent = '0'; // Cập nhật số lượng hiển thị
                        }
                    }
                });
            }

            // Đóng cả hộp thông báo và menu avatar khi click ra ngoài
            window.addEventListener("click", function (e) {
                const avatarMenu = document.getElementById("dropdownMenu");
                const avatar = document.querySelector(".header-avatar img");

                // Đóng menu avatar
                if (avatarMenu && !avatarMenu.contains(e.target) && (!avatar || !avatar.contains(e.target))) {
                    avatarMenu.style.display = "none";
                }

                // Đóng hộp thông báo
                if (notificationBox && notificationBell && !notificationBox.contains(e.target) && !notificationBell.contains(e.target)) {
                    notificationBox.style.display = 'none';
                }
            });

            // Cập nhật hiển thị badge thông báo khi tải trang
            document.addEventListener('DOMContentLoaded', function () {
                if (notificationCount) {
                    const initialCount = parseInt(notificationCount.textContent);
                    if (initialCount > 0) {
                        notificationCount.style.display = 'flex'; // Hiển thị nếu có thông báo
                    } else {
                        notificationCount.style.display = 'none'; // Ẩn nếu không có
                    }
                }
            });

        </script>

        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
    </body>

</html>