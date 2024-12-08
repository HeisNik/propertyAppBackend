# Project Name: Real Estate Property App
**Author:** Niko Heiskanen  
**Date:** 7.12.2024    
**Course:** TTC8430-3005 

## Project plan

### Subject

The project is a Real Estate Property Management Application designed to manage property listings, including adding, updating, and deleting properties. It also includes user authentication and authorization.

### Frameworks used

- Java Spring Boot

### Storage Used

- **Database:** PostgreSQL for storing user and property data.
- **Files:** Local file system for storing uploaded images.

### Special Features

- **User Authentication:** Implemented using JWT (JSON Web Token) for secure login and session management.
- **Image Upload:** Allows users to upload images for property listings.
- **Role-Based Access Control:** Ensures that only property owners can update or delete their listings.  

## Project documentation

Frontend can be found at: https://github.com/HeisNik/propertyAppFrontend

### Overall Structure of the App

```mermaid
graph TD;
    A[React] -->|API Calls| B[Spring Boot]
    B -->|Database| C[(PostgreSQL)];
    B -->|File Storage| D[Local File System];
    B -->|Authentication| E[JSON Web Token];
````  
  
- **Frontend (React)**: The user interface that makes API calls to the backend.
- **Backend (Spring Boot)**: Server-side application that handles API calls, manages the database and file storage.
- **Database (PostgreSQL)**: The database that stores user and property information and the location of images in the file system.
- **File Storage (Local File System)**: Local file system where uploaded images are stored.
- **Authentication (JWT)**: User authentication and session management with JWT tags.

### Endpoints

- **POST /auth/register:** Register a new user.
- **POST /auth/login:** Authenticate a user.
- **GET /properties/:** Get all properties. Requested properties can be filtered with query parameters with type, size and price.
- **GET /properties/{id}:** Get single property.
- **POST /properties/add:** Add a new property.
- **PUT /properties/{id}:** Update an existing property.
- **DELETE /properties/{id}:** Delete a property.
- **GET /images/{imageUrl}** Retrieve an image file from the server's file system using the specified image URL.
  
### Time Spent

- **Planning:** 3 hours
- **Development:** 45 hours
- **Documentation:** 4 hours

### Self-Assessment

- **Proposed Grade:** 4/5
  
In general, the project was quite challenging, because I hadn't used Java Spring boot very much.
Even though I have done apis's before, I have never handled files in them before, which made it difficult in the beginning. 
There were also some difficulties with the database models and I had to modify them as the development progressed. 
In the beginning I started to work on a basic crud model from an application where you could create a property without pictures and then I added the possibility to add pictures to the call when creating a property. 
I then implemented authentication in the app and functionality to ensure that only the user who created the property could edit and delete the property.
Java Spring documentation is really comprehensive and good, if something was left unclear, you could find the answer pretty quickly in the documentation. 

- **Successes:** I got all the necessary endpoints done. The database structure works and the relationships are correct. The images can be submitted with a form and are stored in the file system and retrieved as they should be. The application can be logged into and secure routes cannot be accessed without authentication.

- **Lacking features:** There was no time to test the code because of the deadline, but fortunately this can be done later in my own time.


