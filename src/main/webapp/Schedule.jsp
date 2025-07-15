<!-- timetable.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Weekly Timetable</title>
  <link rel="stylesheet" href="stylecss/schedule.css" />
</head>
<body>
  <div class="layout">
    <div class="sidebar">
      <div class="sidebar-top">
        <button class="sidebar-home">Home</button>
      </div>
      <div class="sidebar-menu">
        <button>My Schedule</button>
        <button>Requests</button>
      </div>
    </div>
    <div class="timetable-wrapper">
      <div class="timetable-border-fade"></div>
      <table class="timetable">
        <thead>
          <tr>
            <th>THỨ 2<br><span>29/07</span></th>
            <th>THỨ 3<br><span>30/07</span></th>
            <th>THỨ 4<br><span>31/07</span></th>
            <th>THỨ 5<br><span>01/08</span></th>
            <th>THỨ 6<br><span>02/08</span></th>
            <th>THỨ 7<br><span>03/08</span></th>
            <th>CHỦ NHẬT<br><span>04/08</span></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><strong>Vinyasa Yoga</strong> (Ms. Liên)<br><span class="time">9:00 - 10:00</span></td>
            <td><strong>Hip Opening</strong> (Mr. Lalit)<br><span class="time">9:00 - 10:00</span></td>
            <td><strong>Back Bend Yoga</strong> (Ms. Liên)<br><span class="time">9:00 - 10:00</span></td>
            <td><strong>Vinyasa Yoga</strong> (Mr. Satya Sai)<br><span class="time">9:00 - 10:00</span></td>
            <td><strong>Power Yoga</strong> (Ms. Liên)<br><span class="time">9:00 - 10:00</span></td>
            <td></td>
            <td><strong>Yin Yoga</strong> (Ms. Dương Vy)<br><span class="time">9:00 - 10:00</span></td>
          </tr>
          <tr>
            <td><strong>Power Yoga</strong> (Ms. Liên)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Dynamic Yoga</strong> (Ms. Dương Vy)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Hatha yoga</strong> (Ms. Liên)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Detox Yoga</strong> (Ms. Dương Vy)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Hip Opening</strong> (Ms. Liên)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Yin Yoga</strong> (Ms. Dương Vy)<br><span class="time">10:00 - 11:00</span></td>
            <td><strong>Detox Yoga</strong> (Ms. Dương Vy)<br><span class="time">10:00 - 11:00</span></td>
          </tr>
          <tr>
            <td><strong>Yoga Mix</strong> (Mr. Kiran)<br><span class="time">18:00 - 19:00</span></td>
            <td><strong>Vinyasa Yoga</strong> (Mr. Satya Sai)<br><span class="time">18:00 - 19:00</span></td>
            <td><strong>Shoulder Opening</strong> (Mr. Kiran)<br><span class="time">18:00 - 19:00</span></td>
            <td><strong>Back Bending</strong> (Mr. Satya Sai)<br><span class="time">18:00 - 19:00</span></td>
            <td><strong>Twist Yoga</strong> (Mr. Ajay)<br><span class="time">18:00 - 19:00</span></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td><strong>Flexibility Yoga</strong> (Ms. My)<br><span class="time">19:15 - 20:15</span></td>
            <td><strong>Dynamic Yoga</strong> (Ms. My)<br><span class="time">19:15 - 20:15</span></td>
            <td><strong>Hip Opening</strong> (Ms. My)<br><span class="time">19:15 - 20:15</span></td>
            <td><strong>Vinyasa Yoga</strong> (Ms. My)<br><span class="time">19:15 - 20:15</span></td>
            <td><strong>Flexibility Yoga</strong> (Ms. My)<br><span class="time">19:15 - 20:15</span></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td class="highlight">Zumba (Ms. Thảo)<br><span class="time">20:30 - 21:30</span></td>
            <td class="highlight">Zumba (Ms. Thảo)<br><span class="time">20:30 - 21:30</span></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </tbody>
      </table>
    </div>
    <button class="request-button" title="New Request">+</button>
  </div>

  <!-- Context Menu -->
  <ul id="contextMenu" class="context-menu" style="display: none;">
    <li id="editOption">Edit</li>
    <li id="addOption">Add</li>
  </ul>

  <script>
    const menu = document.getElementById('contextMenu');
    let targetCell = null;

    document.addEventListener('contextmenu', function (e) {
      if (e.target.closest('td')) {
        e.preventDefault();
        targetCell = e.target.closest('td');

        const hasContent = targetCell.textContent.trim() !== '';
        document.getElementById('editOption').style.display = hasContent ? 'block' : 'none';
        document.getElementById('addOption').style.display = hasContent ? 'none' : 'block';

        menu.style.top = `${e.pageY}px`;
        menu.style.left = `${e.pageX}px`;
        menu.style.display = 'block';
      } else {
        menu.style.display = 'none';
      }
    });

    document.addEventListener('click', function () {
      menu.style.display = 'none';
    });

    document.getElementById('editOption').onclick = () => {
      alert('Edit: ' + targetCell.innerText);
    };

    document.getElementById('addOption').onclick = () => {
      alert('Add new event');
    };
  </script>
</body>
</html>
