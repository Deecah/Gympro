// WebSocket connection for real-time notifications
let notificationSocket = null;

function connectNotificationSocket() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = protocol + '//' + window.location.host + window.location.pathname.replace(/\/[^\/]*$/, '') + '/notification';
    
    notificationSocket = new WebSocket(wsUrl);
    
    notificationSocket.onopen = function(event) {
        console.log('Notification WebSocket connected');
        // Register user ID for notifications
        if (typeof currentUserId !== 'undefined' && currentUserId) {
            notificationSocket.send('register:' + currentUserId);
        }
    };
    
    notificationSocket.onmessage = function(event) {
        try {
            const data = JSON.parse(event.data);
            if (data.type === 'notification') {
                showRealTimeNotification(data);
                updateNotificationList(data);
            }
        } catch (e) {
            console.error('Error parsing notification:', e);
        }
    };
    
    notificationSocket.onclose = function(event) {
        console.log('Notification WebSocket disconnected');
        // Reconnect after 3 seconds
        setTimeout(connectNotificationSocket, 3000);
    };
    
    notificationSocket.onerror = function(error) {
        console.error('Notification WebSocket error:', error);
    };
}

function showRealTimeNotification(data) {
    // Tạo notification element
    const notificationDiv = document.createElement('div');
    notificationDiv.className = 'real-time-notification';
    notificationDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        z-index: 10000;
        max-width: 300px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        animation: slideIn 0.3s ease-out;
    `;
    
    // Set background color based on notification type
    switch(data.notificationType) {
        case 'success':
            notificationDiv.style.backgroundColor = '#28a745';
            break;
        case 'error':
            notificationDiv.style.backgroundColor = '#dc3545';
            break;
        case 'warning':
            notificationDiv.style.backgroundColor = '#ffc107';
            notificationDiv.style.color = '#212529';
            break;
        case 'info':
            notificationDiv.style.backgroundColor = '#17a2b8';
            break;
        default:
            notificationDiv.style.backgroundColor = '#6c757d';
    }
    
    notificationDiv.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: start;">
            <div>
                <div style="font-size: 14px; margin-bottom: 5px;">${data.title}</div>
                <div style="font-size: 12px; font-weight: normal; opacity: 0.9;">${data.content}</div>
            </div>
            <button onclick="this.parentElement.parentElement.remove()" 
                    style="background: none; border: none; color: inherit; font-size: 18px; cursor: pointer; margin-left: 10px;">&times;</button>
        </div>
    `;
    
    document.body.appendChild(notificationDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notificationDiv.parentElement) {
            notificationDiv.style.animation = 'slideOut 0.3s ease-in';
            setTimeout(() => {
                if (notificationDiv.parentElement) {
                    notificationDiv.remove();
                }
            }, 300);
        }
    }, 5000);
}

function updateNotificationList(data) {
    // Cập nhật notification count
    const countElement = document.getElementById('notificationCount');
    if (countElement) {
        const currentCount = parseInt(countElement.textContent) || 0;
        countElement.textContent = currentCount + 1;
    }
    
    // Thêm notification mới vào list
    const notificationList = document.getElementById('notificationList');
    if (notificationList) {
        const noNotifications = notificationList.querySelector('.no-notifications');
        if (noNotifications) {
            noNotifications.remove();
        }
        
        const newNotification = document.createElement('li');
        newNotification.className = 'notification-item';
        newNotification.innerHTML = `
            <p>${data.content}</p>
            <span class="notification-time">Just now</span>
        `;
        
        notificationList.insertBefore(newNotification, notificationList.firstChild);
    }
}

// Connect to WebSocket when page loads
document.addEventListener('DOMContentLoaded', function() {
    connectNotificationSocket();
});

// Add CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }

    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style); 