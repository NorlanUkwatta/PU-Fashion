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
