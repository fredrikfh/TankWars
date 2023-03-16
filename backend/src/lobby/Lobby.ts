import { User } from '../../types/User';
import { IGame } from '../interfaces/IGame';
import { ILobby } from '../interfaces/ILobby';

/**
 * A simple class for defining a lobby with a set of users.
 * Used to create a game.
 *
 * @class Lobby
 * @property {User[]} users - The users in the lobby
 * @property {string} id - The id of the lobby
 * @method addUser - Adds a user to the lobby
 * @method removeUser - Removes a user from the lobby
 * @method getUsers - Returns all users from the lobby
 */

export class Lobby implements ILobby {
  constructor() {
    this.users = [];
    this.id =
      Math.random().toString(36).substring(2, 15) +
      Math.random().toString(36).substring(2, 15);
  }
  getId(): string {
    return this.id;
  }

  private users: User[];
  private game?: IGame;
  id: string;

  addUser(user: User) {
    this.users.push(user);
  }

  removeUser(user: User) {
    this.users = this.users.filter((u) => u.id !== user.id);
  }

  addGame(game: IGame) {
    this.game = game;
  }

  getUsers() {
    return this.users;
  }
}
