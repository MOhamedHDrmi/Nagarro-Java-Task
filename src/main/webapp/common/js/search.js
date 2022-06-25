function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}

$(document).ready(function () {
    let token = localStorage.getItem("token");

    if (parseJwt(token).exp < Date.now() / 1000) {
        localStorage.clear();
        window.location.href = "login.html";
    }

    let params = " <div class=\"col-2\">\n" +
        "            <div class=\"form-outline\">\n" +
        "                <label class=\"form-label\" for=\"fromDate\">From Date</label>\n" +
        "                <input type=\"date\" data-date-format=\"yyyy-MM-dd\" id=\"fromDate\" class=\"form-control\"/>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"col-2\">\n" +
        "            <div class=\"form-outline\">\n" +
        "                <label class=\"form-label\" for=\"toDate\">To Date</label>\n" +
        "                <input type=\"date\" id=\"toDate\" class=\"form-control\"/>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"col-2\">\n" +
        "            <div class=\"form-outline\">\n" +
        "                <label class=\"form-label\" for=\"fromAmount\">From Amount</label>\n" +
        "                <input type=\"number\" id=\"fromAmount\" class=\"form-control\"/>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"col-2\">\n" +
        "            <div class=\"form-outline\">\n" +
        "                <label class=\"form-label\" for=\"toAmount\">To Amount</label>\n" +
        "                <input type=\"number\" id=\"toAmount\" class=\"form-control\"/>\n" +
        "            </div>\n" +
        "        </div>";


    if (token) {
        let parsedToken = parseJwt(token);
        if (parsedToken.roles[0] === 'ROLE_ADMIN') {
            document.getElementById('searchParameters').innerHTML += params;
        }
    } else {
        window.location.href = "/login.html";
    }

    $('#search').click(function () {
        document.getElementById('errorMessage').innerHTML = '';
        let token = localStorage.getItem("token");

        if (!token || parseJwt(token).exp < Date.now() / 1000) {
            localStorage.clear();
            window.location.href = "login.html";
        }

        let parsedToken = parseJwt(token);
        let searchParams = "";
        let urlMethod = "search-accountId/";
        let accountId = $('#accountId').val();
        searchParams += "?accountId=" + accountId;

        if (parsedToken.roles[0] === 'ROLE_ADMIN') {
            let initParams = searchParams;
            let fromDate = $('#fromDate').val();
            if (fromDate)
                searchParams += "&fromDate=" + fromDate;

            let toDate = $('#toDate').val();
            if (toDate)
                searchParams += "&toDate=" + toDate;

            let fromAmount = $('#fromAmount').val();
            if (fromAmount)
                searchParams += "&fromAmount=" + fromAmount;

            let toAmount = $('#toAmount').val();
            if (toAmount)
                searchParams += "&toAmount=" + toAmount;
            if (initParams !== searchParams)
                urlMethod = "search/"
        }

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/api/statement/" + urlMethod + searchParams,
            headers: {'Authorization': 'Bearer ' + token},
            success: function (data) {
                let accountType = data.accountType;
                let accountNumber = data.accountNumber;

                let statements = data.statements;

                let htmlFormat = "<caption style=\"border: inherit; background-color: lightgrey;\">\n" +
                    "            <span class=\"align-left\"><strong>Account Type: " + accountType + "" +
                    "<br>Account Number: " + accountNumber + "</strong></span>\n" +
                    "        </caption>";

                if (statements.length !== 0) {
                    htmlFormat += "<table class=\"table table-hover\" style=\"margin-top: 10px;\">\n" +
                        "  <thead>\n" +
                        "    <tr>\n" +
                        "      <th scope=\"col\">Statement ID</th>\n" +
                        "      <th scope=\"col\">Date Field</th>\n" +
                        "      <th scope=\"col\">Amount</th>\n" +
                        "    </tr>\n" +
                        "  </thead>\n" +
                        "  <tbody>\n";

                    for (let st of statements) {
                        htmlFormat +=
                            "    <tr>\n" +
                            "      <td>" + st.id + "</td>\n" +
                            "      <td>" + st.date + "</td>\n" +
                            "      <td>" + st.amount + "</td>\n" +
                            "    </tr>"
                    }
                    htmlFormat += "</tbody></table>";
                } else {
                    htmlFormat += "<h4>There is no data for the last three months !</h4>";
                }
                document.getElementById('statementsData').innerHTML = htmlFormat;
            },
            error: function (data, status, error) {
                let message = "<span  class=\"alert alert-danger\">" + data.responseJSON.message + "</span>";
                if (data.responseJSON.status === "UNAUTHORIZED") {
                    window.location.href = "login.html";
                } else {
                    document.getElementById('errorMessage').innerHTML = message;
                }

            }
        })
    })

    $('#logOut').click(function () {
        let token = localStorage.getItem("token");

        if (parseJwt(token).exp < Date.now() / 1000) {
            localStorage.clear();
            window.location.href = "login.html";
        }

        $.ajax({
            type: "GET",
            url: "http://localhost:8080/logOut",
            headers: {'Authorization': 'Bearer ' + token},
            success: function (data) {
                localStorage.clear();
                window.location.href = "login.html";
            }
        })
    })

})