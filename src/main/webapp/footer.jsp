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
        <!-- N�t Chat -->
        <a href="ChatServlet?userId=${user.userId}"
           class="btn btn-primary position-fixed shadow rounded-circle d-flex align-items-center justify-content-center"
           style="width: 56px; height: 56px; z-index: 99999; bottom: 20px; right: 20px;"
           title="Chat">
            <i class="fa fa-comments" style="font-size: 20px;"></i>
        </a>

        <!-- N�t L�n ??u (n?m ph�a tr�n n�t Chat) -->
        <button id="backToTop" title="Go to top"
                class="floating-btn back-to-top-btn"
                style="bottom: 90px; display: none;">
            <i class="fa fa-arrow-up" style="font-size: 20px; color: white;"></i>
        </button>
    </c:when>

    <c:otherwise>
        <!-- Ch? c� n�t L�n ??u n?m ? g�c -->
        <button id="backToTop" title="Go to top"
                class="floating-btn back-to-top-btn"
                style="bottom: 20px; right: 20px; display: none;">
            <i class="fa fa-arrow-up" style="font-size: 20px; color: white;"></i>
        </button>
    </c:otherwise>
</c:choose>


<script>
    // Function cho user đã đăng nhập
    function toggleMenu() {
        const menu = document.getElementById("dropdownMenu");
        const guestMenu = document.getElementById("guestDropdownMenu");
        
        // Đóng guest menu nếu đang mở
        if (guestMenu) {
            guestMenu.style.display = "none";
        }
        
        // Toggle user menu
        if (menu) {
            menu.style.display = (menu.style.display === "block") ? "none" : "block";
        }
    }

    // Function cho guest (chưa đăng nhập)
    function toggleGuestMenu() {
        const guestMenu = document.getElementById("guestDropdownMenu");
        const menu = document.getElementById("dropdownMenu");
        
        // Đóng user menu nếu đang mở
        if (menu) {
            menu.style.display = "none";
        }
        
        // Toggle guest menu
        if (guestMenu) {
            guestMenu.style.display = (guestMenu.style.display === "block") ? "none" : "block";
        }
    }

    // Event listener để đóng menu và notification khi click ra ngoài
    window.addEventListener("click", function (e) {
        const menu = document.getElementById("dropdownMenu");
        const guestMenu = document.getElementById("guestDropdownMenu");
        const avatar = document.querySelector(".header-avatar img");
        
        // Đóng user menu
        if (menu && !menu.contains(e.target) && (!avatar || !avatar.contains(e.target))) {
            menu.style.display = "none";
        }
        
        // Đóng guest menu
        if (guestMenu && !guestMenu.contains(e.target) && (!avatar || !avatar.contains(e.target))) {
            guestMenu.style.display = "none";
        }
        
        // Đóng hộp thông báo
        if (notificationBox && notificationBell && !notificationBox.contains(e.target) && !notificationBell.contains(e.target)) {
            notificationBox.style.display = 'none';
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
                const guestMenu = document.getElementById("guestDropdownMenu");
                if (avatarMenu) {
                    avatarMenu.style.display = 'none';
                }
                if (guestMenu) {
                    guestMenu.style.display = 'none';
                }
                // Khi m? h?p th�ng b�o, c� th? reset s? l??ng th�ng b�o v? 0
                if (notificationCount) {
                    notificationCount.style.display = 'none';
                    notificationCount.textContent = '0'; // C?p nh?t s? l??ng hi?n th?
                }
            }
        });
    }
    // Cập nhật hiển thị badge thông báo khi tải trang
    document.addEventListener('DOMContentLoaded', function () {
        if (notificationCount) {
            const initialCount = parseInt(notificationCount.textContent);
            if (initialCount > 0) {
                notificationCount.style.display = 'flex'; // Hiển thị nếu có thông báo
            } else {
                notificationCount.style.display = 'none'; // Ẩn nếu không có
            }
        }
        
        // Xử lý chuyển đến form đăng ký khi có hash #signup
        if (window.location.hash === '#signup') {
            const signUpButton = document.getElementById('signUp');
            if (signUpButton) {
                signUpButton.click();
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
