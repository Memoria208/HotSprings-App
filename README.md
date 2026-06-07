# HotSprings App

A community-driven full-stack application for discovering and sharing
real-time condition reports on hot springs locations in Idaho and beyond.

## About

Hot springs have a vibrant culture built around community care and shared
responsibility. Many pools are maintained by dedicated visitors who
regularly clean and monitor conditions to keep them safe and enjoyable
for everyone.

This app allows anyone to read and contribute condition updates for hot
springs in the area. Safety is a core focus — hot springs can become
hazardous when water temperatures rise too high or conditions deteriorate.

Users are called **Soakers**. Each Soaker can add hot springs and tag
them with community-sourced condition details such as `safe`, `natural`,
`hike-in`, and more.

## Tech Stack

### Back End
- Java 21
- Spring Boot 3.1.1
- MySQL 8.0
- Hibernate / JPA
- Maven 3.9.16
- Lombok 1.18.34

### Front End
- React (Vite)
- Runs on `http://localhost:5173`

## How to Run Locally

### Prerequisites
- Java 21
- MySQL 8.0
- Maven 3.9+
- Node.js and npm

### Database Setup

Open MySQL and run:

```sql
CREATE DATABASE hot_springs;
CREATE USER 'hot_springs'@'localhost' IDENTIFIED BY 'hot_springs';
GRANT ALL PRIVILEGES ON hot_springs.* TO 'hot_springs'@'localhost';
```

### Running the Back End

```bash
cd C:\Users\tamsl\Wk-18-Final-Project\hot-springs
mvn spring-boot:run
```

App runs on `http://localhost:8080`

### Running the Front End

```bash
cd C:\Users\tamsl\Wk-18-Final-Project\hot-springs-ui
npm run dev
```

App runs on `http://localhost:5173`

## API Endpoints

### Soaker

| Method | Endpoint                       | Description          |
|--------|--------------------------------|----------------------|
| GET    | /hot_spring/soaker             | Get all soakers      |
| GET    | /hot_spring/soaker/{id}        | Get soaker by ID     |
| POST   | /hot_spring/soaker             | Create a soaker      |
| PUT    | /hot_spring/soaker/{id}        | Update a soaker      |
| DELETE | /hot_spring/soaker/{id}        | Delete a soaker      |

### Hot Spring

| Method | Endpoint                                         | Description            |
|--------|--------------------------------------------------|------------------------|
| GET    | /hot_spring/soaker/{id}/hot_spring               | Get all hot springs for a soaker |
| GET    | /hot_spring/soaker/{id}/hot_spring/{id}          | Get hot spring by ID   |
| POST   | /hot_spring/soaker/{id}/hot_spring               | Add a hot spring       |
| PUT    | /hot_spring/soaker/{id}/hot_spring/{id}          | Update a hot spring    |
| DELETE | /hot_spring/soaker/{id}/hot_spring/{id}          | Delete a hot spring    |

### Details

| Method | Endpoint            | Description                        |
|--------|---------------------|------------------------------------|
| GET    | /hot_spring/detail  | Get all available condition detail tags |

## Screenshots

### Get All Soakers
![Get All Soakers](screenshots/GET skinnyDipper.png)

### Get Soaker by ID
![Get Soaker by ID](screenshots/GET skinny_dipper 3.png)

### Get Hot Spring
![Get Hot Spring](screenshots/GET hot_spring 1.png)

### Create Soaker
![Create Soaker](screenshots/POST skinny_dipper 4.png)

### Create Hot Spring
![Create Hot Spring](screenshots/POST hot_spring.png)

### Delete Soaker
![Delete Soaker](screenshots/DELETE skinny_dipper 4.png)

## Known Issues

- The app may become unresponsive after periods of inactivity. If
  requests hang, stop the app with `Ctrl+C` and restart with
  `mvn spring-boot:run`.

## Author

Tammy Thomas — Computer Science Program / College of Western Idaho, Back-End Development Certificate - Idaho State University / Promineotech
