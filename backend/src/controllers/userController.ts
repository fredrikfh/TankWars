import { Request, Response } from 'express';
import { GameHandler } from '../gameHandler';
import { getUserById } from '../functions/getUserById';
import { User } from '../../types/User';
import admin from '../functions/firebaseAdmin';

const gameHandler = GameHandler.getInstance();

export const users = async (req: Request, res: Response): Promise<void> => {
  // returns all the user-id's
  const usersRef = admin.firestore().collection('users');
  const querySnapshot = await usersRef.get();

  if (querySnapshot.empty) {
    res.status(204).send('No users found');
  } else {
    const userids = querySnapshot.docs.map((doc: { id: any }) => doc.id);
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
};

// delete user
export const deleteUser = async (req: Request, res: Response): Promise<void> => {
  const usersRef = admin.firestore().collection('users');
  const username = req.params.username;
  const querySnapshot = await usersRef.where('username', '==', username).get();
  if (!querySnapshot.empty) {
    // delete user
    const user: User[] = querySnapshot.docs.map((doc: any) => {
      if (doc.data().username === username) {
        return { id: doc.id, ...doc.data() };
      }
    });
    await usersRef.doc(user[0].id).delete();
    res.status(204).send('User deleted');
  } else {
    res.status(404).send('User not found');
  }
};
