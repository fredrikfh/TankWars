import { Request, Response } from 'express';

export const heartbeat = async (req: Request, res: Response): Promise<void> => {
  res.status(200).send('Server is alive');
};
