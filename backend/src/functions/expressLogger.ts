import { log } from './console';

/**
 * Custom middleware to log express requests
 * @param req
 * @param res
 * @param next
 */

export function expressLogger(req: any, res: any, next: any) {
  // Store the original `.send()` method
  const originalSend = res.send;

  // Override the `.send()` method
  res.send = function (body: any) {
    // Log the response
    log(`${req.method} ${req.url} - Response: ${body}`);

    // Call the original `.send()` method with the provided body
    originalSend.call(res, body);
  };

  // Call the next middleware or route handler in the stack
  next();
}
