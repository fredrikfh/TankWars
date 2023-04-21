import { Request, Response, NextFunction } from 'express';
import { isExhausted } from '../functions/firebaseExhaustion';

/**
 * Custom middleware to automatically send a 429 response when firebase is exhausted
 */
export function verifyExhaustion(req: Request, res: Response, next: NextFunction) {
  if (isExhausted()) {
    res
      .status(429)
      .send('Firebase is exhausted for the day :( Please try again tomorrow');
    return;
  }
  next();
}
