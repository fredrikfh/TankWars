import { Request, Response } from 'express';
import { GameHandler } from '../gameHandler';
import { gameSchema } from '../schemas/gameSchema';
import { validateSchema } from '../schemas/validate';
import { IStats } from '../interfaces/IStats';

const gameHandler = GameHandler.getInstance();

// endpoint to get current gameId of a lobby
export const gameId = async (req: Request, res: Response): Promise<void> => {
  const lobby = gameHandler.getLobbyById(parseInt(req.params.lobbyId));
  if (lobby?.getGameId()) {
    res.status(200).send(lobby.getGameId());
  } else {
    res.status(404).send('Lobby does not have ongoing game');
  }
};

export const move = async (req: Request, res: Response): Promise<void> => {
  const game = gameHandler.getGameById(req.params.gameid);

  if (game) {
    if (!validateSchema(gameSchema, req.body)) {
      res.status(400).send('Invalid request body');
      return;
    }

    if (game.isFinished) {
      res.status(400).send('Game is finished');
      return;
    }

    // only the users stats element in the request body is needed to calculate the next game state
    const newStats: IStats[] = [req.body.users[0].stats, req.body.users[1].stats];
    if (game.calculateNextGameState(newStats)) {
      res.status(200).send('Move made');
    } else {
      res.status(400).send('Invalid move');
    }
  } else {
    res.status(404).send('Game not found');
  }
};

export const currentTurn = async (req: Request, res: Response): Promise<void> => {
  const game = gameHandler.getGameById(req.params.gameid);
  const username = req.body.username || req.body.userName;
  if (game) {
    if (game.lobby.getUsers().length == 1) {
      res.status(206).send('Opponent left');
      return;
    }
    if (game.isFinished) {
      res.status(200).send('Game is finished');
      return;
    }
    if (username === game.getCurrentTurnUser().username) {
      // send the gamestate to the client
      res.status(200).send(game.getGameStateJSON());
    } else {
      res.status(200).send('Not your turn');
    }
  } else {
    res.status(404).send('Game not found (or not created yet because lack of opponent)');
  }
};

export const getTerrain = async (req: Request, res: Response): Promise<void> => {
  const game = gameHandler.getGameById(req.params.gameid);
  if (game) {
    res.status(200).send(game.terrain.getYValues());
  } else {
    res.status(404).send('Game not found');
  }
};
