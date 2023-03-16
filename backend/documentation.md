# API Documentation

This is an API documentation for an Express server that handles users, lobbies, and game state. The server uses Firebase Firestore for database storage.

### Base URL

> http://localhost:3000

### GET /user/

Description: Returns a list of all user IDs
Response:
Status: 200 OK
Body: Array of user IDs

> - 204 No users found (DB is empty)
> - 200 List of user-id's

### GET /user/{id: string}

> - 404 User not found
> - 200 User

### GET /user/{idOrUsername: string}

Returns data for the user with the specified ID or username. If the parameter provided matches an ID, the response will contain data for that user. If the parameter matches a username, the response will contain data for the first user with that username found in the database.

```json
Status: 200 OK
{
  "username": "user1",
  "highscore": 50,
  "games": 10,
  "wins": 5,
  "losses": 5
}
```

> - 404 User not found
> - 200 User

### POST /user/create/{username: string}

Creates a new user with the specified username. The username must be unique. If the username is already taken, the response will contain an error message.

> - 409 User already exists
> - 201 User created

### GET /highscores

Returns an array of the top 10 users with the highest scores, sorted in descending order.

> - 204 No highscores found
> - 200 List of users with highest score

```json
Status: 200 OK
{
  [
    {
        "username": "user1",
        "highscore": 50,
        "games": 10,
        "wins": 5,
        "losses": 5
    },
    {
        "username": "user2",
        "highscore": 40,
        "games": 8,
        "wins": 4,
        "losses": 4
    },
    ...
  ]
}
```

### POST /lobby/:id

Joins a lobby with the specified ID. If the lobby does not exist, a new lobby is created.

```json
POST /lobby/lobby1
Body: { "userId": "user1" }
```

> - 201 Created

### POST /lobby/:id/leave

Leaves a lobby with the specified ID.

Example request:

```json
POST /lobby/lobby1/leave
Body: { "userId": "user1" }
```

### GET /game/:gameid/currentTurn

Checks if it is the specified user's turn in the specified game. Returns the current game state if it is the user's turn.

Example request:

```json
GET /game/game1/currentTurn
Body: { "userName": "user1" }
```

Example response:

```json
Status: 200 OK
{
    "gameId": "12345678",
    "gameStatus": false,
    "currentTurn": 0,
    "users": {
        [
            "User": {
                "id": string;
                "username": string;
                "wins": number;
                "losses": number;
                "highscore": number;
            },
            "IStat": {
                "position": [0,0];
                "turretAngle": 0.5;
                "isMirrored": true;
                "health": 100;
                "ammunition": 100;
                "tankDirection": "left";
                "tankType": "M107";
                "score": 100;
            },
            ...
        ]
    },
}
```

### POST /game/:gameid/move

Updates the game state for the specified game with the new game state provided in the request body.

Example request:

```json
POST /game/123456/move
Body:
{ "newGameState":
    {
        "gameId": "12345678",
        "gameStatus": false,
        "currentTurn": 0,
        "users": {
            [
                "User": {
                    "id": string;
                    "username": string;
                    "wins": number;
                    "losses": number;
                    "highscore": number;
                },
                "IStat": {
                    "position": [0,0];
                    "turretAngle": 0.5;
                    "isMirrored": true;
                    "health": 100;
                    "ammunition": 100;
                    "tankDirection": "left";
                    "tankType": "M107";
                    "score": 100;
                },
                ...
            ]
        },
    }
}
```
