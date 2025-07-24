<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="zxx">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Gutim Template">
        <meta name="keywords" content="Gutim, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Gutim | Template</title>

        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap"
              rel="stylesheet">

        <!-- Css Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">

    </head>

    <body>
        <!-- Page Preloder -->
        <div id="preloder">
            <div class="loader"></div>
        </div>

        <!-- Header Section Begin -->
        <jsp:include page="header.jsp" />
        <!-- Header End -->

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb/classes-breadcrumb.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="breadcrumb-text">
                            <h2>About</h2>
                            <div class="breadcrumb-option">
                                <a href="./index.html"><i class="fa fa-home"></i> Home</a>
                                <span>About</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Breadcrumb Section End -->

        <!-- About Section Begin -->
        <section class="about-section about-page spad">
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
                                quam semper libero sit amet.</p>
                            <img src="img/about-signature.png" alt="">
                            <div class="at-author">
                                <h4>Lettie Chavez</h4>
                                <span>CEO - Founder</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- About Section End -->

        <!-- About Counter Section Begin -->
        <div class="about-counter">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="about-counter-text">
                            <div class="single-counter">
                                <h1 class="counter-num count">98</h1>
                                <p>Programs</p>
                            </div>
                            <div class="single-counter">
                                <h1 class="counter-num count">14</h1>
                                <p>Locations</p>
                            </div>
                            <div class="single-counter">
                                <h1 class="counter-num count">50</h1>
                                <span>k+</span>
                                <p>Members</p>
                            </div>
                            <div class="single-counter">
                                <h1 class="counter-num count">34</h1>
                                <p>Coaches</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- About Counter Section End -->

        <!-- Gym Award Section Begin -->
        <section class="gym-award spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="award-text">
                            <h2>Best gym award</h2>
                            <p>Lorem ipsum proin gravida nibh vel velit auctor aliquet. Aenean pretium sollicitudin,
                                nascetur auci elit consequat ipsutissem niuis sed odio sit amet nibh vulputate cursus a
                                amet.</p>
                            <p>Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, gravida quam semper libero
                                sit amet. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, gravida quam
                                semper libero sit amet.</p>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <img src="img/award.jpg" alt="">
                    </div>
                </div>
            </div>
        </section>
        <!-- Gym Award Section End -->

        <!-- Banner Section Begin -->
        <section class="banner-section set-bg" data-setbg="img/banner-bg.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="banner-text">
                            <h2>Get training today</h2>
                            <p>Gimply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
                                industry’s standard.</p>
                            <a href="CustomerPackageServlet" class="primary-btn banner-btn">Contact Now</a>
                        </div>
                    </div>
                    <div class="col-lg-5">
                        <img src="img/banner-person.png" alt="">
                    </div>
                </div>
            </div>
        </section>
        <!-- Banner Section End -->

        <!-- Trainer Section Begin -->
      <section class="trainer-section about-trainer spad">
    <div class="container">
        <!-- Title -->
        <div class="row">
            <div class="col-lg-12">
                <div class="section-title">
                    <h2>EXPERT TRAINERS</h2>
                </div>
            </div>
        </div>

        <!-- Grid trainers -->
        <div class="row">
            <c:forEach var="trainer" items="${trainers}" varStatus="loop">
                <div class="col-lg-3 col-md-6 mb-4">
                    <div class="single-trainer-item">
                        <img src="${trainer.avatarUrl}" alt="${trainer.userName}"
                             style="width:100%; height:300px; object-fit:cover;">
                        <div class="trainer-text">
                            <h5>${trainer.userName}</h5>
                            <span>${trainer.specialization}</span>
                        </div>
                    </div>
                </div>
                <c:if test="${loop.index % 4 == 3}">
                    </div><div class="row">
                </c:if>
            </c:forEach>
        </div>

      <!-- Pagination -->
<div class="row justify-content-center mt-4">
    <div class="col-auto">
        <ul class="pagination justify-content-center">
            <c:set var="currentPage" value="${currentPage}" />
            <c:set var="totalPages" value="${totalPages}" />

            <%-- Nút Previous --%>
            <c:if test="${currentPage > 1}">
                <li class="page-item">
                    <a class="page-link" href="ExpertTrainerServlet?page=${currentPage - 1}">&laquo;</a>
                </li>
            </c:if>

            <%-- Hiện trang 1 nếu currentPage > 3 --%>
            <c:if test="${currentPage > 3}">
                <li class="page-item"><a class="page-link" href="ExpertTrainerServlet?page=1">1</a></li>
            </c:if>

            <%-- Dấu ... nếu cách xa đầu --%>
            <c:if test="${currentPage > 4}">
                <li class="page-item disabled"><a class="page-link">...</a></li>
            </c:if>

            <%-- Hiện 2 trang trước, trang hiện tại, 2 trang sau --%>
            <c:forEach var="i" begin="${currentPage - 2}" end="${currentPage + 2}">
                <c:if test="${i >= 1 && i <= totalPages}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="ExpertTrainerServlet?page=${i}">${i}</a>
                    </li>
                </c:if>
            </c:forEach>

            <%-- Dấu ... nếu cách xa cuối --%>
            <c:if test="${currentPage < totalPages - 3}">
                <li class="page-item disabled"><a class="page-link">...</a></li>
            </c:if>

            <%-- Trang cuối nếu currentPage đủ xa --%>
            <c:if test="${currentPage < totalPages - 2}">
                <li class="page-item"><a class="page-link" href="ExpertTrainerServlet?page=${totalPages}">${totalPages}</a></li>
            </c:if>

            <%-- Nút Next --%>
            <c:if test="${currentPage < totalPages}">
                <li class="page-item">
                    <a class="page-link" href="ExpertTrainerServlet?page=${currentPage + 1}">&raquo;</a>
                </li>
            </c:if>
        </ul>
    </div>
</div>

    </div>
</section>


        <!-- Trainer Section End -->

        <!-- Footer Section Begin -->
        <jsp:include page="footer.jsp" />
        <!-- Footer Section End -->

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
    </body>

</html>