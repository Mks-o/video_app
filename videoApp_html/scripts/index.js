function loadIndex() {
    const url = "http://localhost:8080/authenticated/auth";
    const urlLogin = "http://localhost:8080/registration/login";
    const f = document.getElementById('regform');
    f.addEventListener("submit", function (e) {
        e.preventDefault();
        let username;
        let password;
        username = document.getElementById("username").value;
        password = document.getElementById("password").value;
        let bodyX = JSON.stringify({
            "username": username,
            "password": password
        });
        try {

            fetch(url, {
                method: 'POST',
                body: bodyX,
                headers: {
                    "Cache-Control": "no-cache",
                    "Content-Type": "application/json; charset=UTF-8",
                    "Accept": "*/*",
                    "Accept-Encoding": "gzip, deflate, br",
                    "Connection": "keep-alive",
                }
            }).then((response) => response.json())
                .then((json) => {
                    document.getElementById("result").innerText = "Result: \n" + json["token"];
                    console.log(json)
                    localStorage.setItem("token", document.getElementById("result").innerText.split('\n')[1]);
                });


            fetch(urlLogin, {
                method: 'POST',
                body: bodyX,
                headers: {
                    "Cache-Control": "no-cache",
                    "Content-Type": "application/json; charset=UTF-8",
                    "Accept": "*/*",
                    "Accept-Encoding": "gzip, deflate, br",
                    "Connection": "keep-alive",
                }
            }).then((response2) => response2.json())
                .then((json) => {
                    document.getElementById("result").innerText += "\nResult: \nName: " + json["name"] + "\nRole: " + json["role"];
                    localStorage.setItem("currentUser", json['id']);

                    localStorage.setItem("userDto", JSON.stringify(json));
                    console.log(json);
                    let newloc = "/mainPage.html";
                    window.location.href = newloc;
                });
        }
        catch (err) {
            document.getElementById("result").innerText = "error" + err.message;
        }
    });
}