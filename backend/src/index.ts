import express from 'express';
import path from 'path';
import { User } from '../types/User';

const app = express();
const port = 3000;

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

app.listen(port, () => {
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
  const querySnapshot = await usersRef.where('username', '==', req.params.username).get();

  if (!querySnapshot.empty) {
    res.status(409).send('User already exists');
  } else {
    const newUserRef = await usersRef.add({
      username: req.params.username,
      highscore: 0,
      games: 0,
      wins: 0,
      losses: 0,
    });
    res.status(201).send(newUserRef.id);
  }
});


// returns users with top 10 highscore

app.get('/highscores', async (req, res) => {
  const usersRef = admin.firestore().collection('users');
  const querySnapshot = await usersRef.orderBy('highscore', 'desc').limit(10).get();

  if (querySnapshot.empty) {
    res.status(204).send('No highscores found');
  } else {
    const users = querySnapshot.docs.map((doc: any) => doc.data() as User);
    res.status(200).send(users);
  }
})
