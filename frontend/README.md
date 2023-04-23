# Frontend

The frontend handles the game-related controllers, models and views used to display and run the game client-side. 
It is written in Java an utilizes the Libgdx-framework. 
The Box2D-framework is used as the 2D-physics-engine. 
Scene2D is used to display menu graphics and game HUD.

## Frontend repository structure

The frontend repo is structured into models, views and controllers, with the exception of a few helper classes.

## Helper classes

### Callback
Interface with methods "onResult" and "onFailed". Passed to the HTTPRequestHandler class to control what should happen when an HTTP request successfully returns or fails.

### CollisionDetection
Implements the Box2D ContactListener that listens for collisions between Box2D bodies such as the tank and a bullet or the terrain and a bullet. Updates the collision field of the bodies involved in the collision.

### ConfigReader
Has static methods for fetching properties from the "config.properties" file containing information about the backend protocol, domain and port, as well as app name and version.

### HTTPRequestHandler
Implements the LibGDX Net.HttpResponseListener interface. It takes a Net.HttpRequest with url, method and content, and a Callback object that defines how the response should be handled. Has a built-in retry method that retries a failed request five times with an increasing back-off period between each retry attempt. Has a method for sending the request to the backend.

### ResourceManager
Singleton that manages graphic and audio resources. Loads sounds files, skins JSON files and texture atlases in a controlled manner and provides getters for specific resources. Provides clear method to unload all currently loaded files.

### TankWarsGame
Gateway to the application. Extends the LibGdx Game place enabling it to manage screens and screen tranisitons for classes that implement the LibGDX Screen interface. Sets the first screen as a LoginScreen. Calls the show method of a new screen and the hide method of an old screen. The Game class extends the LibGDX ApplicationAdapter.

## Models

### Box2dWorld
Provides the Box2D world within which the game objects, called "bodies", live through a static method, ensuring that only one world instance exists. Handles the disposal of game bodies.

### Bullet
Represents a bullet fired by a tank. A body in the world that is affected by gravity and can collide with tanks and the terrain. Has a sprite and its initial velocity and angle are controlled from outside.

### CurrentUser
A singleton that holds a User object, the game ID and the lobby ID. Ensures that only one user is logged in at any point, and makes the user accessible to all frontend classes. The game ID and lobby ID are null if no lobby or game has been joined. The user is null if no user is logged in at the moment.

### FixtureData
Provides a standard way of identification and collision monitoring for the Box2D bodies. Created for each body in their constructors with an id and collision set to false.

### MenuButton
Unused - replaced by scene2D TextButton

### MenuHeader
Unused - replaced by scene2D Table.background

### GameState
Eases JSON serialization and deserialization of the game state data during gameplay. Contains the game ID, the current turn, if the game is finished or not, the two users and their tanks's stats. When a player makes a move, the GameState object is serialized and sent to the backend. Then, the opponent fetches the new game state from the backend and deserializes it into a GameState object.

### Stats
Eases JSON serialization and deserialization of the stas of a tank, such as its position, turret angle, health and the power of the shot. The Stats class is used in a GameState object. 

### Tank
Represents a tank in the game. Contains two Box2D bodies, one for the tank body and one for the turret. The whole tank can move along the terrain until its fuel is exhausted, while the turret follows the tank body and can rotate to set the initial angle of the bullet to be fired. The Tank class has methods both for manual control that are called through input listeners connected to the hud, and for automatic animation of movement, turret rotation and firing.

### Terrain
Initiates the TerrainGenerator and makes the terrain into a Box2d body with which other bodies can collide. Provides a method for drawing the terrain base on an array of 1000 (x, y) vertices, and a getter for the vertices.

### TerrainGenerator
Generates an array of 1000 (x, y) vertices from a seed array using cubic splines. The array represents the terrain.

### User
Represents a user containing the username, amount of games played, amount of losses, amount of wins and highscore. Eases JSON serialization and deserialization of users when communicating with the backend over HTTP.

### UserTank
Simply contains a user object and a corresponding Stats object. Only purpose is to ease JSON serialization and deserialization. Used by the GameState object.

## Views
All view classes implement the LibGDX Screen interface.

