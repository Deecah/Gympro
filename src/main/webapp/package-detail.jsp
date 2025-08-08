<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
        <style>
        .card {
            border: none;
            border-radius: 18px;
            box-shadow: 0 4px 24px rgba(60, 72, 88, 0.08);
            transition: box-shadow 0.2s, transform 0.2s;
            background: #f4fff7; /* Light green background */
            margin-bottom: 1.5rem;
        }

        .card:hover {
            box-shadow: 0 8px 32px rgba(60, 72, 88, 0.16);
            transform: translateY(-4px) scale(1.01);
        }

        .card-body {
            padding: 1.5rem 2rem;
        }

        .card-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #2e7d32;
            margin-bottom: 0.5rem;
        }

        .card-text {
            color: #555;
            font-size: 1rem;
        }
          .serial-circle {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          width: 36px;
          height: 36px;
          border-radius: 50%;
          background: #e8f5e9;
          border: 2px solid #43a047;
          color: #388e3c;
          font-weight: bold;
          font-size: 1.1rem;
          margin-right: 1.25rem;
        }

        /* Modal custom styles for select program */
        #selectProgramModal .modal-content {
            border-radius: 18px;
            box-shadow: 0 8px 32px rgba(60, 72, 88, 0.16);
            background: #f8fff8;
            border: none;
        }
        #selectProgramModal .modal-header {
            border-bottom: none;
            background: #e8f5e9;
            border-radius: 18px 18px 0 0;
        }
        #selectProgramModal .modal-title {
            color: #2e7d32;
            font-weight: 700;
        }
        #selectProgramModal .btn-close {
            background: none;
            border: none;
        }
        #selectProgramModal .modal-body {
            padding-top: 0.5rem;
            padding-bottom: 0.5rem;
        }
        #selectProgramModal .list-group {
            padding: 0;
        }
        #selectProgramModal .select-program-card {
            border: none;
            border-radius: 12px;
            background: #e8f5e9;
            margin-bottom: 12px;
            transition: box-shadow 0.2s, background 0.2s;
            box-shadow: 0 2px 8px rgba(60, 72, 88, 0.08);
            cursor: pointer;
            display: flex;
            align-items: center;
            padding: 1rem 1.25rem;
        }
        #selectProgramModal .select-program-card.selected,
        #selectProgramModal .select-program-card:hover {
            background: #c8e6c9;
            box-shadow: 0 4px 16px rgba(60, 72, 88, 0.16);
        }
        #selectProgramModal .form-check-input {
            margin-right: 1rem;
            accent-color: #43a047;
        }
        #selectProgramModal .form-check-input:checked {
            background-color: #43a047;
            border-color: #43a047;
        }
        #selectProgramModal .serial-circle {
            background: #b9f6ca;
            border: 2px solid #43a047;
            color: #388e3c;
            font-weight: bold;
            font-size: 1.1rem;
            margin-right: 1.25rem;
        }
        #selectProgramModal .modal-footer {
            border-top: none;
            background: #e8f5e9;
            border-radius: 0 0 18px 18px;
        }
        #selectProgramModal .btn-primary {
            background: #43a047;
            border: none;
            font-weight: 600;
        }
        #selectProgramModal .btn-primary:hover {
            background: #388e3c;
        }
        #selectProgramModal .btn-secondary {
            background: #bdbdbd;
            border: none;
            color: #333;
        }
        #selectProgramModal .btn-secondary:hover {
            background: #9e9e9e;
        }
        </style>

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
                            <p><i class="fa fa-usd text-success me-2"></i><span class="info-label">  Price:</span> ${pkg.price} VNĐ</p>
                            <div class="text-end mt-4">
                                <button class="btn btn-success purchase-btn" id="purchaseBtn" onclick="openSelectProgramModal()">Purchase</button>
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
                            <img src="${trainer.avatarUrl}"
                                      class="shadow rounded-circle"
                                      style="width: 150px; height: 150px; object-fit: cover;"
                                      alt="Trainer Avatar"
                                      onerror="this.onerror=null;this.src='img/avatar/avatar1.jpg';">
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

                <h4 class="section-heading">Programs</h4>
                    <div class="section-box">
                        <c:forEach var="program" items="${programs}" varStatus="status">
                            <div class="card mb-3">
                                <div class="card-body d-flex align-items-start">
                                <span class="serial-circle me-3">${status.index + 1}</span>
                                    <div>
                                        <h5 class="card-title mb-1">${program.name}</h5>
                                        <p class="card-text mb-0">${program.description}</p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                <hr>

                <!-- 3. Feedback -->
                <h4 class="section-heading">Customer Feedback</h4>

                <c:if test="${empty feedbacks}">
                    <p>No feedback yet for this package.</p>
                </c:if>

                <div id="feedback-container">
                    <c:forEach var="fb" items="${feedbacks}" varStatus="status">
                        <div class="feedback-box feedback-item"
                             style="margin-bottom: 20px; display: flex; align-items: flex-start; gap: 15px; <c:if test='${status.index >= 5}'>display: none;</c:if>'">
                                 <img src="${fb.userAvatar}" alt="User Avatar" style="width: 50px; height: 50px; border-radius: 50%;">
                             <div>
                                 <strong>${fb.userName}</strong><br>
                                 <span class="text-warning" style="color: #ffc107;">
                                     <c:forEach var="i" begin="1" end="${fb.star}">★</c:forEach>
                                     <c:forEach var="i" begin="${fb.star + 1}" end="5">☆</c:forEach>
                                     </span>
                                 <c:if test="${not empty fb.feedbackContent}">
                                     <p style="margin-top: 5px;">${fb.feedbackContent}</p>
                                 </c:if>
                             </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${fn:length(feedbacks) > 5}">
                    <div class="text-center">
                        <button id="showMoreBtn" class="btn btn-outline-primary">Show More</button>
                    </div>
                </c:if>

                <script>
                    let feedbackIndex = 5;
                    const allFeedbacks = document.querySelectorAll('#feedback-container .feedback-box');
                    const showMoreBtn = document.getElementById('showMoreBtn');

                    if (showMoreBtn) {
                        showMoreBtn.addEventListener('click', () => {
                            let count = 0;
                            for (let i = feedbackIndex; i < allFeedbacks.length && count < 10; i++, count++) {
                                allFeedbacks[i].style.display = 'flex';
                            }
                            feedbackIndex += count;

                            if (feedbackIndex >= allFeedbacks.length) {
                                showMoreBtn.style.display = 'none';
                            }
                        });
                    }
                </script>
            </div>
        </section>

        <!-- Modal for selecting a program -->
        <div class="modal fade" id="selectProgramModal" tabindex="-1" aria-labelledby="selectProgramModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="selectProgramModalLabel">Select a Program</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <c:if test="${empty programs}">
          <p>No programs available for this package.</p>
        </c:if>
        <c:if test="${not empty programs}">
          <form id="selectProgramForm">
            <div class="list-group">
              <c:forEach var="program" items="${programs}" varStatus="status">
                <label class="select-program-card w-100 mb-2">
                  <input class="form-check-input me-2" type="radio" name="programId" value="${program.programId}" style="margin-top:0.2rem;">
                  <span class="serial-circle me-3">${status.index + 1}</span>
                  <div>
                    <h5 class="card-title mb-1">${program.name}</h5>
                    <p class="card-text mb-0">${program.description}</p>
                  </div>
                </label>
              </c:forEach>
            </div>
          </form>
        </c:if>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-primary" id="confirmPurchaseBtn" onclick="handleConfirmPurchase()">Confirm</button>
      </div>
    </div>
  </div>
