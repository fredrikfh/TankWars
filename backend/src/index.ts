import express from 'express';
import { GameHandler } from './gameHandler';
import { expressLogger } from './middleware/expressLogger';
import lobbyRoutes from './routes/lobbyRoutes';
import userRoutes from './routes/userRoutes';
import gameRoutes from './routes/gameRoutes';
import highscoreRoutes from './routes/highscoreRoutes';
import serverRoutes from './routes/serverRoutes';
import { log } from './functions/console';
import { welcome } from './functions/welcomeScreen';
import { disposeInactiveGames } from './functions/disposeInactiveGames';
import swaggerJsdoc from 'swagger-jsdoc';
import path from 'path';
import { firebaseLogger } from './middleware/firebaseLogger';

new GameHandler(); // singleton ;)

// create express app
const app = express();
const port = process.env.NODE_ENV === 'production' ? 80 : 4999;

// middleware
app.use(express.json()); // for parsing application/json
app.use(expressLogger); // for request logging

// routes
app.use('/lobby', lobbyRoutes);
app.use('/user', userRoutes);
app.use('/game', gameRoutes);
app.use('/highscore', highscoreRoutes);
app.use('/server', serverRoutes);

// Cache-Control
app.set('etag', false);
app.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store');
  next();
});

// API Documentation
const swaggerUi = require('swagger-ui-express');
const swaggerJSDoc = require('swagger-jsdoc');
const swaggerOptions: swaggerJsdoc.Options = {
  definition: {
    openapi: '3.0.0',
    info: {
      title: 'TankWars API Documentation',
      version: '1.0.1',
      description:
        'The API lets clients create lobbies and send gamestates between users',
    },
    servers: [
      {
        url: 'http://localhost:4999',
      },
      {
        url: 'http://10.212.26.72:80',
      },
    ],
  },
  apis: ['./src/routes/*.ts', './src/controllers/*.ts'],
};

const swaggerDocs = swaggerJsdoc(swaggerOptions);
app.use('/', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

setInterval(() => {
  disposeInactiveGames();
}, 60 * 1000);

app.listen(port, () => {
  welcome();
  if (process.env.NODE_ENV === 'production') {
    log('PRODUCTION MODE');
  } else {
    log('DEVELOPMENT MODE', 'warning');
  }
  log(`Server started on port ${port}`, 'warning');
});
