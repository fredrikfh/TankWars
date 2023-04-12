import { User } from '../../types/User';
import path from 'path';

export function userGenerator(id: string): User {
  // find user from firestore
  // if user exists, return user
  // if user does not exist, create user and return user

  // establish connection to firebase
  const admin = require('firebase-admin');
  const serviceAccount = path.join(__dirname, '../../', 'keys', 'fb-key.json');

  admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
  });

  return {
    id: id,
    username: 'user' + id,
    highscore: 0,
    games: 0,
    wins: 0,
    losses: 0,
  };
}
