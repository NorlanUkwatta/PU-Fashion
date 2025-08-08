// notification.js

// Create container if it doesn't exist
let container = document.getElementById("notification-container");
if (!container) {
  container = document.createElement("div");
  container.id = "notification-container";
  document.body.appendChild(container);
}

function showNotification(type, message) {
  const notification = document.createElement("div");
  notification.classList.add("custom-notification", `notification-${type}`);

  // Icon
  const icon = {
    success: "✔",
    error: "❌",
    warning: "⚠"
  }[type] || "";

  // Content
  notification.innerHTML = `
    <span class="notification-icon">${icon}</span>
    <span class="notification-message">${message}</span>
    <button class="notification-close">&times;</button>
  `;

  // Close button event
  notification.querySelector(".notification-close").addEventListener("click", () => {
    notification.classList.remove("show");
    setTimeout(() => notification.remove(), 300);
  });

  // Append to container
  container.appendChild(notification);

  // Show animation
  setTimeout(() => {
    notification.classList.add("show");
  }, 10);

  // Auto-dismiss after 4s
  setTimeout(() => {
    if (notification.parentNode) {
      notification.classList.remove("show");
      setTimeout(() => notification.remove(), 300);
    }
  }, 2000);
}
