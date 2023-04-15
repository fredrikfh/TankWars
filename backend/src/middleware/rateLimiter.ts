const rateLimit = require('express-rate-limit');

// Configure rate limiter
export const rateLimiter = rateLimit({
  windowMs: 60 * 1000, // 1 minute in milliseconds
  max: 15, // Limit each IP to 1000 requests per windowMs
  statusCode: 429, // HTTP status code to send when rate limit is exceeded (Too Many Requests)
  message: 'Too many requests, please try again later.',
});
