# HotSprings App

A community-driven REST API for discovering and sharing real-time condition reports on hot springs locations in Idaho and beyond.

## About

Hot springs have a vibrant culture built around community care and shared responsibility. Many pools are maintained by dedicated visitors who regularly clean and monitor conditions to keep them safe and enjoyable for everyone.

This API allows anyone to read and contribute condition updates for hot springs in the area. Safety is a core focus — hot springs can become hazardous when water temperatures rise too high or conditions deteriorate.

## Tech Stack

- Java 25
- Spring Boot 3.1.1
- MySQL 8.0
- Hibernate / JPA
- Maven

## How to Run Locally

### Prerequisites
- Java 25
- MySQL 8.0
- Maven 3.9+

### Database Setup
1. Open MySQL and run:
```sql
CREATE DATABASE hot_springs;
CREATE USER 'hot_springs'@'localhost' IDENTIFIED BY 'hot_springs';
GRANT ALL PRIVILEGES ON hot_springs.* TO 'hot_springs'@'localhost';
```

### Running the App
App runs on `http://localhost:8080`

## API Endpoints

### Skinny Dipper
| Method | Endpoint                       | Description             |
|--------|--------------------------------|-------------------------|
| GET    | /hot_spring/skinny_dipper      | Get all skinny dippers  |
| GET    | /hot_spring/skinny_dipper/{id} | Get skinny dipper by ID |
| POST   | /hot_spring/skinny_dipper      | Create a skinny dipper  |
| PUT    | /hot_spring/skinny_dipper/{id} | Update a skinny dipper  |
| DELETE | /hot_spring/skinny_dipper/{id} | Delete a skinny dipper  |

### Hot Spring
| Method | Endpoint                                       | Description          |
|--------|------------------------------------------------|----------------------|
| GET    | /hot_spring/skinny_dipper/{id}/hot_spring/{id} | Get hot spring by ID |
| POST   | /hot_spring/skinny_dipper/{id}/hot_spring      | Add a hot spring     |
| PUT    | /hot_spring/skinny_dipper/{id}/hot_spring/{id} | Update a hot spring  |

## Screenshots

### Get All Skinny Dippers
![Get All Skinny Dippers](screenshots/GET skinnyDipper.png)

### Get Skinny Dipper by ID
![Get by ID](screenshots/GET skinny_dipper 3.png)

### Get Hot Spring
![Get Hot Spring](screenshots/GET hot_spring 1.png)

### Create Skinny Dipper
![Create Skinny Dipper](screenshots/POST skinny_dipper 4.png)

### Create Hot Spring
![Create Hot Spring](screenshots/POST hot_spring.png)

### Delete Skinny Dipper
![Delete Skinny Dipper](screenshots/DELETE skinny_dipper 4.png)

## Known Issues

- The app may become unresponsive after periods of inactivity. If requests hang, stop the app with Ctrl+C and restart with `mvn spring-boot:run`.

## Author

Tammy Thomas — Back-End Development Certificate, Idaho State University / Promineotech
