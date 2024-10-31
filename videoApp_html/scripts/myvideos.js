function loadvideos() {
    let token = localStorage.getItem("token");
    const url = "http://localhost:8080/video/get/uservideo/";
    let id = localStorage.getItem("currentUser");
    try {
        const response = fetch(url + id, {
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
                    ownerId: json['ownerId'],
                    comments: json["comments"]
                }));
                let container = document.getElementById("container");
               
                for (let i = 0; i < data.length; i++) {
                    var ifrm = document.createElement("iframe");
                    ifrm.setAttribute("src", data[i].url.replace("watch?v=", "embed/"));
                    ifrm.style.width = "24%";
                    ifrm.style.height = "24%";
                    frameborder="0"
                    ifrm.referrerpolicy ="strict-origin-when-cross-origin";
                    ifrm.allowfullscreen;
                    
                    container.appendChild(ifrm);
                }

            });
    }
    catch (err) {
        prompt("load fail");
    }
}