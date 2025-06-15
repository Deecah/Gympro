<%-- 
    Document   : schedule
    Created on : Jun 1, 2025, 3:38:26 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Workout Schedule Demo</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .day-box {
                border: 1px solid #ccc;
                min-height: 120px;
                padding: 10px;
            }
            .day-box:hover {
                background-color: #f8f9fa;
            }
            .workout {
                background-color: #e9ecef;
                padding: 5px;
                margin-top: 5px;
                border-radius: 4px;
            }
        </style>
    </head>
    <body>
        <div class="container mt-4">
            <h3>Workout Program: <em>Fat Burning</em></h3>
            <div class="row">
                <div class="col">Day 1</div>
                <div class="col">Day 2</div>
                <div class="col">Day 3</div>
                <div class="col">Day 4</div>
                <div class="col">Day 5</div>
                <div class="col">Day 6</div>
                <div class="col">Day 7</div>
            </div>
            <div class="row mt-2">
                <!-- Repeat for each day -->
                <div class="col day-box" id="day1">
                    <strong>Workout</strong>
                    <div class="workout">A) Push-up<br>3 sets x 15 reps</div>
                    <button class="btn btn-sm btn-primary mt-2" onclick="addWorkout(1)">Add</button>
                </div>
                <div class="col day-box" id="day2">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(2)">Add</button>
                </div>
                <div class="col day-box" id="day3">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(3)">Add</button>
                </div>
                <div class="col day-box" id="day4">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(4)">Add</button>
                </div>
                <div class="col day-box" id="day5">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(5)">Add</button>
                </div>
                <div class="col day-box" id="day6">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(6)">Add</button>
                </div>
                <div class="col day-box" id="day7">
                    <button class="btn btn-sm btn-primary" onclick="addWorkout(7)">Add</button>
                </div>
            </div>
        </div>

        <!-- JavaScript logic -->
        <script>
            function addWorkout(dayNumber) {
                const text = prompt("Enter workout (e.g., A) Squat\n3x12)");
                if (text) {
                    const dayBox = document.getElementById('day' + dayNumber);
                    const div = document.createElement('div');
                    div.className = 'workout';
                    div.innerHTML = text;
                    dayBox.appendChild(div);
                }
            }
        </script>
    </body>
</html>