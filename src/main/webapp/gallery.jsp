<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
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
                        <h2>Gallery</h2>
                        <div class="breadcrumb-option">
                            <a href="./index.html"><i class="fa fa-home"></i> Home</a>
                            <span>Gallery</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Breadcrumb Section End -->

    <!-- Gallery Section Begin -->
    <div class="gallery-section spad">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <ul class="gallery-controls">
                        <li class="active" data-filter=".all">all gallery</li>
                        <li data-filter=".fitness">fitness</li>
                        <li data-filter=".coaching">coaching</li>
                        <li data-filter=".event">event</li>
                        <li data-filter=".other">other</li>
                    </ul>
                </div>
            </div>
            <div class="row gallery-filter">
                <div class="col-lg-6 mix all fitness">
                    <img src="img/gallery/gallery-1.jpg" alt="">
                </div>
                <div class="col-lg-6">
                    <div class="row">
                        <div class="col-lg-6 mix all fitness coaching">
                            <img src="img/gallery/gallery-2.jpg" alt="">
                        </div>
                        <div class="col-lg-6">
                            <div class="row">
                                <div class="col-lg-12 mix all coaching">
                                    <img src="img/gallery/gallery-3.jpg" alt="">
                                </div>
                                <div class="col-lg-12 mix all coaching">
                                    <img src="img/gallery/gallery-4.jpg" alt="">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-6">
                    <div class="row">
                        <div class="col-lg-6 mix all other">
                            <img src="img/gallery/gallery-5.jpg" alt="">
                        </div>
                        <div class="col-lg-6 mix all other">
                            <img src="img/gallery/gallery-6.jpg" alt="">
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 mix all event">
                    <img src="img/gallery/gallery-7.jpg" alt="">
                </div>
            </div>
        </div>
    </div>
    <!-- Gallery Section End -->

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