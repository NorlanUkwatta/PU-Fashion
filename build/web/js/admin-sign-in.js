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

function showForgot() {
    document.getElementById("signin-section").classList.add("hidden");
    document.getElementById("forgot-section").classList.remove("hidden");
    document.getElementById("step1").classList.add("active");
    document.getElementById("step2").classList.remove("active");
}

function backToSignin() {
    document.getElementById("forgot-section").classList.add("hidden");
    document.getElementById("verify-section").classList.add("hidden");
    document.getElementById("signin-section").classList.remove("hidden");
}

function showVerification() {
    document.getElementById("verify-section").classList.remove("hidden");
    document.getElementById("step2").classList.add("active");
}

function resetPassword() {
    document.getElementById("forgot-password-enter-section").classList.remove("hidden");
}


document.getElementById("admin-sign-in-button").addEventListener("click", async function () {

    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const user = {
        email: email,
        password: password
    };

    const userJson = JSON.stringify(user);

    const response = await fetch(
            "AdminSignIn",
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
            if (json.message == 1) {
                showNotification("success", "Your account not verified. Please verify.");
                setTimeout(function () {
                    verifyAccount();
                }, 2000);
            } else {
                showNotification("success", "Success");
                sessionStorage.setItem("firstName", json.firstName);
                setTimeout(function () {
                    window.location = "admin.html";
                }, 2000);
            }

        } else {
            showNotification("error", json.message);
        }
    } else {
        showNotification("error", "Something went wrong. Please try again");
    }
});

function verifyAccount() {
    document.getElementById("signin-section").classList.add("hidden");
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
                window.location = "admin.html";
            }, 4000);
        } else {

            if (json.message === "Email not found!") {
                showNotification("error", "Please sign up");
                setTimeout(function () {
                    window.location = "admin-sign-up.html";
                }, 2000);
            } else {
                showNotification("error", json.message);
            }
        }
    } else {
        showNotification("error", "Verification failed. Try again.");
        setTimeout(function () {
            window.location = "admin-sign-in.html";
        }, 2000);
    }

});

document.getElementById("forgot-password-verify-button").addEventListener('click', async function () {

    const email = document.getElementById("forgot-password-email").value;

    const user = {
        email: email
    };

    const userJson = JSON.stringify(user);

    const response = await fetch('ForgotPassword',
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: userJson
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            showNotification("success", json.message);
            resetPassword();
        } else {
            showNotification("error", json.message);
        }
    } else {
        showNotification("error", "Verification failed. Try again.");
        setTimeout(function () {
            window.location = "admin-sign-in.html";
        }, 2000);
    }

});


document.getElementById("forgot-password-btn").addEventListener('click', async function () {
    const verificationCode = document.getElementById("passwordverificationCode").value;
    const fnp = document.getElementById("fnp").value;
    const frp = document.getElementById("frp").value;

    const user = {
        verificationCode: verificationCode,
        fnp: fnp,
        frp: frp
    };

    const userJson = JSON.stringify(user);

    const response = await fetch('ResetPassword',
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: userJson
            });

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            showNotification("success", json.message);
            sessionStorage.setItem("firstName", json.firstName);
            setTimeout(function () {
                window.location = "admin.html";
            }, 2000);
        } else {
            showNotification("error", json.message);
        }
    } else {
        showNotification("error", "Password reset failed. Try again.");
        setTimeout(function () {
            window.location = "admin-sign-in.html";
        }, 2000);
    }
});