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
