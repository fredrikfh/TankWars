import { log } from '../functions/console';
import { Request, Response, NextFunction } from 'express';

/**
 * Custom middleware to log express requests
 * @param req
 * @param res
 * @param next
 */

export function expressLogger(req: Request, res: Response, next: NextFunction) {
  // Store the original `.send()` method
  const originalSend = res.send;

  // Override the `.send()` method
  res.send = function (body: any): Response {
    const bodyString: string = body.toString();
    if (bodyString.includes('<!DOCTYPE html>') || bodyString.includes('window.onload')) {
      // dont print html/js files
      log(`${req.method} ${req.url} - Response: HTML / JS file`);
    } else {
      log(`${req.method} ${req.url} - Response: ${body}`);
    }

    // Call the original `.send()` method with the provided body
    return originalSend.call(res, body);
  };

  // Call the next middleware or route handler in the stack
  next();
}
