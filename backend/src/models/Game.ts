import { User } from '../../types/User';
import { log } from '../functions/console';
import { IGame } from '../interfaces/IGame';
import { ILobby } from '../interfaces/ILobby';
import { IStats } from '../interfaces/IStats';
import { Stats } from './Stats';
import { Terrain } from './Terrain';

// class for handling game logic
export class Game implements IGame {
  gameId = Math.random().toString(36);
  isFinished = false;
  terrain = new Terrain(5, 15, 10);
  currentTurn = Math.round(Math.random()); // 0 or 1 to determine who starts
  lastActionTimeStamp = Date.now();

  users: Array<{ user: User; stats: IStats }>; // left [0] and right [1] user
  lobby: ILobby;

  constructor(lobby: ILobby) {
    this.lobby = lobby;
    this.users = lobby.getUsers().map((user) => {
      return { user: user, stats: new Stats() };
    });
    this.users[0].stats.setPosition(100);
    this.users[1].stats.setPosition(900);
    log('Game ' + this.gameId + ' created.');
  }

  calculateNextGameState(newStats: IStats[]): boolean {
    // check if one of the users has 0 health
    if (newStats[0].health <= 0 || newStats[1].health <= 0) {
      this.isFinished = true;
      log('Game is finished (0 HP) ' + this.gameId);
    }

    // in the future, if we want to calculate the next game state, we can do it here

    this.users[0].stats = newStats[0];
    this.users[1].stats = newStats[1];
    this.toggleTurn();
    this.lastActionTimeStamp = Date.now();
    log('gameState updated for ' + this.gameId);
    return true;
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
      isFinished: this.isFinished,
      currentTurn: this.currentTurn,
      users: this.users,
    });
  }
}
