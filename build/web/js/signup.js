document.getElementById("sign-up-button").addEventListener("click", async function () {

    const firstName = document.getElementById("firstName").value;
    const lastName = document.getElementById("lastName").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const repassword = document.getElementById("repassword").value;

    const user = {
        firstName: firstName,
        lastName: lastName,
        email: email,
        password: password,
        repassword: repassword
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "SignUp",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: userJson
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            showNotification("success", json.message);
            verifyAccount();
        } else {
            if (json.message === "User already exist. Please sign in.") {
                showNotification("error", json.message);
                setTimeout(function () {
                    window.location = "signIn.html";
                }, 2000);
            } else {
                showNotification("error", json.message);
            }
        }
    } else {
        showNotification("error", "Something went wrong. Please try again");
    }
});

function togglePassword(id, element) {
    const input = document.getElementById(id);
    if (input.type === "password") {
        input.type = "text";
        element.textContent = "🙈";
    } else {
        input.type = "password";
        element.textContent = "👁️";
    }
}

function verifyAccount() {
    document.getElementById("sign-up-section").classList.add("hidden");
    document.getElementById("accountVerify-section").classList.remove("hidden");
}

document.getElementById("accountVerification").addEventListener("click", async function () {
    const verificationCode = document.getElementById("verificationCode").value;

    const verificationData = {
        verificationCode: verificationCode
    };

    const verificationJson = JSON.stringify(verificationData);

    const response = await fetch("AccountVerify",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },

                body: verificationJson
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            showNotification("success", json.message);
            sessionStorage.setItem("firstName", json.firstName);
            setTimeout(function () {
                window.location = "index.html";
            }, 4000);
        } else {

            if (json.message === "Email not found!") {
                showNotification("error", "Please sign up");
                setTimeout(function () {
                    window.location = "signUp.html";
                }, 2000);
            } else {
                showNotification("error", json.message);
            }
        }
    } else {
        showNotification("error", "Verification failed. Try again.");
        setTimeout(function () {
            window.location = "signIn.html";
        }, 2000);
    }

});

