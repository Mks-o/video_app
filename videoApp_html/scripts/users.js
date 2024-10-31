
function loadcomments() {

    const newData = [];
    try {
        let token = localStorage.getItem("token");
        
        const url = "http://localhost:8080/user/getusers";
        const response = fetch(url, {
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
                    name: json["name"],
                    login: json["login"],
                    mail: json["mail"],
                    role: json["role"],
                    birthdate: json["birthdate"],
                    raiting: json["raiting"],
                    videos: json["videos"],
                    comments: json["comments"]
                }));
                
                let table = document.getElementById("users");
                let tbodytable = document.getElementById("tbodytable");
                for (let i = 0; i < data.length; i++) {
                    tbodytable.innerHTML +=
                        '<tr>' +
                        '<td>' + data[i].login + '</td>' +
                        '<td>' + data[i].mail + '</td>' +
                        '<td>' + data[i].birthdate + '</td>' +
                        '<td>' + data[i].videos.length + '</td>' + '</tr>';

                }
                table.append(tbodytable);
            });
    }
    catch (err) {
    }

}