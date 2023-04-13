import { Request, Response, NextFunction } from 'express';
import { log } from '../functions/console';

export function firebaseLogger(req: Request, res: Response, next: NextFunction) {
  const startTime = Date.now();
  const path = req.path;

  res.on('finish', () => {
    const endTime = Date.now();
    const duration = endTime - startTime;
    log('Firebase read/request: ' + path + ', duration: ' + duration + 'ms');
  });

  next();
}
