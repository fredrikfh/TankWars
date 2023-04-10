import { User } from '../../types/User';

export interface ILobby {
  addUser(user: User): void;
  removeUser(user: User): void;
  getUsers(): User[];
  id: string;
  getId(): string;
  isFull(): boolean;
  // maybe add a relation to a game?
}
