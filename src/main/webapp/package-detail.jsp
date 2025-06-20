<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Package Detail</title>
        <!-- Google Font & CSS -->
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
        <link rel="stylesheet" href="stylecss/package-detail.css" type="text/css">

    </head>
    <body>

        <!-- Header -->
        <jsp:include page="header.jsp" />

        <!-- Breadcrumb -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb/classes-breadcrumb.jpg">
            <div class="container">
                <div class="breadcrumb-text">
                    <h2>Package Detail</h2>
                    <div class="breadcrumb-option">
                        <a href="index.jsp"><i class="fa fa-home"></i> Home</a>
                        <span>Package Detail</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- Package Detail Section -->
        <section class="blog-section spad">
            <div class="container">
                <!-- 1. Package Info -->
                <div class="section-box">
                    <div class="row align-items-center g-4 ps-md-3 pe-md-3">
                        <div class="col-md-6">
                            <img src="${pkg.imageUrl}" class="package-img" alt="${pkg.name}">
                        </div>
                        <div class="col-md-6 ps-md-4">
                            <h2 class="fw-bold mb-3 text-center">${pkg.name}</h2>
                            <p class="mb-3">${pkg.description}</p>
                            <p><i class="fa fa-clock-o text-success me-2"></i><span class="info-label">  Duration:</span> ${pkg.duration} days</p>
                            <p><i class="fa fa-usd text-success me-2"></i><span class="info-label">  Price:</span> $${pkg.price}</p>
                            <div class="text-end mt-4">
                                <a href="purchase.jsp?packageId=${pkg.packageID}" class="btn btn-success purchase-btn">
                                    <i class="fa fa-shopping-cart me-1"></i> Purchase Package
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <hr>

                <!-- 2. Trainer Info -->
                <h4 class="section-heading">Trainer Information</h4>
                <div class="section-box">
                    <div class="row align-items-center">
                        <div class="col-md-3 text-center mb-3 mb-md-0">
                            <img src="${trainer.avatarUrl}" class="rounded-circle shadow" 
                                 style="width: 150px; height: 150px; object-fit: cover;" alt="Trainer Avatar">
                        </div>
                        <div class="col-md-9">
                            <div class="trainer-info-grid">
                                <p><i class="fa fa-user"></i><strong>Name:</strong> ${trainer.userName}</p>
                                <p><i class="fa fa-venus-mars"></i><strong>Gender:</strong> ${trainer.gender}</p>
                                <p><i class="fa fa-envelope"></i><strong>Email:</strong> ${trainer.email}</p>
                                <p><i class="fa fa-phone"></i><strong>Phone:</strong> ${trainer.phone}</p>
                                <p><i class="fa fa-map-marker"></i><strong>Address:</strong> ${trainer.address}</p>
                                <p><i class="fa fa-briefcase"></i><strong>Years of Experience:</strong> ${trainer.experienceYears} years</p>
                                <p><i class="fa fa-certificate"></i><strong>Specialization:</strong> ${trainer.specialization}</p>
                                <p><i class="fa fa-info-circle"></i><strong>About Trainer:</strong> ${trainer.description}</p>
                            </div>
                        </div>
                    </div>
                </div>


                <hr>

                <!-- 3. Included Programs -->
                <h4 class="section-heading">Included Programs</h4>
                <div class="row mb-4 ps-md-2 pe-md-2">
                    <c:forEach var="prog" items="${programs}">
                        <div class="col-md-6">
                            <div class="card mb-3">
                                <div class="card-body">
                                    <h5 class="card-title">${prog.name}</h5>
                                    <p class="card-text">${prog.description}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <hr>

                <!-- 4. Feedback -->
                <h4 class="section-heading">Customer Feedback</h4>
                <c:forEach var="fb" items="${feedbacks}">
                    <div class="feedback-box">
                        <strong>${fb.userName}</strong>
                        <span class="text-warning">★ ${fb.rating}</span>
                        <p>${fb.comment}</p>
                    </div>
                </c:forEach>
            </div>
        </section>


        <!-- Footer -->
        <jsp:include page="footer.jsp" />

        <!-- Avatar Toggle Script -->
        <script>
            function toggleMenu() {
                const menu = document.getElementById("dropdownMenu");
                menu.style.display = (menu.style.display === "block") ? "none" : "block";
            }
            window.addEventListener("click", function (e) {
                const menu = document.getElementById("dropdownMenu");
                const avatar = document.querySelector(".header-avatar img");
                if (!menu.contains(e.target) && !avatar.contains(e.target)) {
                    menu.style.display = "none";
                }
            });
        </script>

        <!-- JS Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/masonry.pkgd.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>

    </body>
</html>
