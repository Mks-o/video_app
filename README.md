Idea was create some app where user will get 2 random videos and rate one of them. 
In app i use swagger and other scary things. It have some security block and can send and 
get requests with Bearer and basic authentication. 
Controller starts on port 8080 and SQL server using standard port 3306. 
At start controller will try to upload database with some values, need network for this. 
Data base load from google youtube. All controller commands available 
in swagger http://localhost:8080/swagger-ui/index.html⁠. 
front web page address to open in browser http://localhost:8081/⁠
docker:
docker pull mksrt/video-app:controller
run with docker compose up
![image](https://github.com/user-attachments/assets/5d657f91-18c0-4d03-a4c0-7a3828532f1a)
