import { User } from '../../types/User';
import { ILobby } from './ILobby';
import { IStats } from './IStats';

// interface for a game instance

export interface IGame {
  notifyUsers(): void;

  setIsFinished(status: boolean): void;

  getWinner(): User;
  getLoser(): User;
  setScore(user: number, score: number): void;
  getScore(user: number): number;

  calculateNextGameState(newGameStateJSON: string): void;
  getGameState(): IGame;

  getCurrentTurnUser(): User;
  getGameStateJSON(): string;

  users: [User, IStats][];
  currentTurn: number;
  lobby: ILobby;
  gameStatus: boolean;
  gameId: string;
}
