// class for handling game logic

import { User } from '../../types/User';
import { checkProjectileHit } from '../functions/checkProjectileHit';
import { log } from '../functions/console';
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
  users: [User, IStats][]; // left [0] and right [1] user

  constructor(lobby: ILobby) {
    this.lobby = lobby;
    this.gameStatus = false; // game not finished
    this.gameId = Math.random().toString(36);
    this.terrain = new Terrain();

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
    log('Game ' + this.gameId + ' created.');
  }
  getTerrain(): ITerrain {
    return this.terrain;
  }

  getUsers(): [User, IStats][] {
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

  calculateNextGameState(newGameState: IGame): void {
    // verify that json is a valid IGame object

    // compare the current game state with the new game state
    if (newGameState.gameId !== this.gameId) {
      log('Game ID mismatch');
      return;
    }

    if (newGameState.gameStatus !== this.gameStatus) {
      log('Game status mismatch');
      return;
    }

    if (newGameState.users.length !== this.users.length) {
      log('User length mismatch');
      return;
    }

    // check if a projectile was fired (we assume that the projectile is always fired by the current user)
    if (
      true
      // this.getUsers()[this.getCurrentTurn()][1].getAmmunition() !==
      // newGameState.getUsers()[newGameState.getCurrentTurn()][1].getAmmunition()
    ) {
      // check if projectile hit a tank (use the turret angle and the tank position)
      if (checkProjectileHit(newGameState)) {
        // The user that was hit is the one not having the current turn
        const hitUserIndex = newGameState.currentTurn === 0 ? 1 : 0;
        const hitUserStats = newGameState.users[hitUserIndex][1];

        // Decrease health by 1 of the user that was hit
        const newHealth = hitUserStats.health - 1;
        hitUserStats.health = newHealth;

        // If health is 0, set health to 0 and set tank destroyed to true
        const tankDestroyed = newHealth <= 0;
        hitUserStats.health = tankDestroyed ? 0 : newHealth;

        // Calculate new score
        const shooterUserIndex = newGameState.currentTurn;
        const shooterUserStats = newGameState.users[shooterUserIndex][1];
        // const currentScore = shooterUserStats.getScore();

        // TODO this will not be saved.
        // If tank destroyed, add 100 points to the score
        // If tank not destroyed, add 10 points to the score
        // const scoreIncrease = tankDestroyed ? 100 : 10;
        // shooterUserStats.setScore(currentScore + scoreIncrease);

        // if tank is destroyed, end the game
        if (tankDestroyed) {
          newGameState.gameStatus = false;
        }
      }

      this.setGameState(newGameState);
      this.toggleTurn();
    }
    log('Game state updated for game ' + this.gameId);
  }

  // update the game state (does not update the current turn)
  setGameState(gameState: IGame): void {
    this.gameStatus = gameState.gameStatus;
    this.users = gameState.users;
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

  static getGameFromJSON(json: string): Game {
    const game = JSON.parse(json) as Game;
    return game;
  }
}
