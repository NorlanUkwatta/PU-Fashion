document.addEventListener("DOMContentLoaded", function () {

    const firstName = sessionStorage.getItem("firstName");


    const adminName = document.getElementById("adminName");

    adminName.innerHTML = "Hi " + firstName;

});