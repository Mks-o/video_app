function loadcomments() {

    const newData = [];
    try {
        let token = localStorage.getItem("token");

        const url = "http://localhost:8080/video/get/videos";
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
                    title: json["title"],
                    description: json["description"],
                    url: json["url"],
                    raiting: json["raiting"],
                    ownerId: json["ownerId"],
                    comments: json["comments"]
                }));

                let table = document.getElementById("videos");
                let tbodytable = document.getElementById("tbodytable");
                for (let i = 0; i < data.length; i++) {
                    tbodytable.innerHTML +=
                        '<tr>' +
                        '<td>' + data[i].title + '</td>' +
                        '<td>' + data[i].raiting + '</td>' +
                        '<td>' + data[i].comments.length + '</td>' + '</tr>';

                }
                table.append(tbodytable);

            });
    }
    catch (err) {
    }

}