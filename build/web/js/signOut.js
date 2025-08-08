async function signOut() {
    const response = await fetch("SignOut");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {

            sessionStorage.removeItem("firstName");

            document.getElementById("sign-in-id").classList.remove("d-none");
            document.getElementById("name-id-li").classList.add("d-none");
            document.getElementById("name-id").textContent = "";

            showNotification("success", "Successfuly signed out");

            setTimeout(function () {
                window.location = "signIn.html";
            }, 2000);
        } else {
            showNotification("error", "Sign out failed");
            window.location.reload();
        }
    } else {
        showNotification("success", "Sign out failed");
    }
}
;
