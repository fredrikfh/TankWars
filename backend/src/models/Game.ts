// class for handling game logic

import { User } from '../../types/User';
import { log } from '../functions/console';
import validateGameState from '../functions/validateGameState';
import { IGame } from '../interfaces/IGame';
import { ILobby } from '../interfaces/ILobby';
import { IStats } from '../interfaces/IStats';
import { ITerrain } from '../interfaces/ITerrain';
import { Stats } from './Stats';
import { Terrain } from './Terrain';

export class Game implements IGame {
  currentTurn: number;
  lobby: ILobby;
  gameStatus: boolean;
  gameId: string;
  terrain: ITerrain;
  users: Array<{ user: User; stats: IStats }>; // left [0] and right [1] user
  lastActionTimeStamp: number;

  constructor(lobby: ILobby) {
    this.lobby = lobby;
    this.gameStatus = false; // game not finished
    this.gameId = Math.random().toString(36);
    this.terrain = new Terrain(5, 15, 10);
    this.lastActionTimeStamp = Date.now();

    // insert the lobby users into the game and create a new stats object for each user
    this.users = lobby.getUsers().map((user) => {
      return { user: user, stats: new Stats() };
    });

    this.users[0].stats.setPosition(100);
    this.users[1].stats.setPosition(900);

    // set the stats for the left and right user
    // todo add random tank type
    this.users[0].stats.setTankType('M107');
    this.users[0].stats.setTankDirection('left');
    this.users[0].stats.setIsMirrored(false); // this mirroring can also be done locally.

    this.users[1].stats.setTankType('M1A2');
    this.users[1].stats.setTankDirection('right');
    this.users[1].stats.setIsMirrored(true);

    // make random number 0 or 1 to determine who starts
    this.currentTurn = Math.round(Math.random());

    this.notifyUsers(); // TODO: implement this method
    log('Game ' + this.gameId + ' created.');
  }
  getLastActionTimeStamp(): number {
    return this.lastActionTimeStamp;
  }
  getTerrain(): ITerrain {
    return this.terrain;
  }

  getUsers(): Array<{ user: User; stats: IStats }> {
    return this.users;
  }

  setIsFinished(status: boolean): void {
    this.gameStatus = status;
  }

  notifyUsers(): void {
    log('[TODO] Notifying users...');
  }

  setGameStatus(status: boolean): void {
    this.gameStatus = status;
  }

  getGameStatus(): boolean {
    return this.gameStatus;
  }

  setScore(user: number, score: number): void {
    if (!this.isValidUserNumber(user)) {
      throw new Error('Unable to update score. Invalid userID.');
    }
    this.users[user].stats.setScore(score);
  }

  getScore(user: number): number {
    if (!this.isValidUserNumber(user)) {
      throw new Error('Unable to get score. Invalid userID.');
    }
    return this.users[user].stats.getScore();
  }

  getWinner(): User {
    // return the the user with the highest score in stats
    if (this.getGameStatus() == false) {
      throw new Error('Game is not finished');
    }
    if (this.getScore(0) > this.getScore(1)) {
      return this.users[0].user;
    } else {
      return this.users[1].user;
    }
  }
  getLoser(): User {
    // return the the user with the highest score in stats (duplicate code from getWinner)
    if (this.getGameStatus() == false) {
      throw new Error('Game is not finished');
    }
    if (this.getScore(0) < this.getScore(1)) {
      return this.users[0].user;
    } else {
      return this.users[1].user;
    }
  }

  calculateNextGameState(newGameState: IGame): boolean {
    // verify that json is a valid IGame object

    // verify that gameStates are valid
    if (!validateGameState(this, newGameState)) {
      log("Game state is not valid. Can't update game state.");
      return false;
    }

    this.setGameState(newGameState);
    this.toggleTurn();

    log('Game state updated for game ' + this.gameId);
    return true;
  }

  // update the game state (does not update the current turn)
  setGameState(gameState: IGame): void {
    this.gameStatus = gameState.gameStatus;
    this.users = gameState.users;
    this.lastActionTimeStamp = Date.now();
  }

  getGameState(): IGame {
    throw new Error('Method not implemented.');
  }

  isValidUserNumber(userNumber: number): boolean {
    return userNumber === 0 || userNumber === 1;
  }

  getCurrentTurn(): number {
    return this.currentTurn;
  }

  setCurrentTurn(turn: number): void {
    // check if turn is 0 or 1
    if (turn === 0 || turn === 1) {
      this.currentTurn = turn; // TODO validate number
    } else {
      throw new Error('Invalid turn');
    }
  }

  toggleTurn(): void {
    this.currentTurn = this.currentTurn === 0 ? 1 : 0;
  }

  getCurrentTurnUser(): User {
    return this.users[this.currentTurn].user;
  }

  // make json object of the game state (to send to clients)
  getGameStateJSON(): string {
    return JSON.stringify({
      gameId: this.gameId,
      gameStatus: this.gameStatus,
      currentTurn: this.currentTurn,
      users: this.users,
    });
  }

  static getGameFromJSON(json: string): Game {
    const game = JSON.parse(json) as Game;
    return game;
  }
}
