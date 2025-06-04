<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head><title>Email Verification</title></head>
<body>
    <form action="EmailVerificationServlet" method="post">
        Enter verification code: <input type="text" name="code" required><br>
        <input type="submit" value="Verify">
    </form>
</body>
</html>