### FindGameScreen
Uses Scene2D and the component library scene2D.ui to show a screen with background images, a header with logo and the title "Find Game", an arrow button to go back to main menu, an input field for the gamepin and a button with the text "Join lobby". Also provides methods for showing and hiding a white popup that with a "Cancel" button and text saying "Awaiting opponent" and showing the lobby's gamepin.

### GameHud
Uses Scene2D and scene2d.ui to set up the hud with health bars, movement arrows, turret rotation arrows, fuel bar, power slider, fire button and quit game button. Provides getters for the GameController, so that it can get the values of the sliders and buttons.

### GameOverScreen
Unused and empty class for an unimplemented game over screen.

### GameScreen
Initiates the TerrainController, the GameHud and the GameController. Renders the terrain, the tanks, the bullets and the hud. Provides various banners for informing about turns and win or loss. Calls relevant methods of Box2dWorld and GameController that need to run every render cycle, such as the method for disposing Box2D bodies, and the methods for checking if the tanks have been hit and to animate the move of the opponent tank.

### LeaderboardScreen
Uses Scene2D and scene2D.ui to show a screen with a background image, a header with logo, the title "Leaderboard" and an arrow button leading back to main menu, and a table showing users and their number of wins. The table is a Scene2D ScrollPane, meaning that the all results can be reached by scrolling.

### LoginScreen
Uses Scene2D and scene2d.ui to show a screen with background images, a header with logo and the title "Log in", a text field for the username and a button saying "Log in".

### MainMenuScreen
Uses Scene2D and scene2d.ui to show a screen with a background image, a header with logo and the title "Main Menu", and the four buttons "Find Game", "Leaderboard", "Settings" and "Log out".

## Controllers

### FindGameController
Sets input listeners for the gamepin text field and the "Join lobby" button. A filter is set for the gamepin field so that it only accepts numbers, and the screen will move slightly vertically to make space for the on-screen keyboard. Sends three different HTTP requests to server. The first request to /lobby/:id/join makes the user join the lobby with the gamepin in the text field, which makes the FindGameScreen show the "Awaiting opponent..." popup. The second request to /lobby/:id/leave is fired when the "Cancel" button of the popup is pressed, and the user leaves the lobby they had joined. The popup is then hidden. The last request polls the /game/gameId endpoint, so that it will be notified and receive the game ID when a second player joins the same lobby.

### GameController
A quite big class that controls most parts of the gameplay. Instantiates the two tanks, sets the input listeners for the hud elements, initializes game state, provides methods for disposing bullets, animating the opponent tank, check if it is game over and starting a new turn, sends requests. One request is made to /game/:gameId/gameState that polls to check whose turn it is and to receive the game state when it is their turn. This activates the animation of the opponent tank, which must finish before a move can be made. Another request is sent to /game/:gameId/move, which sends the updated game state to the server and disables the hud input listeners until it is their turn again. The third request goes to /user/:username/highscore, where the updated score is sent to the server to be recorded in the database when the game has ended. The last request is sent to /lobby/:lobbyId/leave when the leave game button is pressed, to transition back to main menu and tell the server that the user has left the game.

### LeaderboardController
Sets the input listener for the arrow button to make it transition back to MainMenuScreen. Sends a request to /highscore/top10 to fetch the top 10 users with the highest number of wins and updates the table in LeaderboardScreen with the users and wins.

### LoginController
Sets input listeners for the input field and the "Log in" button. The screen moves slightly vertically when the on-screen keyboard appears and disappears to make space for it. The "Log in" button sends a request to /user/:username, which creates a new user if it does not exist, and simply returns the user if it exits. It also sets the user field in the CurrentUser class and transitions to MainMenuScreen.

### MainMenuController
Sets input listeners for the four buttons. The "Find Game" button transitions to the FindGameScreen, the "Leaderboard" button to the "LeaderboardScreen and the "Log out" button to the LoginScreen. The "Settings" button is not yet implemented.

### TerrainController
Sets the terrain of the GameScreen, using a seed array from a json file. Also has an unused fetch method that sends a request to /game/:gameID/terrain to get a seed array from the server. The fetch method has not been used in the GameController because of time-constraints, even though the endpoint for it works as expected.
