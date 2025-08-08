let productList;

window.addEventListener("load", async function () {
    loadProductData();
});

async function loadProductData() {
    const response = await fetch("LoadProductData");

    if (response.ok) {
        const json = await response.json();
        if (json.status) {
            loadSelect("category", json.categoryList, "name");
            loadSelect("color", json.colorList, "name");
            loadSelect("status", json.statusList, "name");
            loadSelect("size", json.sizeList, "name");

            productList = json.productList;

        } else {
            document.getElementById("message").innerHTML = "Unable to load products. Please try again later!";
        }
    } else {
        document.getElementById("message").innerHTML = "Unable to load products. Please try again later!";
    }
}

function loadSelect(selectId, list, property) {

    const select = document.getElementById(selectId);

    list.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.innerHTML = item[property];
        select.appendChild(option);
    });

}

function loadProduct(){
    
}


document.getElementById("addProduct").addEventListener("click", async function () {
    event.preventDefault();

    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const price = document.getElementById("price").value;
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
    form.append("price", price);
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
        if (json.status) {
            showNotification("success", json.message);
            document.getElementById("title").value = "";
            document.getElementById("description").value = "";
            document.getElementById("price").value = "0.00";
            document.getElementById("qty").value = "0";
            document.getElementById("category").value = 0;
            document.getElementById("color").value = 0;
            document.getElementById("status").value = 0;
            document.getElementById("size").value = 0;
            document.getElementById("img1").value = "";
            document.getElementById("img2").value = "";
            document.getElementById("img3").value = "";
            document.getElementById("img4").value = "";
        } else {
            if (json.message == "Session expired or user not found. Please sign in") {
                showNotification("error", json.message);
                setTimeout(function () {
                    window.location = "admin-sign-in.html";
                }, 2000);
            } else {
                showNotification("error", json.message);
            }
        }
    } else {
        showNotification("error", "Somthing went wrong. Please try again");
    }

});