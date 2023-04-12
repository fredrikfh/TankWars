import { Request, Response } from 'express';
import { Lobby } from '../models/Lobby';
import { GameHandler } from '../gameHandler';
import { getUserById } from '../functions/getUserById';
import { User } from '../../types/User';
import admin from '../functions/firebaseAdmin';
import { IGame } from '../interfaces/IGame';

const gameHandler = GameHandler.getInstance();

export const top10 = async (req: Request, res: Response): Promise<void> => {
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
};
