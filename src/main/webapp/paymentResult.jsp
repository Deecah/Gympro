<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Transaction Result</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <style>
            body {
                background-color: #f4f4f4;
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 20px;
                text-align: center;
            }

            .status-container {
                margin-top: 50px;
            }

            .status-image {
                width: 120px;
                height: 120px;
                margin-bottom: 20px;
            }

            .status-message {
                font-weight: bold;
                font-size: 20px;
                margin-bottom: 15px;
            }

            .success {
                color: #28a745;
            }
            .failed {
                color: #dc3545;
            }
            .processing {
                color: #ffc107;
            }

            .home-button {
                display: inline-block;
                padding: 12px 24px;
                margin-top: 20px;
                background-color: #007bff;
                color: white;
                font-size: 16px;
                font-weight: bold;
                text-decoration: none;
                border-radius: 8px;
                transition: background-color 0.3s ease, transform 0.2s ease;
                box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
            }

            .home-button:hover {
                background-color: #0056b3;
                transform: translateY(-3px);
                box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.15);
            }

            .home-button:active {
                transform: translateY(1px);
            }
        </style>
    </head>

    <body>

        <section class="status-container">
            <div>
                <img class="status-image"
                     src="https://cdn2.cellphones.com.vn/insecure/rs:fill:150:0/q:90/plain/https://cellphones.com.vn/media/wysiwyg/Review-empty.png"
                     alt="Transaction Status">
            </div>

            <!-- Successful Transaction -->
            <c:if test="${transResult}">
                <div>
                    <h3 class="status-message success">
                        Your transaction was successful! 
                        <i class="fas fa-check-circle"></i>
                    </h3>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="home-button">
                        🏠 Back to Home Page
                    </a>
                </div>
            </c:if>

            <!-- Failed Transaction -->
            <c:if test="${transResult == false}">
                <div>
                    <h3 class="status-message failed">
                        Your transaction has failed!
                    </h3>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="home-button">
                        🏠 Back to Home Page
                    </a>
                </div>
            </c:if>

            <!-- Processing Transaction -->
            <c:if test="${transResult == null}">
                <div>
                    <h3 class="status-message processing">
                        We have received your order, please wait while it's being processed.
                    </h3>
                    <a href="${pageContext.request.contextPath}/index.jsp" class="home-button">
                        🏠 Back to Home Page
                    </a>
                </div>
            </c:if>
        </section>

    </body>
</html>
