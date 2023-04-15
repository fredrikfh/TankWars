import { Request, Response } from 'express';
import { User } from '../../types/User';
import { getTopUsers } from '../functions/firebaseCache';

export const top10 = async (req: Request, res: Response): Promise<void> => {
  const topUsers = await getTopUsers();

  if (topUsers) {
    res.status(200).send(topUsers);
  } else {
    res.status(204).send('No highscores found');
  }
};
