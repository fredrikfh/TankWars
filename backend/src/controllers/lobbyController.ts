import { Request, Response } from 'express';
import { Lobby } from '../models/Lobby';
import { GameHandler } from '../gameHandler';
import { getUserById } from '../functions/getUserById';
import { User } from '../../types/User';

const gameHandler = GameHandler.getInstance();

export const leaveLobby = async (req: Request, res: Response): Promise<void> => {
  const id = parseInt(req.params.id);
  const lobby = gameHandler.getLobbyById(id);
  const user = (await getUserById(req.body.username)) as User;
  lobby?.removeUser(user);
  // if lobby is empty, dispose
  if (lobby?.getUsers().length === 0) {
    gameHandler.removeLobby(lobby);
    res.status(200).send('Lobby disposed');
    return;
  }
  res.status(200).send('left lobby');
};

export const joinLobby = async (req: Request, res: Response): Promise<void> => {
  // join a lobby with specific id
  const id = parseInt(req.params.id);
  const user = (await getUserById(req.body.username)) as User;

  if (gameHandler.getLobbyById(id)) {
    const lobby = gameHandler.getLobbyById(id);

    // check if lobby is full
    if (lobby?.getUsers().length === 2) {
      res.status(429).send('Lobby is full');
      return;
    }

    if (!lobby) {
      res.status(404).send('Lobby not found');
      return;
    }

    // Check if the user instance already exists in the lobby users
    if (lobby.getUsers().some((lobbyUser) => lobbyUser.username === user.username)) {
      res.status(409).send('User already in lobby');
      return;
    }

    lobby.addUser(user);
    gameHandler.createGame(lobby); // start game since 2 players are in lobby
    res.status(200).send('Joined lobby');
  } else {
    // if lobby doesn't exist, create a new lobby
    const lobby = gameHandler.createLobby(id);
    lobby.addUser(user);
    res.status(201).send('Created lobby');
  }
};
