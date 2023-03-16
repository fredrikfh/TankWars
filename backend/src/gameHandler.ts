// this module is responsible for handling the ongoing games.

import { Game } from './game/Game';
import { IGame } from './interfaces/IGame';
import { ILobby } from './interfaces/ILobby';
import { Lobby } from './lobby/Lobby';

// this class is responsible for handling the ongoing games. and the lobbies.

export class GameHandler {
  private static instance: GameHandler;

  static getInstance(): GameHandler {
    if (!GameHandler.instance) {
      GameHandler.instance = new GameHandler();
    }
    return GameHandler.instance;
  }

  private lobbies: ILobby[] = [];
  private games: IGame[] = [];

  constructor() {
    this.lobbies = [];
    this.games = [];
  }

  addLobby(lobby: ILobby) {
    this.lobbies.push(lobby);
  }

  createGame(lobby: ILobby) {
    const game = new Game(lobby);
    this.addGame(game);
    return game;
  }

  removeLobby(lobby: ILobby) {
    this.lobbies = this.lobbies.filter((l) => l.id !== lobby.id);
  }

  addGame(game: IGame) {
    this.games.push(game);
  }

  removeGame(game: IGame) {
    this.games = this.games.filter((g) => g.gameId !== game.gameId);
  }

  getLobbies() {
    return this.lobbies;
  }

  getGames() {
    return this.games;
  }

  getGameById(gameId: string) {
    return this.games.find((g) => g.gameId === gameId);
  }

  getLobbyById(id: string) {
    return this.lobbies.find((l) => l.id === id);
  }

  createLobby() {
    const lobby = new Lobby();
    this.addLobby(lobby);
    return lobby;
  }
}
