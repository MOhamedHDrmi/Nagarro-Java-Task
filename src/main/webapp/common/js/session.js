setInterval(function () {
    let token = localStorage.getItem("token");

    if (!token || parseJwt(token).exp < Date.now() / 1000) {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/logOut",
            headers: {'Authorization': 'Bearer ' + token}
        });
        localStorage.clear();
        window.location.href = "login.html";
    }

}, 1000);