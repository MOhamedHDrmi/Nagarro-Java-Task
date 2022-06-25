$(document).ready(function () {
    $("#loginButton").click(function(){
        var username = $("#username").val();
        var password = $("#password").val();
        var user = {"userName": username, "password": password};

        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            url: "http://localhost:8080/login",
            data: JSON.stringify(user),
            success: function (data) {
                localStorage.setItem("token", data.jwt);
                window.location.href = "/statement-search.html";
            },
            error: function (data, status, error){
                var message = "<span  class=\"alert alert-danger\">" + data.responseJSON.message + "</span>";
                document.getElementById('failureMessage').innerHTML = message;
            }
        })
    })

})