</div>

        <!-- Footer -->
        <jsp:include page="footer.jsp" />

        <!-- JS Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/masonry.pkgd.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>

        <script>
          function handleConfirmPurchase() {
            var selected = document.querySelector('input[name="programId"]:checked');
            if (!selected) {
              alert('Please select a program.');
              return;
            }
            // Redirect to purchase page with selected programId and packageId
            var packageId = '${pkg.packageID}';
            var programId = selected.value;
            var trainerId = '${pkg.trainerID}';
            var price = '${pkg.price}';
            window.location.href = '${pageContext.request.contextPath}/purchase?packageId=' + packageId + '&programId=' + programId + '&trainerId=' + trainerId + '&price=' + price;
          }
        </script>
        <script>
          document.addEventListener('DOMContentLoaded', function() {
            var purchaseBtn = document.getElementById('purchaseBtn');
            var selectProgramModal = new bootstrap.Modal(document.getElementById('selectProgramModal'));
            purchaseBtn.addEventListener('click', function(e) {
              e.preventDefault();
              selectProgramModal.show();
            });
          });
        </script>
        <script>
        document.addEventListener('DOMContentLoaded', function() {
          // Highlight selected card on radio change
          document.querySelectorAll('#selectProgramModal input[name="programId"]').forEach(function(radio) {
            radio.addEventListener('change', function() {
              document.querySelectorAll('#selectProgramModal .select-program-card').forEach(function(card) {
                card.classList.remove('selected');
              });
              if (this.checked) {
                this.closest('.select-program-card').classList.add('selected');
              }
            });
          });
        });
        </script>
        <script>
function openSelectProgramModal() {
  var selectProgramModal = new bootstrap.Modal(document.getElementById('selectProgramModal'));
  selectProgramModal.show();
}
</script>

    </body>
</html>
