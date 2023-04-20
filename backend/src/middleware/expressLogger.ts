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

  // colorize the request method
  const colorCode = (method: any) => {
    switch (method) {
      case 'GET':
        return '\x1b[32m'; // green
      case 'POST':
        return '\x1b[34m'; // blue
      case 'PUT':
        return '\x1b[33m'; // yellow
      case 'DELETE':
        return '\x1b[31m'; // red
      default:
        return '';
    }
  };

  function sendToLogger(message: string) {
    const methodColor = colorCode(req.method);
    log(`${methodColor}[${req.ip}] ${req.method} ${req.url} - Response: ${message}`);
  }

  // Override the `.send()` method
  res.send = function (body: any): Response {
    const bodyString: string = body.toString();

    if (bodyString.startsWith('{')) {
      sendToLogger('{ Collapsed JSON }');
    } else if (bodyString.includes('<!DOCTYPE html>')) {
      sendToLogger('HTML file');
    } else if (bodyString.includes('window.onload')) {
      sendToLogger('JS file');
    } else if (bodyString.startsWith('[')) {
      sendToLogger('[ Collapsed array ]');
    } else {
      sendToLogger(bodyString);
      // log(`${methodColor}${req.method} ${req.url} - Response: ${body}`);
    }

    // Call the original `.send()` method with the provided body
    return originalSend.call(res, body);
  };

  // Call the next middleware or route handler in the stack
  next();
}
