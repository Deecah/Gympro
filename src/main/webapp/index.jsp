<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Gympro Template">
        <meta name="keywords" content="Gympro, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Gympro</title>


        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

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

        <jsp:include page="header.jsp" />


        <!-- Hero Section Begin -->
        <section class="hero-section set-bg" data-setbg="img/hero-bg.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="hero-text">
                            <span>FITNESS ELEMENTS</span>
                            <h1>BMI CALCULATOR</h1>
                            <p>Gympro comes packed with the user-friendly BMI Calculator<br /> shortcode which lets</p>

                            <!-- Nút bấm xổ form -->
                            <a href="#" class="primary-btn" id="toggleBmiForm">Read More</a>

                            <!-- Form BMI ẩn mặc định -->
                            <div class="bmi-form-container" id="bmiForm" style="display: none; margin-top: 20px; background: rgba(255,255,255,0.05); padding: 20px; border-radius: 10px;">
                                <label for="height" style="color:white;">Height (cm):</label>
                                <input type="number" id="height" placeholder="Example: 170" min="50" max="250" class="form-control" />

                                <label for="weight" style="color:white; margin-top: 10px;">Weight (kg):</label>
                                <input type="number" id="weight" placeholder="Example: 65" min="30" max="300" class="form-control" />

                                <button onclick="calculateBMI()" class="btn btn-warning mt-3">Calculate BMI</button>
                                <p id="bmiResult" style="margin-top:10px; font-weight: bold; color: white;"></p>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Hero Section End -->

        <!-- About Section Begin -->
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
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- About Section End -->
        <!-- Content Section Begin -->
        <section class="content-section spad">
            <div class="container">
                <jsp:include page="contentList.jsp" />
            </div>
        </section>
        <!-- Content Section End -->

        <!-- Services Section Begin -->
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
        <!-- Services Section End -->

        <!-- Classes Section Begin -->
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
        <!-- Classes Section End -->

        <!-- Trainer Section Begin -->
        <section class="trainer-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>EXPERT TRAINERS</h2>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-md-6">
                        <div class="single-trainer-item">
                            <img src="img/trainer/trainer-1.jpg" alt="">
                            <div class="trainer-text">
                                <h5>Patrick Cortez</h5>
                                <span>Leader</span>
                                <p>non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat
                                    voluptatem.</p>
                                <div class="trainer-social">
                                    <a href="#"><i class="fa fa-facebook"></i></a>
                                    <a href="#"><i class="fa fa-instagram"></i></a>
                                    <a href="#"><i class="fa fa-twitter"></i></a>
                                    <a href="#"><i class="fa fa-pinterest"></i></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="single-trainer-item">
                            <img src="img/trainer/trainer-2.jpg" alt="">
                            <div class="trainer-text">
                                <h5>Gregory Powers</h5>
                                <span>Gym coach</span>
                                <p>non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat
                                    voluptatem.</p>
                                <div class="trainer-social">
                                    <a href="#"><i class="fa fa-facebook"></i></a>
                                    <a href="#"><i class="fa fa-instagram"></i></a>
                                    <a href="#"><i class="fa fa-twitter"></i></a>
                                    <a href="#"><i class="fa fa-pinterest"></i></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="single-trainer-item">
                            <img src="img/trainer/trainer-3.jpg" alt="">
                            <div class="trainer-text">
                                <h5>Walter Wagner</h5>
                                <span>Dance trainer</span>
                                <p>non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat
                                    voluptatem.</p>
                                <div class="trainer-social">
                                    <a href="#"><i class="fa fa-facebook"></i></a>
                                    <a href="#"><i class="fa fa-instagram"></i></a>
                                    <a href="#"><i class="fa fa-twitter"></i></a>
                                    <a href="#"><i class="fa fa-pinterest"></i></a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Trainer Section End -->

        <!-- Testimonial Section Begin -->
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
        <!-- Testimonial Section End -->

        <!-- Banner Section Begin -->
        <section class="banner-section set-bg" data-setbg="img/banner-bg.jpg">
            <div class="container">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="banner-text">
                            <h2>Get training today</h2>
                            <p>Gimply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
                                industry’s standard.</p>
                            <a href="contact.jsp" class="primary-btn banner-btn">Contact Now</a>
                        </div>
                    </div>
                    <div class="col-lg-5">
                        <img src="img/banner-person.png" alt="">
                    </div>
                </div>
            </div>
        </section>
        <!-- Banner Section End -->

        <!-- Latest Blog Section Begin -->
        <section class="latest-blog-section spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>Latest Blog</h2>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-md-6">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-1.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Gym</a>
                            </div>
                            <h4><a href="#">10 States At Risk of Rural Hospital Closures</a></h4>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-2.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Sport</a>
                            </div>
                            <h4><a href="#">Diver who helped save Thai soccer team</a></h4>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-6">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-3.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Body</a>
                            </div>
                            <h4><a href="#">Man gets life in prison for stabbing</a></h4>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Latest Blog Section End -->

        <jsp:include page="notificationPopup.jsp"/>

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
      <script>
    const toggleBtn = document.getElementById("toggleBmiForm");
    const bmiForm = document.getElementById("bmiForm");

    toggleBtn.addEventListener("click", function (e) {
        e.preventDefault();
        bmiForm.style.display = (bmiForm.style.display === "none" || bmiForm.style.display === "") ? "block" : "none";
    });

   function calculateBMI() {
    const heightCm = parseFloat(document.getElementById("height").value);
    const weightKg = parseFloat(document.getElementById("weight").value);
    const resultEl = document.getElementById("bmiResult");

    if (!isNaN(heightCm) && !isNaN(weightKg) && heightCm >= 50 && heightCm <= 250 && weightKg >= 30 && weightKg <= 300) {
        const heightM = heightCm / 100;
        const bmi = (weightKg / (heightM * heightM)).toFixed(1);

        let category = "";
        let suggestion = "";

        if (bmi < 18.5) {
            category = "Underweight";
            suggestion = " Consider checking our <a href='supplement.jsp' style='color: yellow;'>supplements</a> or <a href='CustomerPackageServlet' style='color: yellow;'>hire a personal trainer</a>.";
        } else if (bmi < 24.9) {
            category = "Normal";
            suggestion = " Maintain your shape with our <a href='CustomerPackageServlet' style='color: yellow;'>coaching programs</a> or <a href='supplement.jsp' style='color: yellow;'>nutrition support</a>.";
        } else if (bmi < 29.9) {
            category = "Overweight";
            suggestion = " We recommend joining our <a href='CustomerPackageServlet' style='color: yellow;'>weight loss courses</a> and try <a href='supplement.jsp' style='color: yellow;'>fat-burning supplements</a>.";
        } else {
            category = "Obese";
            suggestion = " Urgent help needed! Join a <a href='CustomerPackageServlet' style='color: yellow;'>personalized training program</a> today.";
        }

        resultEl.innerHTML = "Your BMI is <strong>" + bmi + "</strong> (" + category + ").<br/>" + suggestion;
        resultEl.style.color = "#fff";
    } else {
        resultEl.textContent = "Please enter valid height (50–250 cm) and weight (30–300 kg)!!!";
        resultEl.style.color = "red";
    }
}

</script>
        <jsp:include page="footer.jsp" />
    </body>
</html>