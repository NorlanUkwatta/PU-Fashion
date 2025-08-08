async function signOut() {
    const response = await fetch("SignOut");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {

            sessionStorage.removeItem("firstName");
            sessionStorage.clear();

            document.getElementById("sign-in-id").classList.remove("d-none");
            document.getElementById("name-id-li").classList.add("d-none");
            document.getElementById("name-id").textContent = "";

            window.location = "signIn.html";
        } else {
            window.location.reload();
        }
    } else {
        console.log("log out failed!");
    }
}
;
