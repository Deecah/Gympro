<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Certification</title>
</head>
<body>
    <h2>Add New Certification</h2>
    <form action="addCertification" method="post">
        <label>Name:</label><br>
        <input type="text" name="name" required><br><br>

        <label>Description:</label><br>
        <textarea name="description" rows="4" cols="50"></textarea><br><br>

        <label>Expire Date:</label><br>
        <input type="datetime-local" name="expireDate" required><br><br>

        <input type="submit" value="Add Certification">
    </form>
</body>
</html>
