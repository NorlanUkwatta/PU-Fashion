document.getElementById("addProduct").addEventListener("click", async function () {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const qty = document.getElementById("qty").value;
    const category = document.getElementById("category").value;
    const color = document.getElementById("color").value;
    const status = document.getElementById("status").value;
    const size = document.getElementById("size").value;
    const img1 = document.getElementById("img1").files[0];
    const img2 = document.getElementById("img2").files[0];
    const img3 = document.getElementById("img3").files[0];
    const img4 = document.getElementById("img4").files[0];

    const form = new FormData();
    form.append("title", title);
    form.append("description", description);
    form.append("qty", qty);
    form.append("category", category);
    form.append("color", color);
    form.append("status", status);
    form.append("size", size);
    form.append("img1", img1);
    form.append("img2", img2);
    form.append("img3", img3);
    form.append("img4", img4);

    const response = await fetch("AddProduct",
            {
                method: "POST",
                body: form
            }
    );

    if (response.ok) {
        const json = await response.json();
        if (status) {
            showNotification("success", json.message);
        } else {
            showNotification("error", json.message);
        }
    } else {
        if (json.message === "User not found. Please sign in!") {
//            window.location = "signin.html";
            showNotification("error", json.message);
        } else {
            showNotification("error", json.message);
        }
    }

});