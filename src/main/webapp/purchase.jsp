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
        <title>Select Payment Method - GymPro</title>
        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap"
              rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Css Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/chat.css" type="text/css">
        
        <script>
            // Set current user ID for notification.js
            <% if (session.getAttribute("user") != null) { %>
            var currentUserId = <%= ((model.User)session.getAttribute("user")).getUserId() %>;
            <% } else { %>
            var currentUserId = null;
            <% } %>
        </script>
        <script src="js/notification.js"></script>
        
        <style>
    body {
         background: #ffffff !important;
    }

    .payment-method-btn {
        width: 150px;
        height: 50px;
        font-weight: bold;
        border-radius: 8px;
        transition: 0.3s;
    }

    .payment-method-btn:hover {
        transform: scale(1.05);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
    }

    .card-img-top {
        max-height: 250px;
        object-fit: cover;
    }

    .checkout-section {
        background-color: #ffffff !important;
    }

    .spad {
        background-color: #ffffff !important;
    }
        </style>

    </head>
    <body>
        <jsp:include page="header.jsp"/>

        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb/classes-breadcrumb.jpg">
            <div class="container">
                <div class="breadcrumb-text">
                    <h2>Purchase Package</h2>
                    <div class="breadcrumb-option">
                        <a href="index.jsp"><i class="fa fa-home"></i> Home</a>
                        <span>Checkout</span>
                    </div>
                </div>
            </div>
        </section>
        <section class="checkout-section spad" style="background: #ffffff; min-height: 100vh;">
            <div class="container d-flex justify-content-center align-items-center" style="min-height: 80vh;">
                <div class="card shadow-lg p-4" style="width: 100%; max-width: 700px; border-radius: 20px; background-color: white;">
                    <c:if test="${not empty pkg}">
                        <div class="text-center">
                            <img src="${empty pkg.imageUrl ? 'images/default.jpg' : pkg.imageUrl}" alt="${pkg.name}"
                                 class="img-fluid rounded" style="max-height: 250px; object-fit: cover;" />
                        </div>

                        <div class="mt-3 text-center">
                            <h3 class="fw-bold">${pkg.name}</h3>
                            <p class="text-muted">${pkg.description}</p>
                            <hr />
                            <p class="mb-1"><strong>Price:</strong> <span class="text-primary fs-5">${pkg.price} VNĐ</span></p>
                            <p class="mb-3"><strong>Duration:</strong> <span class="text-success">${pkg.duration} days</span></p>
                        </div>

                        <h5 class="text-center mt-4 mb-3">Select a payment method</h5>

                        <form method="post" action="${pageContext.request.contextPath}/SelectPaymentMethodServlet" class="text-center">
                            <input type="hidden" name="packageId" value="${pkg.packageID}">
                            <input type="hidden" name="trainerId" value="${pkg.trainerID}">
                            <input type="hidden" name="amount" value="${pkg.price}">
                            <input type="hidden" name="duration" value="${pkg.duration}">

                            <div class="d-flex justify-content-around mt-3">
                                <button name="method" value="vnpay" type="submit" class="btn btn-outline-primary payment-method-btn">
                                    <img src="img/payment/logo-vnpay.png" alt="VNPay" style="height: 24px; vertical-align: middle;"> 
                                </button>

                                <button name="method" value="paypal" type="submit" class="btn btn-outline-success payment-method-btn">
                                    <img src="img/payment/logo-paypal.png" alt="PayPal" style="height: 24px; vertical-align: middle;"> 
                                </button>

                                <button name="method" value="payos" type="submit" class="btn btn-outline-warning payment-method-btn">
                                    <img src="img/payment/logo-payos.png" alt="PayOS" style="height: 24px; vertical-align: middle;"> 
                                </button>
                            </div>
                        </form>
                    </c:if>

                    <c:if test="${empty pkg}">
                        <div class="alert alert-danger text-center">Package not found.</div>
                    </c:if>
                </div>
            </div>
        </section>


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

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/masonry.pkgd.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>

        <jsp:include page="footer.jsp" />
    </body>
</html>
