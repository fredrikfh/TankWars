import { User } from '../../types/User';
import { ILobby } from './ILobby';
import { IStats } from './IStats';
import { ITerrain } from './ITerrain';

// interface for a game instance

export interface IGame {
  users: Array<{ user: User; stats: IStats }>;
  currentTurn: number;
  lobby: ILobby;
  gameStatus: boolean;
  gameId: string;
  terrain: ITerrain;
  lastActionTimeStamp: number;

  notifyUsers(): void;

  setIsFinished(status: boolean): void;

  getWinner(): User;
  getLoser(): User;
  setScore(user: number, score: number): void;
  getScore(user: number): number;
  calculateNextGameState(newGameState: IGame): boolean;

  getGameState(): IGame;
  setGameState(gameState: IGame): void;

  getCurrentTurnUser(): User;
  getCurrentTurn(): number;
  setCurrentTurn(turn: number): void;
  getGameStateJSON(): string;

  setGameStatus(status: boolean): void;
  toggleTurn(): void;

  getUsers(): Array<{ user: User; stats: IStats }>;

  getTerrain(): ITerrain;
  getLastActionTimeStamp(): number;
}
