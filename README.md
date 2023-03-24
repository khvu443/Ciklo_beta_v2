# Ciklo_beta_v2

## Description
This project is a website for tourists can book cyclo, and go around Hoi An with the purpose is sightseeing the scenery.

## Using tools and Technologies
```
Spring Boot 3.0.2
Spring Security
Spring Websocket
JPA Repository & Hibernate
Microsoft Sql Server
JWT Authenticate and Authorize
Thymeleaf
Jquery, Axios and Ajax
Datatable for table
Bootstrap 5
MailDev
MapBox
```
## Screenshoots
![image](https://user-images.githubusercontent.com/83583888/225262037-7f5838b4-0b73-4f57-abfe-53ead584474e.png)
![image](https://user-images.githubusercontent.com/83583888/225262173-114b117e-83a5-4881-a73f-c8741a20e358.png)
![image](https://user-images.githubusercontent.com/83583888/225261704-349ab443-5620-4904-9aed-7d6e9a7a4619.png)

## Requirements
For building and running application:
- [JDK 18](https://www.oracle.com/java/technologies/javase/jdk18-archive-downloads.html)
- [Maven 3.8.5 or latest](https://maven.apache.org/install.html)
- [Tomcat 10](https://tomcat.apache.org/download-10.cgi)
## Installations
- For using maildev to accept sign up in application
```
$ npm install -g maildev
$ maildev
```
- With mapbox, You should make account to get a token, then copy and patse token to the file `map.js` in folder `custom\js`

## Running the application locally
First thing you should do is create database with whatever name you want and go to `application.yml`, if you use authenticate in SQL Server you should copy and paste it

You can clone this repository and use it locally
```
git clone https://github.com/khvu443/Ciklo_beta_v2.git
```

There are many way that you can run application, you can excute the `main` method in the `com.example.ciklo.CikloApplication` or maybe using
```
mvn spring-boot:run
```
###### Login & Sign up
- For admins and drivers, in database doesn't have and cannot sign up so you should add it in Database first
- For customer, You can sign up but it have confirm email so you should go to this `http://127.0.0.1:1080/`
###### Roles
- Admin:
  - Can view all bills, drivers, and cyclo
  - Add driver and cyclo
  - Soft delete driver
- Driver: 
  - Can accept the bill by click the notification 
  - Change the status of cyclo from `not maintain` to `maintain` by press the icon in navbar
  - Can change info of myself
- Customer can book the cyclo, and change the info of myself

## important !!!
- When customer book the trip, you should enter it twice so that the beginning and destination can appear in form (This is a bug that I haven't found out the way to fix it).
- Map is to small when it appear first so you can change the size of browser and map is big.
- The front-end is the template that I had found in the internet for a long time ago.
