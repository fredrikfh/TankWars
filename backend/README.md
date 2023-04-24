# Backend

The backend handles ongoing lobbies and games. Also communicates with the Firebase server connected to the FireStore database for persistance.
The backend uses Node.js and is written in TypeScript with Express for routing.

## Documentation

The API documentation is available at the root of the backend (http://10.212.26.72:80 or http://localhost:4999/). The documentation is generated with Swagger.

<img src="backend/assets/apidoc.png" width="400" alt="API-docs">

## Setup

To access database you must add a the file `fb-key.json` with the Firebase-key to the following directory: `backend/keys/`. To gain access to a Firebase-key, contact the team.

## How to run the backend

Run the following commands in your terminal from the backend directory:

- `yarn` download dependencies
- `yarn start:local` start server in development mode on localhost:4999
- `yarn start:prod` start server in production mode on {machine_ip}:80

## CD: Deployment to virtual machine

The backend is automatically deployed to a virtual machine when a commit is pushed to the main branch. The deployment is done by a Gitlab CI server that connects to the VM with SSH and runs the `vm-startup.sh` script.

The VM is hosted on NTNU (requires VPN) and can be accessed on the following address:
IP: 10.212.26.72
Port: 80

The VM utilizes Nodemon to automatically restart the server if a crash occurs.

## CI: Testing

The backend is automatically tested by a Gitlab CI server for each commit.
Formatter: `yarn prettier`
e2e tests: `yarn test`

## Restrictions

The backend will only allow 30 requests per minute from the same IP address. This is to prevent abuse of the API..


# Backend repository structure
Under follows a listing of the files in the backend/src directory, each file with a short description.

[[_TOC_]]

## controllers
Controller part of the MVC pattern. Each file contains a collection of functions that process the requests coming in through the endpoints in the Routes module, and returns a response.

### gameController.ts
Handles requests related to an ongoing game. 
- The `gameId` function returns the game ID of the game in the provided lobby.
- The `move` function receives an updated game state. If the game ID is valid, it is checked if the game is finished, the new game state is calculated, and the new game state is returned. Other responses are returned if anything goes wrong or the game is finished.
- The `currentTurn` function checks if it is the provided user's turn or not, and returns the game state if it is.
- The `getTerrain` function returns the terrain seed of the game.

### highscoreController.ts
Handles requests related to highscores in stored in the database.
- The `top10` function returns the ten users with the greatest highscores in descending order.

### lobbyController.ts
Handles requests related to lobbies.
- The `leaveLobby` function removes the provided user from the provided lobby. Disposes the lobby if it is empty.
- The `joinLobby` function adds the provided user to the lobby with the provided lobby ID (lobby IDs are the same as gamepins) if the lobby is not full. Creates a new lobby with the provided lobby ID if it does not exist.

### serverController.ts
Handles requests that do not belong to a particular category.
- The `heartbeat` function simply returns a string to show that the server is alive and working.

### userController.ts
Handles requests related to users.
- The `users` function returns the IDs of all users in the database.
- The `getUser` function returns the user with the provided username.
- The `createUser` function creates and stores a new user in the database with the provided username. Returns the user.
- The `deleteUser` function deletes the user with the provided username from the database if the user exists.
- The `updateHighscore` function updates the number of games, losses and wins and the highscore of the provided user. The new score data comes from the provided user object.


## functions
Collection of helper functions used in various parts of the backend.

### checkProjectileHit.ts
Calculates the trajectory of a bullet given its start angle, position and speed, and returns whether it hit the opponent tank or not.

### console.ts
Functions for logging to the console.
- The `log` function prints a log entry to the console. The entry has a color, the time it was made, the file name of the component logging the file, and the message.
- The `slopePreview` function logs the form of the terrain given the seed array.

### disposeInactiveGames.ts
Checks all games, and uses their `lastActionTimeStamp` field to see if they have been inactive long enough to be disposed. If so, both the lobby and the game is removed from the gameHandler.

### firebaseAdmin.ts
Establishes contact with the Firebase server using the firebase key from the `fb-key.json` file.

### firebaseCache.ts
Has methods for retrieving data from the FireStore database. Searches the node-cache before querying the database.
- The `getUsers` function fetches all users.
- The `getUsersIds` function fetches all user IDs.
- The `getTopUsers` function fetches ten users by descending highscore.
- The `retrieveFromCache` function searches the node-cache. Returns the result on cache-hit. Queries the FireStore database on cache-miss.
- The `sendFirestoreRequest` function queries the FireStore database. Sets the `exhausted` field of `firebaseExhausted.ts` if Firebase is exhausted.

### firebaseExhausted.ts
Keeps track of if Firebase is exhaused or not. Resets the `exhausted` field to false every night. Notifies the team through Slack when the exhausted field changes.

### getUserById.ts
Fetches a user by its ID or its username. Starts by searching the cache. Queries the Firestore database on cache-miss.

### TerrainGenerator.ts
Methods for either generating the terrain's seed with random numbers or with the `simplex-noise` module that gives randomized values and interpolates between them.

### welcomeScreen.ts
Prints ASCII art to the console.


## interfaces
Interfaces corresponding to the classes in the models directory.

### IGame.ts
Interface specifying the fields and methods of the Game class

### ILobby.ts
Interface specifying the fields and methods of the Lobby class

### IStats.ts
Interface specifying the fields and methods of the Stats class

### ITerrain.ts
Interface specifying the fields and methods of the Terrain class


## middleware

### expressLogger.ts
Overrides the `res.send` command that sends the HTTP response. Logs the request IP address, the request method, the request url and the response message before sending the response. Colors the log text depending on the request method.

### firebaseLogger.ts
Logs the request path and the processing duration of the request sent to the Firebase server to retrieve data from the database.

### rateLimiter.ts
Configuration for the `express-rate-limit` middleware that returns an error response with status code 429 to an IP address that has exceeded 30 requests within 1 minute. Attempts to avoid exhausting Firebase.

### verifyExhaustion.ts
Returns error response with status code 429 if Firebase is exhausted - meaning that it has received 50 000 requests in a day.

## models
Model part of the MVC pattern, containing classes that manages and stores the internal state of the server.

### Game.ts
Class representing an ongoing game. Generates a random game ID and keeps track of if the game is finished, the terrain seed, whose turn it is, the time of the last move, the lobby to which the game belongs and an array containing pairs of User objects and Stats objects. Has methods for calculating the next game state given a new Stats object, and for returning a JSON representation of the game state.

### Lobby.ts
Class representing a lobby. Has a list of User objects that have joined the lobby, a belonging Game object if a game has been started, a lobby ID and a the ID of the belonging game. Has methods for adding and removing users, setting IDs and adding a game.

### Stats.ts
Class representing the stats of a user's tank. Keeps track of a tank's x-position, the angle of the turret, its health, the power of the shot, the available ammunition and the score. Has getters and setters and a method for calculating the score.

### Terrain.ts
Class for generating a seed for the terrain to make both users have the same terrain when playing. The seed is an array of the y-values of the terrain and will be used to create the terrain using cubic splines on the frontend.


## routes
Each file uses the `express.Router()` service to determine the API endpoints of the server. It specifies the HTTP method, the url and the controller method for each endpoint. Additionally, each endpoint has a javadoc that documents the endpoint for `swagger` to show in the browser at the root, /, endpoint.

### gameRoutes.ts
All endpoints related to an ongoing game, each starting with /game:
- GET /game/:lobbyId calling `gameController.gameId`
- POST /game/:gameid/move calling `gameController.move`
- POST /game/:gameid/gameState calling `gameController.currentTurn`
- GET /game/:gameid/terrain calling `gameController.getTerrain`

### highscoreRoutes.ts
All endpoints related to scores, each starting with /highscore:
- GET /highscore/top10 calling `highscoreController.top10`

### lobbyRoutes.ts
All endpoints related to lobbies, each starting with /lobby:
- POST /lobby/:id/leave calling `lobbyController.leaveLobby`
- POST /lobby/:id/join calling `lobbyController.joinLobby`

### serverRoutes.ts
General endpoints not pertaining to a particular category, each starting with /server:
- GET /server/heartbeat calling `serverController.heartbeat`

### userRoutes.ts
All endpoints related to users, each starting with /user:
- GET /user/ calling `userController.users`
- GET /user/:username calling `userController.getUser`
- POST /user/:username calling `userController.createUser`
- DELETE /user/:username calling `userController.deleteUser`
- PUT /user/:username/highscore calling `userController.updateHighscore`

## schemas
Each file ending with "Schema.ts", set up a JSON schema using the third-party tool `@hapi/joi`. This restricts the request and response bodies to follow the format of the given schemas, and ensures that correctly formated data is sent and received.

### gameSchema.ts
Defines shema for JSON data related to the Game class

### lobbySchema.ts
Defines shema for JSON data related to the Lobby class

### statsSchema.ts
Defines shema for JSON data related to the Stats class

### terrainSchema.ts
Defines shema for JSON data related to the Terrain class

### userSchema.ts
Defines shema for JSON data related to the User interface.

### validate.ts
Function for validating JSON data up against a given schema. Returns true if valid, false otherwise and logs validation failures.

## gameHandler.ts
A singleton that keeps track of active lobbies and games. Has methods for creating, adding, removing and getting lobbies and games.

## index.ts
Entry point to the backend. Sets up the express server and adds routes and middleware. Configures `swagger` to add API documentation to the root endpoint /. Sets time interval for disposing inactive games. Allows for running the server in production mode and development mode according to the start script.

## ../types/User.ts
Interface representing a user. Should have been placed in the interfaces folder.
