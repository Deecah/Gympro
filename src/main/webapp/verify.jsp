<%-- 
    Document   : verify
    Created on : Jun 2, 2025, 12:22:17 AM
    Author     : ASUS
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Email Verification</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f0f2f5;
        }
        .verify-box {
            max-width: 400px;
            margin: 100px auto;
            padding: 30px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="verify-box">
        <h4 class="text-center mb-4">Email Verification</h4>
        <form action="VerificationServlet" method="get">
            <div class="mb-3">
                <label for="code" class="form-label">Enter Verification Code</label>
                <input type="text" class="form-control" id="code" name="code" required>
            </div>
            <div class="d-grid">
                <button type="submit" class="btn btn-primary">Verify</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
