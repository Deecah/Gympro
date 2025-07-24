<!-- Footer Section Begin -->
<footer class="footer-section">
    <div class="container">
        <div class="row">
            <div class="col-lg-4">
                <div class="contact-option">
                    <span>Phone</span>
                    <p>02888888888</p>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="contact-option">
                    <span>Address</span>
                    <p>FPT CITY, DA NANG</p>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="contact-option">
                    <span>Email</span>
                    <p>contactcompany@gympro.com</p>
                </div>
            </div>
        </div>
        <div class="subscribe-option set-bg" data-setbg="img/footer-signup.jpg">
            <div class="so-text">
                <h4>Subscribe To Our Mailing List</h4>
                <p>Sign up to receive the latest information </p>
            </div>
            <form action="#" class="subscribe-form">
                <input type="text" placeholder="Enter Your Mail">
                <button type="submit"><i class="fa fa-send"></i></button>
            </form>
        </div>
        <div class="copyright-text">
            <ul>
                <li><a href="#">Term&Use</a></li>
                <li><a href="#">Privacy Policy</a></li>
            </ul>
            <p>
                Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved |
                This template is made with <i class="fa fa-heart" aria-hidden="true"></i> by 
                <a href="https://colorlib.com" target="_blank">Colorlib</a>
            </p>
            <div class="footer-social">
                <a href="#"><i class="fa fa-facebook"></i></a>
                <a href="#"><i class="fa fa-twitter"></i></a>
                <a href="#"><i class="fa fa-instagram"></i></a>
                <a href="#"><i class="fa fa-dribbble"></i></a>
            </div>
        </div>
    </div>
</footer>
<!-- Footer Section End -->
<!-- Chat Floating Button -->
<c:if test="${not empty user}">
    <a href="ChatServlet?userId=${user.userId}"
       class="btn btn-primary position-fixed m-4 shadow rounded-circle d-flex align-items-center justify-content-center"
       style="width: 56px; height: 56px; z-index: 99999; bottom: 20px; right: 20px;"
       title="Chat">
        <i class="fa fa-comments" style="font-size: 20px;"></i>
    </a>
</c:if>
<script>
    function toggleMenu() {
        const menu = document.getElementById("dropdownMenu");
        menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }

    window.addEventListener("click", function (e) {
        const menu = document.getElementById("dropdownMenu");
        const avatar = document.querySelector(".header-avatar img");
        if (!menu.contains(e.target) && !avatar.contains(e.target)) {
            menu.style.display = "none";
        }
    });

    // JavaScript cho N�t chu�ng th�ng b�o
    const notificationBell = document.getElementById('notificationBell');
    const notificationBox = document.getElementById('notificationBox');
    const notificationCount = document.getElementById('notificationCount');
    const notificationList = document.getElementById('notificationList');

    if (notificationBell && notificationBox) { // ??m b?o c�c ph?n t? t?n t?i tr??c khi th�m listener
        notificationBell.addEventListener('click', function (event) {
            event.stopPropagation(); // Ng?n ch?n s? ki?n click lan ra ngo�i
            if (notificationBox.style.display === 'block') {
                notificationBox.style.display = 'none';
            } else {
                notificationBox.style.display = 'block';
                // ?�ng menu avatar n?u n� ?ang m?
                const avatarMenu = document.getElementById("dropdownMenu");
                if (avatarMenu) {
                    avatarMenu.style.display = 'none';
                }
                // Khi m? h?p th�ng b�o, c� th? reset s? l??ng th�ng b�o v? 0
                if (notificationCount) {
                    notificationCount.style.display = 'none';
                    notificationCount.textContent = '0'; // C?p nh?t s? l??ng hi?n th?
                }
            }
        });
    }
    // ?�ng c? h?p th�ng b�o v� menu avatar khi click ra ngo�i
    window.addEventListener("click", function (e) {
        const avatarMenu = document.getElementById("dropdownMenu");
        const avatar = document.querySelector(".header-avatar img");

        // ?�ng menu avatar
        if (avatarMenu && !avatarMenu.contains(e.target) && (!avatar || !avatar.contains(e.target))) {
            avatarMenu.style.display = "none";
        }

        // ?�ng h?p th�ng b�o
        if (notificationBox && notificationBell && !notificationBox.contains(e.target) && !notificationBell.contains(e.target)) {
            notificationBox.style.display = 'none';
        }
    });

    // C?p nh?t hi?n th? badge th�ng b�o khi t?i trang
    document.addEventListener('DOMContentLoaded', function () {
        if (notificationCount) {
            const initialCount = parseInt(notificationCount.textContent);
            if (initialCount > 0) {
                notificationCount.style.display = 'flex'; // Hi?n th? n?u c� th�ng b�o
            } else {
                notificationCount.style.display = 'none'; // ?n n?u kh�ng c�
            }
        }
    });

</script>