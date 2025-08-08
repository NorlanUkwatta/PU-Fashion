document.getElementById("admin-logout").addEventListener('click', async function () {
    const response = await fetch("SignOut");
    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            sessionStorage.removeItem("firstName");
            showNotification("success", "Successfuly signed out");
            setTimeout(function () {
                window.location = "admin-sign-in.html";
            }, 2000);
        } else {
            showNotification("error", "Sign out failed");
        }
    } else {
        showNotification("error", "Log out failed");
    }
});