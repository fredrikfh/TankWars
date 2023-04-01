import express from 'express';
import path from 'path';
import { User } from '../types/User';
import { GameHandler } from './gameHandler';

const app = express();
const port = 80;

const gameHandler = new GameHandler(); // singleton ;)

// const cors = require('cors');
// app.use(cors());

// in testing, we don't want to cache the results
app.set('etag', false);
app.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store');
  next();
});

app.get('/', (req, res) => {
  res.send('Hello, World!');
});

app.listen(port, '0.0.0.0', () => {
  console.log(`Server started on port ${port}`);
});

// establish connection to firebase
const admin = require('firebase-admin');
const serviceAccount = path.join(__dirname, '../..', 'keys', 'fb-key.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

// returns all the user-id's
app.get('/user', async (req, res) => {
  const usersRef = admin.firestore().collection('users');
  const querySnapshot = await usersRef.get();

  if (querySnapshot.empty) {
    res.status(204).send('No users found');
  } else {
    const userids = querySnapshot.docs.map((doc: { id: any }) => doc.id);
    res.status(200).send(userids);
  }
});

// returns data of a specific user
app.get('/user/:idOrUsername', async (req, res) => {
  const usersRef = admin.firestore().collection('users');
  const snapshot = await usersRef.get();
  const searchParam = req.params.idOrUsername;

  const user =
    snapshot.docs.find((doc: { id: string }) => doc.id === searchParam) ||
    (
      await Promise.all(
        snapshot.docs.map(async (doc: { id: string }) => {
          const userSnapshot = await usersRef.doc(doc.id).get();
          return userSnapshot.data().username === searchParam ? userSnapshot : null;
        })
      )
    ).find(Boolean);

  if (user) {
    res.status(200).send(user.data());
  } else {
    res.status(404).send('User not found');
  }
});

// create new user
app.post('/user/create/:username', async (req, res) => {
  const usersRef = admin.firestore().collection('users');
  const username = req.params.username;
  const querySnapshot = await usersRef.where('username', '==', username).get();

  if (!querySnapshot.empty) {
    const user: User[] = querySnapshot.docs.map((doc: any) => {
      if (doc.data().username === username) {
        return { id: doc.id, ...doc.data() };
      }
    });
    res.status(409).send(user[0]);
  } else {
    const newUserRef = await usersRef.add({
      username: req.params.username,
      highscore: 0,
      games: 0,
      wins: 0,
      losses: 0,
    });
    res.status(201).send({
      username: req.params.username,
      highscore: 0,
      games: 0,
      wins: 0,
      losses: 0,
      id: newUserRef.id,
    } as User);
  }
});

// returns users with top 10 highscore
app.get('/highscores', async (req, res) => {
  const usersRef = admin.firestore().collection('users');
  const querySnapshot = await usersRef.orderBy('highscore', 'desc').limit(10).get();

  if (querySnapshot.empty) {
    res.status(204).send('No highscores found');
  } else {
    const users = querySnapshot.docs.map(
      (doc: any) =>
        ({
          id: doc.id,
          ...doc.data(),
        } as User)
    );
    res.status(200).send(users);
  }
});

// join a lobby with specific id
app.post('/lobby/:id', async (req, res) => {
  if (gameHandler.getLobbyById(req.params.id)) {
    const lobby = gameHandler.getLobbyById(req.params.id);

    if (!lobby) {
      res.status(404).send('Lobby not found');
      return;
    }

    lobby.addUser(req.body.userId);
    gameHandler.createGame(lobby); // start game since 2 players are in lobby
    res.status(200).send(lobby.getId());
  } else {
    // if lobby doesn't exist, create a new lobby
    const lobby = gameHandler.createLobby();
    lobby.addUser(req.body.userId);
    res.status(201).send(lobby.getId());
  }
});

// leave a lobby with specific id
app.post('/lobby/:id/leave', async (req, res) => {
  const lobby = gameHandler.getLobbyById(req.params.id);
  lobby?.removeUser(req.body.userId);
  // if lobby is empty, dispose
  if (lobby?.getUsers().length === 0) {
    gameHandler.removeLobby(lobby);
  }
  res.status(200).send(lobby?.getId());
});

// Polling endpoint to check if its your turn (sends username)
app.get('/game/:gameid/currentTurn', async (req, res) => {
  const game = gameHandler.getGameById(req.params.gameid);
  const user = req.body.userName;
  if (game) {
    if (user === game.getCurrentTurnUser().username) {
      // send the gamestate to the client
      res.status(200).send(game.getGameStateJSON());
    } else {
      res.status(200).send('Not your turn');
    }
  } else {
    res.status(404).send('Game not found (or not created yet because lack of opponent)');
  }
});

// endpoint to send a move / gamestate
app.post('/game/:gameid/move', async (req, res) => {
  const game = gameHandler.getGameById(req.params.gameid);
  if (game) {
    game.calculateNextGameState(req.body.newGameState);
    res.status(200).send('Move made');
  } else {
    res.status(404).send('Game not found');
  }
});
