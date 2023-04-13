import { Request, Response } from 'express';
import { GameHandler } from '../gameHandler';
import { getUserById } from '../functions/getUserById';
import { User } from '../../types/User';
import admin from '../functions/firebaseAdmin';
import { getUsers } from '../functions/firebaseCache';
import { log } from '../functions/console';

const gameHandler = GameHandler.getInstance();

export const users = async (req: Request, res: Response): Promise<void> => {
  const users = await getUsers();

  if (users == null) {
    res.status(204).send('No users found');
  } else {
    const userids = users.docs.map((doc: { id: any }) => doc.id);
    res.status(200).send(userids);
  }
};

// returns data of a specific user
export const getUser = async (req: Request, res: Response): Promise<void> => {
  const user = await getUserById(req.params.idOrUsername);

  if (user) {
    res.status(200).send(user);
  } else {
    res.status(404).send('User not found');
  }
};

// create new user
export const createUser = async (req: Request, res: Response): Promise<void> => {
  const usersRef = admin.firestore().collection('users');
  const username = req.params.username;
  log('firestore: quering for user with username: ' + username);
  const user = await getUserById(username);

  if (user) {
    res.status(409).send(user);
  } else {
    const initialUserData = {
      username: req.params.username,
      highscore: 0,
      games: 0,
      wins: 0,
      losses: 0,
    };
    log('firestore: creating new user with username: ' + username);
    const newUserRef = await usersRef.add(initialUserData);
    res.status(201).send({ id: newUserRef.id, ...initialUserData } as User);
  }
};

// delete user
export const deleteUser = async (req: Request, res: Response): Promise<void> => {
  const usersRef = admin.firestore().collection('users');
  const username = req.params.username;
  log('firestore: quering for user with username: ' + username);
  const querySnapshot = await usersRef.where('username', '==', username).get();
  if (!querySnapshot.empty) {
    // delete user
    const userDoc = querySnapshot.docs[0];
    const user: User = { id: userDoc.id, ...userDoc.data() };
    log(
      'firestore: deleting user with id: ' + user.id + ' and username: ' + user.username
    );
    await usersRef.doc(user.id).delete();
    res.status(204).send('User deleted');
  } else {
    res.status(404).send('User not found');
  }
};
