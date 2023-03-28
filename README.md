# Ciklo_beta_v2

## Description
This project is a website for tourists can book cyclo, and go around Hoi An with the purpose is sightseeing the scenery.

## Using tools and Technologies
```
Spring Boot 3.0.2
Spring Security
Spring Websocket
JPA Repository & Hibernate
Microsoft Sql Server in Brand main
PostgreSQL in sub brand (ciklo-psql)
JWT Authenticate and Authorize
Thymeleaf
Jquery, Axios and Ajax
Datatable for table
Bootstrap 5
MailDev
MapBox
```
 ```Now this project can be run by using docker ```

## Screenshoots
![image](https://user-images.githubusercontent.com/83583888/225262037-7f5838b4-0b73-4f57-abfe-53ead584474e.png)
![image](https://user-images.githubusercontent.com/83583888/225262173-114b117e-83a5-4881-a73f-c8741a20e358.png)
![image](https://user-images.githubusercontent.com/83583888/225261704-349ab443-5620-4904-9aed-7d6e9a7a4619.png)

## Requirements
For building and running application:
- [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- [Maven 3.8.5 or latest](https://maven.apache.org/install.html)
- [Tomcat 10](https://tomcat.apache.org/download-10.cgi)

For using docker to run, you should download [docker](https://www.docker.com/)

## Usage
### For using docker, I recommend using docker-compose to run this project
  1. Pull three images is [khvu443/ciklo-website](https://hub.docker.com/r/khvu443/ciklo-website), [postgresql](https://hub.docker.com/_/postgres), [maildev](https://hub.docker.com/r/maildev/maildev)
  ```
  $ docker pull khvu443/ciklo-website
  $ docker pull postgres:15-alpine
  $ docker pull maildev/maildev
  ```
  2. Create a `docker-compose.yml` file, copy the code bellow and patst it to that file
  > for postgresql image, I use tag 15-alpine so if you want to you another tag version you should change the tag of image in docker-compose file to the tag version you use
  ```
      version: "3"
    services:
      web:
        image: khvu443/ciklo-website:latest
        container_name: ciklo-web
        environment:
          SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/ciklo
        ports:
          - "8080:8080"
        restart: always
        depends_on:
          - db
      db:
        image: postgres:15-alpine
        container_name: ciklo-DB
        environment:
          - POSTGRES_USER=lenovo
          - POSTGRES_PASSWORD=binbo123
          - POSTGRES_DB=ciklo
          - PGDATA=/var/lib/postgresql/data/pgdata
        ports:
          - "5432:5432"
        restart: always

      maildev:
        image: maildev/maildev
        ports:
          - "1080:1080"

    networks:
      common-network:
        driver: bridge
    volumes:
      pgdata:
  ```
  3. Using `docker compose up` command to run - this will a minute to run and for stop using `docker compose down`
  
  
  
### Running the application locally
> Now I have two version one is in main using sql server and another one is postgresql. You can choose what version use want to use
> You should install maildev to sending mail and confirm when sign up
  ```
  $ npm install -g maildev
  ```
> With mapbox, You should make account to get a token, then copy and patse token to the file `map.js` in folder `custom\js`
> You must create a database with whatever name you want and go to `application.yml` change the datasource database name, if you have authenticate in SQL Server you may change it to. For postgresql, it is the same as sql server
-----
  Clone this repository and use it locally
   - For sql server   `$ git clone https://github.com/khvu443/Ciklo_beta_v2.git`
   - For Postgresql `$ git clone -b ciklo-psql https://github.com/khvu443/Ciklo_beta_v2.git`
  
  There are many way that you can run application, you can excute the `main` method in the `com.example.ciklo.CikloApplication` or maybe using
  ```
  $ mvn spring-boot:run
  ```
  But you should run command `$ maildev` for using maildev
 
## Login & Sign up
For admin `Email: admin@gmail.com Password: 1234` but it doesn't have sign up so the only way to add new admin is insert data to database
For drivers, in database doesn't have any account and cannot sign up so you should login with account admin to add driver and cyclo first.

For customer, You can sign up but it have confirm email so you should go to this `http://127.0.0.1:1080/`. 

## Roles
- Admin:
  - Can view all bills, drivers, and cyclo
  - Add driver and cyclo
  - Soft delete driver (disable account of driver)
  - Can update information account
- Driver: 
  - Can accept book from customer by click the notification in nav bar from driver page or in homepage 
  `After accept the status driver from free to busy, so I make a auto change for it and the time change to free depend on the time show in form. You should not change to anothe page if the status is not change yet`
  - Change the status of cyclo from `not maintain` to `maintain` by press the icon in navbar from driver page
  - Can update information account
- Customer can book the cyclo, and update the information account

## important !!!
- Map is to small when it appear first so you have to change the size of browser and map is big.
- For using docker postgresql, use command `$ docker exec -it <container name datasbe/id> bash' then `$ psql -U lenovo -d ciklo` and now you can use any command from psql.
