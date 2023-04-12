import { Request, Response } from 'express';
import { Lobby } from '../models/Lobby';
import { GameHandler } from '../gameHandler';
import { getUserById } from '../functions/getUserById';
import { User } from '../../types/User';
import admin from '../functions/firebaseAdmin';
import { IGame } from '../interfaces/IGame';

const gameHandler = GameHandler.getInstance();

// endpoint to get current gameId of a lobby
export const gameId = async (req: Request, res: Response): Promise<void> => {
  const lobby = gameHandler.getLobbyById(parseInt(req.params.lobbyId));
  if (lobby) {
    res.status(200).send(lobby.getGameId());
  } else {
    res.status(404).send('Lobby does not have ongoing game');
  }
};

export const move = async (req: Request, res: Response): Promise<void> => {
  const game = gameHandler.getGameById(req.params.gameid);
  // const newGameState = JSON.parse(req.body) as Game;
  if (game) {
    // if (!validateGameStateJSON(req.body)) {
    //   res.status(400).send('Invalid gamestate');
    //   return;
    // }

    game.calculateNextGameState(req.body as IGame);

    res.status(200).send('Move made');
  } else {
    res.status(404).send('Game not found');
  }
};

export const currentTurn = async (req: Request, res: Response): Promise<void> => {
  const game = gameHandler.getGameById(req.params.gameid);
  const user = req.body.userName;
  if (game) {
    if (user === game.getCurrentTurnUser().username) {
      // send the gamestate to the client
      res.status(200).send(game.getGameStateJSON());
    } else {
      res.status(200).send('Not your turn');
    }
  } else {
    res.status(404).send('Game not found (or not created yet because lack of opponent)');
  }
};
