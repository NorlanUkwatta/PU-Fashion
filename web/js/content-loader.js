document.addEventListener("DOMContentLoaded", function () {
    // Load header
    fetch('header.html')
            .then(response => response.text())
            .then(data => {
                document.getElementById('header-placeholder').innerHTML = data;

                var dropdowns = document.querySelectorAll('.dropdown-toggle');
                dropdowns.forEach(function (dropdown) {
                    new bootstrap.Dropdown(dropdown);
                });

                const firstName = sessionStorage.getItem("firstName");

                if (firstName) {
                    const signIn = document.getElementById("sign-in-id");
                    const nameLi = document.getElementById("name-id-li");
                    const nameSpan = document.getElementById("name-id");

                    signIn.classList.add("d-none");
                    nameLi.classList.remove("d-none");
                    nameSpan.textContent = "Hi, " + firstName;

                } else {
                    signIn.classList.remove("d-none");
                    nameLi.classList.add("d-none");
                }
            });

    // Load footer
    fetch('footer.html')
            .then(response => response.text())
            .then(data => {
                document.getElementById('footer-placeholder').innerHTML = data;
            });
});

//document.addEventListener("DOMContentLoaded", function () {
//    // Load header
//    fetch('header.html')
//            .then(response => response.text())
//            .then(data => {
//                document.getElementById('header-placeholder').innerHTML = data;
//
//                var dropdowns = document.querySelectorAll('.dropdown-toggle');
//                dropdowns.forEach(function (dropdown) {
//                    new bootstrap.Dropdown(dropdown);
//                });
//
//                const firstName = sessionStorage.getItem("firstName");
//
//                if (firstName) {
//                    const signIn = document.getElementById("sign-in-id");
//                    const nameLi = document.getElementById("name-id-li");
//                    const nameSpan = document.getElementById("name-id");
//
//                    signIn.classList.add("d-none");
//                    nameLi.classList.remove("d-none");
//                    nameSpan.textContent = "Hi, " + firstName;
//
//                } 
//            });
//
//    // Load footer
//    fetch('footer.html')
//            .then(response => response.text())
//            .then(data => {
//                document.getElementById('footer-placeholder').innerHTML = data;
//            });
//});
