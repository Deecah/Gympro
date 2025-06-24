<%-- 
    Document   : login
    Created on : May 26, 2025, 9:28:22 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
        <link rel="stylesheet" href="stylecss/login.css" type="text/css">
        <link rel="stylesheet" href="stylecss/alert.css" type="text/css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style> 
            .role-select { display: flex; justify-content: center; gap: 20px; margin: 20px 0;}
            .role-option { position: relative; cursor: pointer;}
            .role-option input[type="radio"] { display: none;}
            .role-option span { display: inline-block; padding: 10px 20px; border: 2px solid #ff4b2b; border-radius: 30px; font-weight: bold; color: #ff4b2b; transition: 0.3s;}
            .role-option input[type="radio"]:checked + span { background-color: #ff4b2b; color: white;}
        </style>
    </head>
    <body>
        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success">
                Verification successful! You can now log in.
                <button class="close-btn" onclick="this.parentElement.style.display='none';">&times;</button>
            </div>
        </c:if>
        <c:if test="${param.msg == 'fail'}">
            <div class="alert alert-danger">
                Verification failed! Please try again.
                <button class="close-btn" onclick="this.parentElement.style.display='none';">&times;</button>
            </div>
        </c:if>

        <h2>Weekly Coding Challenge: Sign in/up Form</h2>
        <div class="container" id="container">
            <div class="form-container sign-up-container">
                <form action="${pageContext.request.contextPath}/VerificationServlet" method="post">
                    <h1>Create Account</h1>
                    <div class="social-container">
                        <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/SWP391/LoginServlet&response_type=code&client_id=582791377884-rafqmdbmn059o94eiraoipo1jljsblj7.apps.googleusercontent.com&approval_prompt=force"
                           class="social"><i class="fab fa-google-plus-g"></i></a>
                    </div>
                    <span>or use your email for registration</span>
                    <input type="text" name="name" placeholder="Name" />
                    <input type="email" name="email" placeholder="Email" />
                    <input type="password" name="password" placeholder="Password" />
                    <div class="role-select">
                        <label class="role-option">
                            <input type="radio" name="role" value="Customer" checked>
                            <span>I'm Customer</span>
                        </label>
                        <label class="role-option">
                            <input type="radio" name="role" value="Trainer">
                            <span> I'm Trainer</span>
                        </label>
                    </div>
                    <button type="submit" name="action" value="signup">Sign Up</button>
                </form>
            </div>
            <div class="form-container sign-in-container">
                <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
                    <h1>Sign in</h1>
                    <c:if test="${not empty error}">
                        <p style="color: red;">${error}</p>
                    </c:if>
                    <div class="social-container">
                        <a href="https://accounts.google.com/o/oauth2/auth?scope=email profile openid&redirect_uri=http://localhost:8080/SWP391/LoginServlet&response_type=code&client_id=582791377884-rafqmdbmn059o94eiraoipo1jljsblj7.apps.googleusercontent.com&approval_prompt=force" class="social"><i class="fab fa-google-plus-g"></i></a>
                    </div>
                    <span>or use your account</span>
                    <input type="email" name="email" placeholder="Email" />
                    <input type="password" name="password" placeholder="Password" />
                    <a href="requestPassword.jsp">Forgot your password?</a>
                    <button type="submit" name="action" value="signin">Sign In</button>
                </form>
            </div>
            <div class="overlay-container">
                <div class="overlay">
                    <div class="overlay-panel overlay-left">
                        <h1>Welcome Back!</h1>
                        <p>To keep connected with us please login with your personal info</p>
                        <button class="ghost" id="signIn">Sign In</button>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <h1>Hello, Friend!</h1>
                        <p>Enter your personal details and start journey with us</p>
                        <button class="ghost" id="signUp">Sign Up</button>
                    </div>
                </div>
            </div>
        </div>
        <footer>
            <p>
                Created by Group D02-RT01
            </p>
        </footer>
        <script>
            const signUpButton = document.getElementById('signUp');
            const signInButton = document.getElementById('signIn');
            const container = document.getElementById('container');
            signUpButton.addEventListener('click', () => {
                container.classList.add("right-panel-active");
            });
            signInButton.addEventListener('click', () => {
                container.classList.remove("right-panel-active");
            });
        </script>
    </body>
</html>