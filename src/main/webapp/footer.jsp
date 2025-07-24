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
        <div class="copyright-text">
            <ul>
                <li><a href="#">Term&Use</a></li>
                <li><a href="#">Privacy Policy</a></li>
            </ul>
            <p>
                Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved |
                This is made with <i class="fa fa-heart" aria-hidden="true"></i> by D02-RT01
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
<c:choose>
    <c:when test="${not empty user}">
        <!-- Nút Chat -->
        <a href="ChatServlet?userId=${user.userId}"
           class="btn btn-primary position-fixed shadow rounded-circle d-flex align-items-center justify-content-center"
           style="width: 56px; height: 56px; z-index: 99999; bottom: 20px; right: 20px;"
           title="Chat">
            <i class="fa fa-comments" style="font-size: 20px;"></i>
        </a>

        <!-- Nút Lên ??u (n?m phía trên nút Chat) -->
        <button id="backToTop" title="Go to top"
                class="floating-btn back-to-top-btn"
                style="bottom: 90px; display: none;">
            <i class="fa fa-arrow-up" style="font-size: 20px; color: white;"></i>
        </button>
    </c:when>

    <c:otherwise>
        <!-- Ch? có nút Lên ??u n?m ? góc -->
        <button id="backToTop" title="Go to top"
                class="floating-btn back-to-top-btn"
                style="bottom: 20px; right: 20px; display: none;">
            <i class="fa fa-arrow-up" style="font-size: 20px; color: white;"></i>
        </button>
    </c:otherwise>
</c:choose>


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

    // JavaScript cho Nút chuông thông báo
    const notificationBell = document.getElementById('notificationBell');
    const notificationBox = document.getElementById('notificationBox');
    const notificationCount = document.getElementById('notificationCount');
    const notificationList = document.getElementById('notificationList');

    if (notificationBell && notificationBox) { // ??m b?o các ph?n t? t?n t?i tr??c khi thêm listener
        notificationBell.addEventListener('click', function (event) {
            event.stopPropagation(); // Ng?n ch?n s? ki?n click lan ra ngoài
            if (notificationBox.style.display === 'block') {
                notificationBox.style.display = 'none';
            } else {
                notificationBox.style.display = 'block';
                // ?óng menu avatar n?u nó ?ang m?
                const avatarMenu = document.getElementById("dropdownMenu");
                if (avatarMenu) {
                    avatarMenu.style.display = 'none';
                }
                // Khi m? h?p thông báo, có th? reset s? l??ng thông báo v? 0
                if (notificationCount) {
                    notificationCount.style.display = 'none';
                    notificationCount.textContent = '0'; // C?p nh?t s? l??ng hi?n th?
                }
            }
        });
    }
    // ?óng c? h?p thông báo và menu avatar khi click ra ngoài
    window.addEventListener("click", function (e) {
        const avatarMenu = document.getElementById("dropdownMenu");
        const avatar = document.querySelector(".header-avatar img");

        // ?óng menu avatar
        if (avatarMenu && !avatarMenu.contains(e.target) && (!avatar || !avatar.contains(e.target))) {
            avatarMenu.style.display = "none";
        }

        // ?óng h?p thông báo
        if (notificationBox && notificationBell && !notificationBox.contains(e.target) && !notificationBell.contains(e.target)) {
            notificationBox.style.display = 'none';
        }
    });

    // C?p nh?t hi?n th? badge thông báo khi t?i trang
    document.addEventListener('DOMContentLoaded', function () {
        if (notificationCount) {
            const initialCount = parseInt(notificationCount.textContent);
            if (initialCount > 0) {
                notificationCount.style.display = 'flex'; // Hi?n th? n?u có thông báo
            } else {
                notificationCount.style.display = 'none'; // ?n n?u không có
            }
        }
    });


    const backToTopBtn = document.getElementById("backToTop");

    window.onscroll = function () {
        if (window.scrollY > 200) {
            backToTopBtn.style.display = "flex";
        } else {
            backToTopBtn.style.display = "none";
        }
    };

    backToTopBtn.onclick = function () {
        window.scrollTo({top: 0, behavior: 'smooth'});
    };


</script>
