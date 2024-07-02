# Genesis Resources

This project is a Java-based API for managing user resources, specifically designed for the Genesis Resources application. It provides endpoints for creating, updating, retrieving, and deleting user information.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [API Testing Interface](#api-testing-interface)

## Overview

The Genesis Resources API allows you to manage user information through a simple RESTful interface. This includes the ability to create new users, update existing users, retrieve user information, and delete users from the database.

## Features

- Create a new user
- Update an existing user
- Retrieve user details by ID
- Retrieve all users
- Delete a user by ID

## Usage

Once the application is running, you can use the provided HTML interface or any HTTP client (like Postman) to interact with the API.

## API Endpoints

### Create User

- **URL:** `/api/v1/user`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "name": "John",
        "surname": "Doe",
        "personID": "1234567890ab"
    }
    ```
- **Response:**
    ```json
    {
        "id": "1",
        "name": "John",
        "surname": "Doe",
        "personID": "1234567890ab",
        "uuid": "12345678-abcd-1234-abcd-123456abcdef"
    }
    ```

### Update User

- **URL:** `/api/v1/user`
- **Method:** `PUT`
- **Request Body:**
    ```json
    {
        "id": "1",
        "name": "John",
        "surname": "Smith"
    }
    ```
- **Response:**
    ```json
    {
        "id": "1",
        "name": "John",
        "surname": "Smith"
    }
    ```
  
### Get User by ID

- **URL:** `/api/v1/user/`
- **Method:** `GET`
- **Response:**
    ```json
    {
        "id": "1",
        "name": "John",
        "surname": "Smith"
    }
    ```

- **URL:** `/api/v1/user/{id}?detail=true`
- **Method:** `GET`
- **Response:**
    ```json
    {
        "id": "1",
        "name": "John",
        "surname": "Smith",
        "personID": "1234567890ab",
        "uuid": "12345678-abcd-1234-abcd-123456abcdef"
    }
    ```

### Get All Users

- **URL:** `/api/v1/users`
- **Method:** `GET`
- **Response:**
    ```json
    [
        {
            "id": "1",
            "name": "John",
            "surname": "Smith"
        },
        {
            "id": "2",
            "name": "Jane",
            "surname": "Doe"
        }
    ]
    ```

- **URL:** `/api/v1/users?detail=true`
- **Method:** `GET`
- **Response:**
    ```json
    [
        {
            "id": "1",
            "name": "John",
            "surname": "Smith",
            "personID": "1234567890ab",
            "uuid": "12345678-abcd-1234-abcd-123456abcdef"
        },
        {
            "id": "2",
            "name": "Jane",
            "surname": "Doe",
            "personID": "1234567890cd",
            "uuid": "12345678-abcd-1234-abcd-abcdef123456"
        }
    ]
    ```

### Delete User

- **URL:** `/api/v1/user/{id}`
- **Method:** `DELETE`
- **Response:**
    ```json
    "User with ID 1 has been successfully deleted."
    ```

## Configuration

The application uses a MySQL database. The connection settings are defined in the `Settings` class:

```java
public class Settings {
    private static final String DB_HOST = "database_host";
    private static final int DB_PORT = database_port;
    private static final String DB_NAME = "database_name";
    private static final String DB_USER = "database_user";
    private static final String DB_PASSWORD = "database_password";
    
    public static final String PERSON_ID_FILE = "fileWithPersonIdList";
}
```

## API Testing Interface

For simplicity in testing the functionalities of the Genesis Resources API, we have implemented a basic web interface. This allows you to easily and quickly test fundamental API operations, such as creating, updating, fetching, and deleting users.

### How to Use

1. **Create User**: Enter the name, surname, and person ID, then click the "Create User" button.
2. **Update User**: Enter the user ID, new name, and surname, then click the "Update User" button.
3. **Get User by ID**: Enter the user ID and check "Detail" for detailed information, then click the "Get User" button.
4. **Get All Users**: Check "Detail" for detailed information, then click the "Get All Users" button.
5. **Delete User by ID**: Enter the user ID and click the "Delete User" button.

### Note

Ensure that you have the API server running at `http://localhost:8080` before using the testing interface.
