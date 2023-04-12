import { User } from '../../types/User';

export interface ILobby {
  id: number;
  gameId: string | undefined;

  addUser(user: User): void;
  removeUser(user: User): void;
  getUsers(): User[];
  getId(): number;
  setId(id: number): void;
  setGameId(gameId: string): void;
  getGameId(): string | undefined;

  // maybe add a relation to a game?
}
