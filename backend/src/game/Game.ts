// class for handling game logic

import { User } from '../../types/User';
import { IGame } from '../interfaces/IGame';
import { ILobby } from '../interfaces/ILobby';
import { IStats } from '../interfaces/IStats';
import { Stats } from './Stats';

export class Game implements IGame {
  currentTurn: number;
  lobby: ILobby;
  gameStatus: boolean;
  gameId: string;
  users: [User, IStats][]; // left [0] and right [1] user

  constructor(lobby: ILobby) {
    this.lobby = lobby;
    this.gameStatus = false; // game not finished
    this.gameId = Math.random().toString(36);

    // insert the lobby users into the game and create a new stats object for each user
    this.users = lobby.getUsers().map((user) => [user, new Stats()]);

    // set the stats for the left and right user
    // todo add random tank type
    this.users[0][1].setTankType('M107');
    this.users[0][1].setTankDirection('left');
    this.users[0][1].setIsMirrored(true); // this mirroring can also be done locally.

    this.users[1][1].setTankType('M1A2');
    this.users[1][1].setTankDirection('right');
    this.users[1][1].setIsMirrored(false);

    // make random number 0 or 1 to determine who starts
    this.currentTurn = Math.round(Math.random());

    this.notifyUsers(); // TODO: implement this method
  }

  setIsFinished(status: boolean): void {
    this.gameStatus = status;
  }

  notifyUsers(): void {
    throw new Error('Method not implemented.');
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
    this.users[user][1].setScore(score);
  }

  getScore(user: number): number {
    if (!this.isValidUserNumber(user)) {
      throw new Error('Unable to get score. Invalid userID.');
    }
    return this.users[user][1].getScore();
  }

  getWinner(): User {
    // return the the user with the highest score in stats
    if (this.getGameStatus() == false) {
      throw new Error('Game is not finished');
    }
    if (this.getScore(0) > this.getScore(1)) {
      return this.users[0][0];
    } else {
      return this.users[1][0];
    }
  }
  getLoser(): User {
    // return the the user with the highest score in stats (duplicate code from getWinner)
    if (this.getGameStatus() == false) {
      throw new Error('Game is not finished');
    }
    if (this.getScore(0) < this.getScore(1)) {
      return this.users[0][0];
    } else {
      return this.users[1][0];
    }
  }
  calculateNextGameState(newGameStateJSON: string): void {
    throw new Error('Method not implemented.');
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

  getCurrentTurnUser(): User {
    return this.users[this.currentTurn][0];
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
}
