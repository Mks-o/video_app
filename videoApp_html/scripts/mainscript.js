function loadtop() {
    const videosUrl = [];
    let token = localStorage.getItem("token");
    const url = "http://localhost:8080/video/get/popularvideos";
    try {
        fetch(url, {
            method: 'GET',
            headers: {
                "Authorization": token,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json; charset=UTF-8",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive",
            }
        }).then((response) => response.json())
            .then((json) => {
                console.log(json)
                for (element of json) {
                    let res = element['url'].replace("watch?v=", "embed/");
                    videosUrl.push(res);
                }
                let frames = document.getElementsByClassName("frame");
                frames[0].src = videosUrl[0];
                frames[1].src = videosUrl[1];
                frames[2].src = videosUrl[2];
                frames[3].src = videosUrl[3];
                frames[4].src = videosUrl[4];

            });
    }
    catch (err) {
    }
}
const newData = [];
function loadnewvideos() {
    try {
        let token = localStorage.getItem("token");
        const url = "http://localhost:8080/video/get/randomvideos";
        fetch(url, {
            method: 'GET',
            headers: {
                "Authorization": token,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json; charset=UTF-8",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive",
            }
        }).then((response) => response.json())
            .then((json) => {
                console.log(json)
                const data = json.map(json => ({
                    id: json['id'],
                    title: json["title"],
                    description: json["description"],
                    url: json["url"].replace("watch?v=", "embed/"),
                    raiting: json["raiting"],
                    comments: json["comments"]
                }));
                for (let i = 0; i < data.length; i++) {
                    newData[i] = data[i];
                };
                let frames = document.getElementsByClassName("mainframe");
                frames[0].src = newData[0].url;
                frames[1].src = newData[1].url;
                let labels = document.getElementsByClassName("ratelabel");
                labels[0].innerText = "Raiting: " + newData[0].raiting;
                labels[1].innerText = "Raiting: " + newData[1].raiting;
                let comments = document.getElementsByClassName("videocomments");
                comments[0].innerHTML = "";
                for (var i = 0; i < newData[0].comments.length; i++) {
                    var item = document.createElement('li');
                    item.appendChild(document.createTextNode(newData[0].comments[i].content));
                    comments[0].appendChild(item);
                }
                comments[1].innerHTML = "";
                for (var i = 0; i < newData[1].comments.length; i++) {
                    var item = document.createElement('li');
                    item.appendChild(document.createTextNode(newData[1].comments[i].content));
                    comments[1].appendChild(item);
                }

            });
    }
    catch (err) {
    }
}

function rateHandler(x) {
    try {
        let raiting;
        let token = localStorage.getItem("token");
        const url = "http://localhost:8080/video/update";
        
        let bodyX = JSON.stringify({
            "id": newData[x].id,
            "title": newData[x].title,
            "description": newData[x].description,
            "raiting": newData[x].raiting = newData[x].raiting + 1,
            "comments": newData[x].comments == "" ? null : newData[x].comments
        });
        fetch(url, {
            method: 'POST',
            body: bodyX,
            headers: {
                "Authorization": token,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive"
            }
        }).then((response) => response.json())
            .then((json) => {
                console.log(json)
                raiting = json['raiting'];
                let frames = document.getElementsByClassName("ratelabel");
                frames[x].innerText = "Raiting: " + raiting;
                loadnewvideos();

            });

        let userDto = JSON.parse(localStorage.getItem("userDto"));
        let raite = Number(document.getElementById("userRate").innerText);
        const url2 = "http://localhost:8080/user/updateuser";
        let bodyD = JSON.stringify({
            "id": userDto.id,
            "name": userDto.name,
            "login": userDto.login,
            "mail": userDto.mail,
            "role": userDto.role,
            "birthdate": userDto.birthdate,
            "raiting": raite + 1,
            "videos": userDto.videos == "" ? null||userDto.videos == ""  : userDto.videos,
            "comments": userDto.comments == null||userDto.comments == "" ? null : userDto.comments
        });
        fetch(url2, {
            method: 'POST',
            body: bodyD,
            headers: {
                "Authorization": token,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive"
            }
        }).then((response2) => response2.json())
            .then((json) => {
                console.log(json);
                document.getElementById("userRate").innerText = json["raiting"];
            });
    }
    catch (err) {
        prompt("error");
    }

}
function addComment(x) {
    try {
        let raiting;
        let token = localStorage.getItem("token");
        const url = "http://localhost:8080/video/addcomment";
        
        let comment = prompt("Please enter your comment", "Your comment");
        if(comment==null)return;
        let bodyX = JSON.stringify({
            "id": null,
            "content": comment,
            "ownerId": localStorage.getItem('currentUser'),
            "videoId": newData[x].id,
        });
        fetch(url, {
            method: 'POST',
            body: bodyX,
            headers: {
                "Authorization": token,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive"
            }
        });
        let comments = document.getElementsByClassName("videocomments");
        var item = document.createElement('li');
        item.appendChild(document.createTextNode(comment));
        comments[x].appendChild(item);
    }
    catch (err) {
        prompt(localStorage.getItem("ids"));
    }

}
function deleteAccount() {
    let userDto = JSON.parse(localStorage.getItem("userDto"));
    // let token = localStorage.getItem("token");
    const url = "http://localhost:8080/user/delete/" + userDto.id;
    let pass = prompt("Enter your password");
    // let bodyD = JSON.stringify({

    //     "username": userDto.login,
    //     "password": pass
    // });
    let auth = window.btoa(userDto.login + ':' + pass);
    try {
        fetch(url, {
            method: 'DELETE',
            headers: {
                "Authorization": auth,
                "Cache-Control": "no-cache",
                "Content-Type": "application/json",
                "Accept": "*/*",
                "Accept-Encoding": "gzip, deflate, br",
                "Connection": "keep-alive"
            }
        }).then((response) => response.json())
            .then((json) => {
                console.log(json);
                let newloc = "/index.html";
                window.location.replace(newloc);
            });
    }
    catch (err) {
        prompt(err);
    }
}