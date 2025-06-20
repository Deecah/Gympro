<%-- 
    Document   : addCertification
    Created on : Jun 16, 2025, 8:12:29 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Add Certification</title>
        <style>
            body {
                font-family: Arial, sans-serif;
            }

            form {
                width: 60%;
                margin: 40px auto;
                padding: 20px;
                border: 1px solid #ccc;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }

            label {
                display: block;
                margin-top: 10px;
                font-weight: bold;
            }

            input[type="text"], input[type="date"] {
                width: 100%;
                padding: 8px;
                margin-top: 5px;
            }

            input[type="submit"] {
                margin-top: 20px;
                padding: 10px 15px;
                background-color: #007bff;
                color: white;
                border: none;
                cursor: pointer;
            }

            a {
                display: inline-block;
                margin-top: 10px;
                text-decoration: none;
            }
        </style>
    </head>
    <body>
        <h2>Add New Certification</h2>
        <form action="AddCertificationServlet" method="post">
            <label>Name:</label><br>
            <input type="text" name="name" required><br><br>

            <label>Description:</label><br>
            <textarea name="description" rows="4" cols="50"></textarea><br><br>

            <label>Expire Date:</label><br>
            <input type="datetime-local" name="expireDate" required><br><br>

            <input type="submit" value="Add Certification">
            <br><a href="ViewCertificationServlet">← Back to list</a>
        </form>
    </body>
</html>
