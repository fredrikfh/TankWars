import express from 'express';
import { GameHandler } from './gameHandler';
import { expressLogger } from './functions/expressLogger';
import lobbyRoutes from './routes/lobbyRoutes';
import userRoutes from './routes/userRoutes';
import gameRoutes from './routes/gameRoutes';
import highscoreRoutes from './routes/highscoreRoutes';
import { log } from './functions/console';
import { welcome } from './functions/welcomeScreen';

new GameHandler(); // singleton ;)

// create express app
const app = express();
const port = process.env.NODE_ENV === 'production' ? 80 : 4999;

// middleware
app.use(express.json()); // for parsing application/json
app.use(expressLogger); // for logging

// routes
app.use('/lobby', lobbyRoutes);
app.use('/user', userRoutes);
app.use('/game', gameRoutes);
app.use('/highscore', highscoreRoutes);

// in testing, we don't want to cache the results
app.set('etag', false);
app.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store');
  next();
});

app.listen(port, () => {
  welcome();
  if (process.env.NODE_ENV === 'production') {
    log('PRODUCTION MODE');
  } else {
    log('DEVELOPMENT MODE');
  }
  log(`Server started on port ${port}`);
});
