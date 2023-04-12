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
  calculateNextGameState(newGameState: IGame): void;

  getGameState(): IGame;
  setGameState(gameState: IGame): void;

  getCurrentTurnUser(): User;
  getCurrentTurn(): number;
  setCurrentTurn(turn: number): void;
  getGameStateJSON(): string;

  setGameStatus(status: boolean): void;
  toggleTurn(): void;

  getUsers(): [User, IStats][];

  users: [User, IStats][];
  currentTurn: number;
  lobby: ILobby;
  gameStatus: boolean;
  gameId: string;
}
