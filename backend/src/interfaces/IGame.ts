import { User } from '../../types/User';
import { ILobby } from './ILobby';
import { IStats } from './IStats';
import { ITerrain } from './ITerrain';

// interface for a game instance

export interface IGame {
  users: Array<{ user: User; stats: IStats }>;
  currentTurn: number;
  lobby: ILobby;
  gameId: string;
  terrain: ITerrain;
  lastActionTimeStamp: number;
  isFinished: boolean;

  getGameStateJSON(): string;
  /**
   * Send in a new turn (game state), do some calculations and update the game state.
   * @param newGameState
   */
  calculateNextGameState(newStats: IStats[]): boolean;
  toggleTurn(): void;
  getCurrentTurnUser(): User;
}
