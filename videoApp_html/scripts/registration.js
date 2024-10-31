
const urlreg = "http://localhost:8080/registration/";
const url = "http://localhost:8080/authenticated/auth";
const f = document.getElementById('regform2');
f.addEventListener("submit", function (e) {
    e.preventDefault()
    let username;
    let password;
    username = document.getElementById("name").value;
    password = document.getElementById("password").value;
    let userDto= JSON.stringify({
        "id": null,
        "name": document.getElementById("name").value,
        "login": document.getElementById("login").value,
        "mail": document.getElementById("mail").value,
        "role": "USER",
        "birthdate": document.getElementById("birthdate").value,
        "videos":null,
        "comments":null
    });
    
    let bodyX = JSON.stringify({
        "username": username,
        "password": password
    });
    try {
        const response2 = fetch(urlreg + password, {
            method: 'POST',
            body: userDto,
            headers: {
                "Cache-Control": "no-cache",
                "Content-Type": "application/json; charset=UTF-8",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive",
            }
        }).then((response2) => response2.json())
            .then((json) => {
                localStorage.setItem("currentUser", json['id']);
                console.log(json)
            });

        const response = fetch(url, {
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
                window.close();
            });

    }
    catch (err) {
        document.getElementById("result").innerText = "error" + err.message;
    }
});