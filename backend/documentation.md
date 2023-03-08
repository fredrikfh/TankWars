# API Documentation

### GET /user/

> - 204 No users found (DB is empty)
> - 200 List of user-id's

### GET /user/{id: string}

> - 404 User not found
> - 200 User

### GET /user/{username: string}

> - 404 User not found
> - 200 User

### POST /user/create/{id}

> - 409 User already exists
> - 201 User created

### GET /highscores
> - 204 No highscores found 
> - 200 List of users with highest score
