<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả giao dịch</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" 
              integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" 
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

            .success { color: #28a745; }
            .failed { color: #dc3545; }
            .processing { color: #ffc107; }

            /* Nút Cart */
            .cart-button {
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

            .cart-button:hover {
                background-color: #0056b3;
                transform: translateY(-3px);
                box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.15);
            }

            .cart-button:active {
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

            <!-- Giao dịch thành công -->
            <c:if test="${transResult}">
                <div>
                    <h3 class="status-message success">
                        Bạn đã giao dịch thành công! 
                        <i class="fas fa-check-circle"></i>
                    </h3>
                    <a href="${pageContext.request.contextPath}/CartServlet?action=list" class="cart-button">
                        🛒 Quay lại giỏ hàng
                    </a>
                </div>
            </c:if>

            <!-- Giao dịch thất bại -->
            <c:if test="${transResult == false}">
                <div>
                    <h3 class="status-message failed">
                        Đơn hàng giao dịch thất bại!
                    </h3>
                    <a href="${pageContext.request.contextPath}/CartServlet?action=list" class="cart-button">
                        🛒 Quay lại giỏ hàng
                    </a>
                </div>
            </c:if>

            <!-- Đang xử lý giao dịch -->
            <c:if test="${transResult == null}">
                <div>
                    <h3 class="status-message processing">
                        Chúng tôi đã tiếp nhận đơn hàng, xin chờ quá trình xử lý!
                    </h3>
                    <a href="${pageContext.request.contextPath}/CartServlet?action=list" class="cart-button">
                        🛒 Quay lại giỏ hàng
                    </a>
                </div>
            </c:if>
        </section>

    </body>
</html>
