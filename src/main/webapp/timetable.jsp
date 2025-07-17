<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Weekly Timetable</title>

    <!-- Fonts & Icons -->
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <!-- Core & Custom CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="css/style.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/notification.css" type="text/css">
    <style>
        body {
            background-color: #f2fcfa;
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
        }
        .section-wrapper {
            max-width: 1300px;
            margin: 60px auto;
            padding: 30px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 16px rgba(0,0,0,0.05);
        }
        .timetable {
            width: 100%;
            border-collapse: collapse;
            table-layout: fixed;
        }
        th {
            background-color: #e3f2fd;
            color: #0277bd;
            font-size: 14px;
            font-weight: 600;
            border: 1px solid #cfd8dc;
            padding: 14px;
            height: 100px;
        }
        .time-col {
            background-color: #f0f4c3;
            color: #33691e;
            font-weight: bold;
            width: 130px;
        }
        td {
            border: 1px solid #e0e0e0;
            height: 100px;
            width: 130px;
            vertical-align: top;
            text-align: center;
            padding: 4px;
        }
        .workout {
            background-color: #fff8e1;
            border-left: 4px solid #ff9800;
            padding: 8px;
            border-radius: 5px;
            font-size: 13px;
            color: #5d4037;
            text-align: left;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb/classes-breadcrumb.jpg">
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <div class="breadcrumb-text">
                    <h2>Package</h2>
                    <div class="breadcrumb-option">
                        <a href="./index.jsp"><i class="fa fa-home"></i> Home</a>
                        <span>Weekly Training Schedule</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="section-wrapper">
    <h2 class="text-center text-success mb-4">WEEKLY TRAINING SCHEDULE</h2>
    <div class="timetable-wrapper">
        <table class="timetable">
            <thead>
                <tr>
                    <th class="time-col">
                        <form action="timetable" method="get">
                            <select name="weekRange" onchange="this.form.submit()">
                                <c:forEach var="opt" items="${weekOptions}">
                                    <c:set var="parts" value="${fn:split(opt, ' - ')}" />
                                    <option value="${opt}" ${opt == currentWeekRange ? 'selected' : ''}>
                                        ${parts[0].substring(0, 5)} - ${parts[1].substring(0, 5)}
                                    </option>
                                </c:forEach>
                            </select>
                        </form>
                    </th>
                    <c:forEach var="date" items="${weekDates}">
                        <th>
                            <fmt:formatDate value="${date}" pattern="EEEE"/><br>
                            <fmt:formatDate value="${date}" pattern="dd/MM"/>
                        </th>
                    </c:forEach>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="h" items="${[7,9,14,16,18]}">
                    <tr>
                        <td class="time-col">
                            ${h < 10 ? '0' : ''}${h}:00 - ${h+2 < 10 ? '0' : ''}${h+2}:00
                        </td>
                        <c:forEach var="date" items="${weekDates}">
                            <td>
                                <c:forEach var="w" items="${scheduleMap[date]}">
                                    <c:if test="${w.startTime.hour == h}">
                                        <div class="workout">
                                            <strong>${w.title}</strong><br/>
                                            <span>${w.startTime} - ${w.endTime}</span><br/>
                                            <em>${w.notes}</em>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- JS -->
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery.magnific-popup.min.js"></script>
<script src="js/mixitup.min.js"></script>
<script src="js/jquery.slicknav.js"></script>
<script src="js/owl.carousel.min.js"></script>
<script src="js/main.js"></script>

<jsp:include page="footer.jsp" />
</body>
</html>
